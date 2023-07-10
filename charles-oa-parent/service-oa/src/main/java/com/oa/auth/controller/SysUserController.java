package com.oa.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.model.system.*;
import com.charles.vo.system.SysUserQueryVo;
import com.oa.auth.service.impl.*;
import com.oa.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles
 * @create 2023-04-22-14:42
 */

@Api(tags = "用户管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysUserRoleServiceImpl sysUserRoleService;
    @Autowired
    private SysRoleServiceImpl sysRoleService;
    @Autowired
    private SysPostServiceImpl sysPostService;
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @ApiOperation(value = "获取当前用户基本信息")
    @GetMapping("/getCurrentUser")
    public Result getCurrentUser() {
        return Result.success(sysUserService.getCurrentUser());
    }

    @ApiOperation("分配的post")
    @PreAuthorize("hasAuthority('bnt.sysPost.allocatePost')")
    @GetMapping("/allocatePost/{userId}/{postId}")
    public Result allocatePost(@PathVariable("postId") Long postId,
                               @PathVariable("userId") Long userId){

        Boolean isSuccess = sysUserService.allocatePostById(userId,postId);
        if(isSuccess)
            return Result.success();
        else
            return Result.fail();
    }

    @ApiOperation(value = "获取指定用户对应的角色id")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/getAllUserRole/{id}")
    public Result getAllUserRole(@PathVariable("id") Long id){
        List<Long> allRoleIdLists = sysUserRoleService.getAllRole(id);
        //使用stream去重
        allRoleIdLists = allRoleIdLists.stream().distinct().collect(Collectors.toList());
        return Result.success(allRoleIdLists);
    }

    @ApiOperation(value = "获取指定id对应的角色岗位")
    @GetMapping("/getPostById/{userId}")
    public Result getPostById(@PathVariable("userId") Long userId){
        SysUser sysUser = sysUserService.getById(userId);
        if(!ObjectUtils.isEmpty(sysUser) & sysUser.getPostId() != null)
            return Result.success(sysUser.getPostId());
        else //找不到或者无分配
            return Result.success(0);
    }

    @ApiOperation(value = "修改用户的状态")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @PutMapping("/changeStatus/{id}/{status}")
    public Result changeStatus(@PathVariable("status") int status,
                               @PathVariable("id") Long id){
        boolean isChange = sysUserService.changeStatusService(status, id);
        if(isChange)
            return Result.success(isChange);
        else
            return Result.fail();
    }

    @ApiOperation(value = "获取指定用户对应的角色")
    @GetMapping("/getAllRole/{id}")
    public Result getAllRole(@PathVariable("id") Long id){
        List<Long> roleIDs = (List<Long>) getAllUserRole(id).getData();
        //设置默认值
        if(roleIDs.size() == 0)
            roleIDs.add(-1L);

        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysRole::getId,roleIDs);
        List<SysRole> query = sysRoleService.list(lambdaQueryWrapper);
        return Result.success(query);
    }

    @ApiOperation(value = "赋值指定的角色给用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @DeleteMapping("/assignRoles/{id}")
    public Result assignRoles(@RequestBody List<Long> roleIDs,
                              @PathVariable("id") Long uid){
        List<Long> originalIdList = (List<Long>) getAllUserRole(uid).getData();

        for(Long id : originalIdList){
            if(!roleIDs.contains(id))
                sysUserRoleService.assignRole(uid,id);
        }
        for(Long id : roleIDs){
            if(!originalIdList.contains(id)){
                SysUserRole sysUserRole = new SysUserRole(id,uid);
                sysUserRoleService.saveOrUpdate(sysUserRole);
            }
        }
        return Result.success();
    }


    @ApiOperation(value = "获取所有用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/getAllUser")
    public Result getAllUser(){
        List<SysUser> list = sysUserService.list();
        if(list.isEmpty())
            return Result.fail("当前没有员工");
        else
            return Result.success(list);
    }

    @ApiOperation(value = "分页查询所有用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/{page}/{limit}")
    public Result getAllUserByPage(@PathVariable("page") Long page,
                                   @PathVariable("limit") Long limit,
                                   SysUserQueryVo sysUserQueryVo){
        Page<SysUser> myPage = new Page<>(page,limit);
        //封装条件，判断条件值不为空
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取条件值
        String username = sysUserQueryVo.getKeyword();
        String realName = sysUserQueryVo.getName();
        String phone = sysUserQueryVo.getPhone();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件值不为空
        //like 模糊查询
        if(!StringUtils.isEmpty(username)) {
            wrapper.like(SysUser::getUsername,username);
        }
        if(!StringUtils.isEmpty(realName)) {
            wrapper.like(SysUser::getName,realName);
        }
        if(!StringUtils.isEmpty(phone)) {
            wrapper.like(SysUser::getPhone,phone);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        //le 小于等于
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }

        //调用mp的方法实现条件分页查询
        IPage<SysUser> pageModel = sysUserService.page(myPage, wrapper);
        List<SysUser> records = pageModel.getRecords();


        for(SysUser sysUser : records){
            //获取到每一个postName
            if(sysUser.getPostId() != null){
                SysPost sysPost = sysPostService.getById(sysUser.getPostId());
                sysUser.setPostName(sysPost.getName());
            }else{
                sysUser.setPostName("暂未分配岗位");
            }

            //获取到每一个deptName
            if(sysUser.getDeptId() == null)
                sysUser.setDeptName("暂未分配部门");
            else{
                String fullName = getFullDeptName(sysUser.getDeptId());
                sysUser.setDeptName(fullName);
            }

            //获取到用户对应的角色列表
            List<Long> allRole = sysUserRoleService.getAllRole(sysUser.getId());
            if(CollectionUtils.isEmpty(allRole))
                sysUser.setRoleList(null);
            else{
                List<SysRole> roles = sysRoleService.listByIds(allRole);
                sysUser.setRoleList(roles);
            }

        }

        pageModel.setRecords(records);

        return Result.success(pageModel);

    }

    //递归获取全部门名
    private String getFullDeptName(Long deptId) {
        SysDept sysDept = sysDeptService.getById(deptId);

        String fullName = "";
        if(sysDept.getParentId() != 0)
            fullName = fullName.concat(getFullDeptName(sysDept.getParentId()) + "-");

        return fullName.concat(sysDept.getName());
    }

    @ApiOperation(value = "根据id获取用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/get/{id}")
    public Result getUserById(@PathVariable("id") Long id){
        SysUser sysUser = sysUserService.getById(id);
        if(sysUser.equals(null))
            return Result.fail("找不到该员工");
        else
            return  Result.success(sysUser);
    }

    @ApiOperation(value = "根据id修改用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @PutMapping("/update")
    public Result updateUserById(@RequestBody SysUser sysUser){
        boolean isUpdate = sysUserService.updateById(sysUser);

        if(!isUpdate)
            return Result.fail("修改失败");
        else
            return Result.success();
    }

    @ApiOperation(value = "添加用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @PostMapping("/add")
    public Result addUser(@RequestBody SysUser sysUser){
        boolean isSave = sysUserService.save(sysUser);

        if(!isSave)
            return Result.fail("添加失败");
        else
            return Result.success();
    }

    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @DeleteMapping("/delete/{id}")
    public Result deleteUser(@PathVariable("id") Long id){
        boolean isDelete = sysUserService.removeById(id);

        if(!isDelete)
            return Result.fail("删除失败");
        else
            return Result.success();
    }
}
