package com.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.system.SysMenu;
import com.charles.vo.system.RouterVo;
import com.charles.vo.wechat.MenuVo;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-02-11:11
 */
public interface SysMenuService extends IService<SysMenu> {
    //获取树形节点
    List<SysMenu> getNode(List<SysMenu> sysMenus);
    //删除节点
    boolean removeMenuById(Long id);
    //根据id获取所有按钮列表
    List<String> getAllMenuListByUserId(Long userId);
    //根据id获取所有菜单列表
    List<RouterVo> getAllRouterListByUserId(Long userId);
}
