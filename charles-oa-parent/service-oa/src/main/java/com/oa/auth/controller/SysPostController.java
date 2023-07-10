package com.oa.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charles.model.system.SysPost;
import com.charles.vo.system.SysPostQueryVo;
import com.oa.auth.service.impl.SysPostServiceImpl;
import com.oa.auth.service.impl.SysUserServiceImpl;
import com.oa.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Charles
 * @create 2023-06-23-1:18
 */

@RestController
@Api(tags = "职位管理")
@RequestMapping("/admin/system/sysPost")
public class SysPostController {

    @Autowired
    private SysPostServiceImpl sysPostService;
    @Autowired
    private SysUserServiceImpl sysUserService;


    @ApiOperation("获取所有的post集合")
    @PostMapping("/list")
    public Result listAll(SysPostQueryVo sysPostQueryVo){
        List<SysPost> list;

        if(sysPostQueryVo == null || StringUtils.isEmpty(sysPostQueryVo.getName()))
            list = sysPostService.list();
        else{
            String name = sysPostQueryVo.getName();
            list = sysPostService.list(new LambdaQueryWrapper<SysPost>().eq(SysPost::getName,name));
        }
        return Result.success(list);
    }

    @ApiOperation("获取所有的post集合")
    @PutMapping("/changeStatus/{id}")
    public Result changeStatus(@PathVariable("id") Long id){
        sysPostService.changeStatus(id);

        return Result.success();
    }

    @ApiOperation("根据id查找指定的post")
    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable("id") Long id){
        SysPost sysPost = sysPostService.getById(id);
        if(sysPost == null){
            return Result.fail("找不到该职位");
        }

        return Result.success(sysPost);
    }

    @ApiOperation("根据id更新指定的post")
    @PutMapping("/updateById")
    public Result updateById(@RequestBody SysPost sysPost){
        Boolean isUpdate = sysPostService.updateById(sysPost);
        if(!isUpdate){
            return Result.fail("更新失败");
        }

        return Result.success();
    }

    @ApiOperation("根据id删除指定的post")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id){
        Boolean isDelete = sysPostService.removeById(id);
        if(!isDelete){
            return Result.fail("删除失败");
        }

        return Result.success();
    }

    @ApiOperation("添加指定的post")
    @PostMapping("/add")
    public Result add(@RequestBody SysPost sysPost){
        Boolean isSave = sysPostService.saveOrUpdate(sysPost);
        if(!isSave){
            return Result.fail("添加失败");
        }

        return Result.success();
    }
}
