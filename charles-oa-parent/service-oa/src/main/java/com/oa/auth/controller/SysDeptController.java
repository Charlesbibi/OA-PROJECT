package com.oa.auth.controller;

import com.charles.model.system.SysDept;
import com.charles.model.system.SysUser;
import com.oa.auth.service.impl.SysDeptServiceImpl;
import com.oa.auth.service.impl.SysUserServiceImpl;
import com.oa.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author Charles
 * @create 2023-06-23-0:59
 */

@RestController
@Api(tags = "部门管理")
@RequestMapping("/admin/system/sysDept")
public class SysDeptController {

    @Autowired
    private SysDeptServiceImpl sysDeptService;
    @Autowired
    private SysUserServiceImpl sysUserService;

    //层级展示数据
    @ApiOperation("显示层级数据")
    @PreAuthorize("hasAuthority('bnt.sysDept.list')")
    @GetMapping("/treeNode")
    public Result treeNode(){
        List<SysDept> nodes = sysDeptService.findTreeNode();

        return Result.success(nodes);
    }

    //添加数据
    @ApiOperation("添加数据")
    @PreAuthorize("hasAuthority('bnt.sysDept.add')")
    @PostMapping("/add")
    public Result add(@RequestBody SysDept sysDept){
        if(StringUtils.isEmpty(sysDept.getTreePath())){
            sysDept.setTreePath(getTreePathById(sysDept));
        }

        boolean isAdd = sysDeptService.save(sysDept);

        if(isAdd)
            return Result.success("添加成功");
        else
            return Result.fail("添加失败");
    }

    private String getTreePathById(SysDept sysDept) {
        Long parentId = sysDept.getParentId();
        String value = "";

        if(parentId != 0){
            SysDept dept = sysDeptService.getById(parentId);
            value = value.concat(getTreePathById(dept));
        }
        if(sysDept.getId() == null)
            return value;
        else
            return value.concat("," + sysDept.getId().toString());
    }

    //获取路径id数组数据
    @ApiOperation("获取路径id数组数据")
    @PreAuthorize("hasAuthority('bnt.sysDept.list')")
    @GetMapping("/findTreePath/{id}")
    public Result findTreePath(@PathVariable("id") Long id){
        //没有设置部门
        if(id.equals(0L)){
            return Result.success(new long[0]);
        }

        SysDept one = sysDeptService.getById(id);

        if(one != null){
            String treePath = one.getTreePath();
            treePath = treePath.concat(","+one.getId().toString());
            String[] tree = treePath.split(",");

            //string数组转换为long数组
            long[] longList = Arrays.stream(tree).mapToLong(s -> Long.parseLong(s.trim())).toArray();
            return Result.success(longList);
        }
        return Result.fail();
    }

    //获取路径id数组数据
    @ApiOperation("修改部门")
    @PreAuthorize("hasAuthority('bnt.sysDept.allocateDept')")
    @PutMapping("/allocateDept/{id}/{deptId}")
    public Result allocateDept(@PathVariable("id") Long id,
                               @PathVariable("deptId") Long deptId){
        SysUser user = sysUserService.getById(id);

        user.setDeptId(deptId);
        boolean isUpdate = sysUserService.updateById(user);
        if(isUpdate)
            return Result.success();
        else
            return Result.fail();
    }

    //删除数据
    @ApiOperation("删除数据")
    @PreAuthorize("hasAuthority('bnt.sysDept.remove')")
    @DeleteMapping("/remove/{id}")
    public Result add(@PathVariable("id") Long id){
        boolean isRemove = sysDeptService.removeById(id);

        if(isRemove)
            return Result.success("删除成功");
        else
            return Result.fail("删除失败");
    }

    //修改数据
    @ApiOperation("修改数据")
    @PreAuthorize("hasAuthority('bnt.sysDept.update')")
    @PutMapping("/update")
    public Result update(@RequestBody SysDept sysDept){
        boolean isUpdate = sysDeptService.updateById(sysDept);

        if(isUpdate)
            return Result.success("修改成功");
        else
            return Result.fail("修改失败");
    }
}
