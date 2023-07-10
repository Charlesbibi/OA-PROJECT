package com.oa.process.controller.Api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.model.process.Process;
import com.charles.model.process.ProcessTemplate;
import com.charles.model.process.ProcessType;
import com.charles.vo.process.ApprovalVo;
import com.charles.vo.process.ProcessFormVo;
import com.charles.vo.process.ProcessVo;
import com.oa.common.result.Result;
import com.oa.process.service.Impl.ProcessServiceImpl;
import com.oa.process.service.Impl.ProcessTemplateServiceImpl;
import com.oa.process.service.Impl.ProcessTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles
 * @create 2023-06-08-22:04
 */

@Api(tags = "审批流管理")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin
public class WCProcessController {

    @Autowired
    private ProcessTypeServiceImpl processTypeService;
    @Autowired
    private ProcessTemplateServiceImpl processTemplateService;
    @Autowired
    private ProcessServiceImpl processService;

    @ApiOperation(value = "获取所有模板")
    @GetMapping("/findProcessType")
    public Result findProcessType(){
        List<ProcessType> processTypeList =  processTypeService.findAllProcess();

        return Result.success(processTypeList);
    }

    @ApiOperation(value = "获取审批模板")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplateById(@PathVariable("processTemplateId") Long processTemplateId){
        ProcessTemplate template = processTemplateService.getById(processTemplateId);

        return Result.success(template);
    }

    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo){
        processService.start(processFormVo);
        return Result.success();
    }

    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page, @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        return Result.success(processService.findPending(pageParam));
    }

    @ApiOperation(value = "展示详情信息")
    @GetMapping("show/{id}")
    public Result show(@PathVariable("id") Long id) {
        Map<String,Object> resultMap = processService.show(id);
        return Result.success(resultMap);
    }

    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.success();
    }

    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        return Result.success(processService.findProcessed(pageParam));
    }

    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        return Result.success(processService.findStarted(pageParam));
    }
}
