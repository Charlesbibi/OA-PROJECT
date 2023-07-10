package com.oa.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.model.system.SysRole;
import com.charles.vo.system.SysRoleQueryVo;
import com.oa.common.result.Result;
import com.oa.auth.service.impl.SysRoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Charles
 * @create 2023-04-01-11:22
 */

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController{

    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @ApiOperation(value = "获取所有role")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/getAllRole")
    public Result<List<SysRole>> getAllSysRole(){
        List<SysRole> sysRoles = sysRoleService.list();
        return Result.success(sysRoles);
    }

    @ApiOperation(value = "条件分页获取role")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/{page}/{limit}")
    public Result getAllSysRoleByPage(@PathVariable("page") Integer page,
                                      @PathVariable("limit") Integer limit,
                                      SysRoleQueryVo sysRoleQueryVo){
        //1 创建page，传入页面相关信息
        Page<SysRole> myPage = new Page<>(page,limit);
        //2 封装条件
        String roleName = sysRoleQueryVo.getRoleName();
        LambdaQueryWrapper<SysRole> qw = new LambdaQueryWrapper();
        if(!StringUtils.isEmpty(roleName)){
            qw.like(SysRole::getRoleName,roleName);
        }

        //3 调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(myPage, qw);

        return Result.success(pageModel);
    }

    @ApiOperation("根据id获取role")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/get/{id}")
    public Result getSysRoleById(@PathVariable("id") Integer id){
        SysRole sysRole = sysRoleService.getById(id);
        if(sysRole.equals(null))
            return Result.fail();
        return Result.success(sysRole);
    }

    @ApiOperation("添加role")
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @PostMapping("/insert")
    public Result insertSysRole(@RequestBody SysRole sysRole){
        boolean isSave = sysRoleService.save(sysRole);
        if(isSave)
            return Result.success();
        else
            return Result.fail();
    }

    @ApiOperation("修改role")
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @PutMapping("/update")
    public Result updateSysRole(@RequestBody SysRole sysRole){
        boolean isUpdate = sysRoleService.updateById(sysRole);
        if(isUpdate)
            return Result.success();
        else
            return Result.fail();
    }

    @ApiOperation("根据id删除role")
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteSysRole(@PathVariable("id") Integer id){
        boolean isRemove = sysRoleService.removeById(id);
        if(isRemove)
            return Result.success();
        else
            return Result.fail();
    }

    @ApiOperation("根据id批量删除role")
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/deleteByPatchId")
    public Result deletePatchSysRole(@RequestBody List<Integer> idList){
        boolean isRemove = sysRoleService.removeByIds(idList);
        if(isRemove)
            return Result.success();
        else
            return Result.fail();
    }
}
