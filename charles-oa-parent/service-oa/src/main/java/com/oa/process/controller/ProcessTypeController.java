package com.oa.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.model.process.ProcessType;
import com.oa.common.result.Result;
import com.oa.process.service.Impl.ProcessTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-28-13:34
 */

@Api(value = "审批类型", tags = "审批类型")
@RestController
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {
    @Autowired
    private ProcessTypeServiceImpl processTypeService;

    @ApiOperation("分页查询")
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @GetMapping("/{page}/{limit}")
    public Result getAllProcessTypeByPage(@PathVariable("limit") Long limit,
                                          @PathVariable("page") Long page){
        Page<ProcessType> myPage = new Page(page,limit);
        Page<ProcessType> processTypePage = processTypeService.page(myPage);

        return Result.success(processTypePage);
    }

    @ApiOperation("根据id获取")
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @GetMapping("/findAll")
    public Result getAllType(){
        List<ProcessType> list = processTypeService.list();

        return Result.success(list);
    }

    @ApiOperation("根据id获取")
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @GetMapping("/get/{id}")
    public Result getOneTypeById(@PathVariable("id") Long id){
        ProcessType processType = processTypeService.getById(id);

        if(processType == null)
            return Result.fail("找不到该记录");

        return Result.success(processType);
    }

    @ApiOperation("修改")
    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @PutMapping("/update")
    public Result updateProcessType(@RequestBody ProcessType processType){
        boolean isUpdate = processTypeService.updateById(processType);

        if(isUpdate)
            return Result.success("修改成功");
        else
            return Result.fail("修改失败");
    }

    @ApiOperation("添加")
    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @PostMapping("/add")
    public Result addProcessType(@RequestBody ProcessType processType){
        boolean isUpdate = processTypeService.saveOrUpdate(processType);

        if(isUpdate)
            return Result.success("添加成功");
        else
            return Result.fail("添加失败");
    }

    @ApiOperation("删除")
    @PreAuthorize("hasAuthority('bnt.processType.delete')")
    @DeleteMapping("/delete/{id}")
    public Result deleteProcessType(@PathVariable("id") Long id){
        boolean isUpdate = processTypeService.removeById(id);

        if(isUpdate)
            return Result.success("删除成功");
        else
            return Result.fail("删除失败");
    }
}
