package com.oa.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.vo.process.ProcessQueryVo;
import com.charles.vo.process.ProcessVo;
import com.oa.common.result.Result;
import com.oa.process.service.Impl.ProcessServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Charles
 * @create 2023-06-07-0:33
 */

@Api(tags = "审批处理")
@RestController
@RequestMapping("/admin/process")
public class ProcessController {

    @Autowired
    private ProcessServiceImpl processService;

    //分页条件查询所有记录
    @ApiOperation("分页查找")
    @PreAuthorize("hasAuthority('bnt.process.list')")
    @GetMapping("/{page}/{limit}")
    public Result findAll(@PathVariable("page") Long page,
                          @PathVariable("limit") Long limit,
                          ProcessQueryVo processQueryVo){
        Page<ProcessVo> myPage = new Page<>(page,limit);
        //封装查询所有条件
        IPage<ProcessVo> pageList = processService.selectPage(myPage,processQueryVo);

        return Result.success(pageList);
    }
}
