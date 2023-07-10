package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysMenu;
import com.charles.vo.system.MetaVo;
import com.charles.vo.system.RouterVo;
import com.oa.auth.mapper.SysMenuMapper;
import com.oa.auth.mapper.SysRoleMenuMapper;
import com.oa.auth.menuConfig.MenuHelper;
import com.oa.auth.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles
 * @create 2023-05-02-11:12
 */

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper,SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //获取树形节点
    @Override
    public List<SysMenu> getNode(List<SysMenu> sysMenuList) {
        //非空判断
        if (CollectionUtils.isEmpty(sysMenuList)) return null;

        //构建树形数据
        List<SysMenu> result = MenuHelper.buildTree(sysMenuList);
        return result;
    }

    @Override
    public boolean removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> sq = new LambdaQueryWrapper<>();
        sq.eq(SysMenu::getParentId,id);
        Integer count = sysMenuMapper.selectCount(sq);
        if(count!=0)
            return false;

        sysMenuMapper.deleteById(id);
        return true;
    }

    //根据id获取所有按钮列表
    @Override
    public List<String> getAllMenuListByUserId(Long userId) {
        //规定1为测试管理员，具备所有权限
        List<SysMenu> sysMenuList = null;
        if(userId == 1){
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,1));
        }else{
            sysMenuList = sysRoleMenuMapper.getAllMenuById(userId);
        }

        List<String> permsList = sysMenuList.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());
        return permsList;
    }

    //根据id获取所有菜单列表
    @Override
    public List<RouterVo> getAllRouterListByUserId(Long userId) {

        //规定1为测试管理员，具备所有权限
        List<SysMenu> sysMenuList = null;
        if(userId == 1){
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,1).
                    orderByAsc(SysMenu::getSortValue));
        }else{
            sysMenuList = sysRoleMenuMapper.getAllMenuById(userId);
        }
        //构建树形结构
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        //将结果封装为前端相同的格式
        List<RouterVo> routerVos = this.buildMenus(sysMenuTreeList);
        return routerVos;
    }

    //封装前端菜单
    public List<RouterVo> buildMenus(List<SysMenu> sysMenuList) {
        List<RouterVo> routers = new LinkedList<RouterVo>();

        for(SysMenu menu : sysMenuList){
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if(menu.getType().intValue() == 1){
                List<SysMenu> hiddenMenuList = children.stream().filter(item
                        -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                //重复将所有隐藏的节点遍历 并封装为前端路由结构
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            }else{
                if(!CollectionUtils.isEmpty(children)){
                    if(children.size() > 0){
                        router.setAlwaysShow(true);
                    }
                    //递归处理每一个结点
                    router.setChildren(buildMenus(children));
                }
            }
            routers.add(router);
        }

        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}
