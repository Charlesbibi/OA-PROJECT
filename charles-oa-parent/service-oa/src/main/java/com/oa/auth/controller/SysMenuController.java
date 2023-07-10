package com.oa.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charles.model.system.SysMenu;
import com.charles.model.system.SysRoleMenu;
import com.oa.auth.service.SysRoleMenuService;
import com.oa.common.result.Result;
import com.oa.auth.service.impl.SysMenuServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles
 * @create 2023-05-02-11:14
 */

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuServiceImpl sysMenuService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @ApiOperation("根据角色id获取其菜单")
    @GetMapping("/getAssign/{id}")
    public Result getAssignByRoleId(@PathVariable("id") Long id){
        //获取给该角色分配的idList
        List<Long> idList = sysRoleMenuService.getAssign(id);

        //通过idList获取到完整的菜单列表
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysMenu::getId,idList).eq(SysMenu::getStatus,1);
        List<SysMenu> roleMenuList = sysMenuService.list(lambdaQueryWrapper);

        List<SysMenu> node = sysMenuService.getNode(roleMenuList);
        List<Long> subList = getSubIdList(node,new ArrayList<Long>());

        return Result.success(subList);
    }

    private List<Long> getSubIdList(List<SysMenu> roleMenuList,List<Long> subList) {
        for(SysMenu sysMenu : roleMenuList){
            if(sysMenu.isSelect())
                continue;

            List<SysMenu> children = sysMenu.getChildren();
            if(CollectionUtils.isEmpty(children)) {
                subList.add(sysMenu.getId());
                sysMenu.setSelect(true);
            }
            else
                subList = getSubIdList(children,subList);
        }
        return subList;
    }

    @ApiOperation("更新菜单结构")
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @PutMapping("/doAssign/{id}")
    public Result doAssign(@PathVariable("id") Long id,
                           @RequestBody List<Long> idList){
        //获取给该角色分配的idList
        List<Long> originalIdList = sysRoleMenuService.getAssign(id);

        for(Long Id : originalIdList){
            if(!idList.contains(Id))
                sysRoleMenuService.deleteRoleMenu(id,Id);
        }
        for(Long Id : idList){
            if(!originalIdList.contains(Id)){
                SysRoleMenu sysRoleMenu = new SysRoleMenu(id,Id);
                sysRoleMenuService.saveOrUpdate(sysRoleMenu);
            }
        }
        return Result.success();
    }

    @ApiOperation("获取菜单树形结构")
    @GetMapping("/getNodeList")
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    public Result getNodeList(){
        List<SysMenu> sysMenus = sysMenuService.list();
        //转换为树形结构
        List<SysMenu> nodeList = sysMenuService.getNode(sysMenus);
        if(!CollectionUtils.isEmpty(nodeList))
            return Result.success(nodeList);
        else
            return Result.fail("当前没有菜单");
    }

    @ApiOperation("增加菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @PostMapping("/add")
    public Result addMenu(@RequestBody SysMenu sysMenu){
        boolean isSave = sysMenuService.save(sysMenu);
        if(isSave)
            return Result.success();
        else
            return Result.fail("添加失败");
    }

    @ApiOperation("修改菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @PutMapping("/update")
    public Result updateMenu(@RequestBody SysMenu sysMenu){
        boolean isUpdate = sysMenuService.updateById(sysMenu);
        if(isUpdate)
            return Result.success();
        else
            return Result.fail("修改失败");
    }

    @ApiOperation("删除菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @DeleteMapping("/delete/{id}")
    public Result deleteMenu(@PathVariable("id") Long id){
        boolean isDelete = sysMenuService.removeMenuById(id);
        if(isDelete)
            return Result.success();
        else
            return Result.fail("删除失败");
    }
}
