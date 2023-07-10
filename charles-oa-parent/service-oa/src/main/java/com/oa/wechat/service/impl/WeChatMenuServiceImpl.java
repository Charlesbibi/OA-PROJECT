package com.oa.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.wechat.Menu;
import com.charles.vo.wechat.MenuVo;
import com.oa.wechat.mapper.WeChatMenuMapper;
import com.oa.wechat.service.WeChatMenuService;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles
 * @create 2023-06-14-23:45
 */

@Service
public class WeChatMenuServiceImpl extends ServiceImpl<WeChatMenuMapper, Menu> implements WeChatMenuService {

    @Autowired
    private WeChatMenuMapper weChatMenuMapper;
    @Autowired
    private WxMpService wxMpService;

    //获取菜单树形结构
    @Override
    public List<MenuVo> findMenuInfo() {
        List<Menu> menus = weChatMenuMapper.selectList(null);

        //获取到所有的父级菜单
        List<Menu> menusParent = menus.stream().filter(menu -> menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());

        List<MenuVo> menuVoList = new ArrayList<>();
        for(Menu menu : menusParent){
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu,menuVo);

            //获取到所有的二级目录
            List<Menu> subMenuList = menus.stream().filter(myMenu -> myMenu.getParentId().longValue() == menu.getId())
                    .sorted(Comparator.comparing(Menu::getSort))
                    .collect(Collectors.toList());
            //遍历转换
            List<MenuVo> children = new ArrayList<>();
            for(Menu menu1 : subMenuList){
                MenuVo myMenuVo  = new MenuVo();
                BeanUtils.copyProperties(menu1,myMenuVo);
                children.add(myMenuVo);
            }

            menuVo.setChildren(children);
            menuVoList.add(menuVo);
        }

        return menuVoList;
    }

    //同步菜单
    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://charlesoa.free.idcfengye.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://charlesoa.free.idcfengye.com/#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        //推送菜单
        try {
            System.out.println(button.toJSONString());
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    //删除菜单
    @Override
    @SneakyThrows
    public void removeMenu(){
        wxMpService.getMenuService().menuDelete();
    }
}
