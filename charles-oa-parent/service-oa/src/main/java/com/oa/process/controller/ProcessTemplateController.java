package com.oa.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.model.process.ProcessTemplate;
import com.oa.common.result.Result;
import com.oa.process.service.Impl.ProcessTemplateServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles
 * @create 2023-05-28-14:20
 */

@Api("审批模板")
@RestController
@RequestMapping("/admin/process/processTemplate")
public class ProcessTemplateController {

    @Autowired
    private ProcessTemplateServiceImpl processTemplateService;

    @ApiOperation("查找全部模板")
    @GetMapping("/getAll")
    public Result getAllTemplate(){
        List<ProcessTemplate> list = processTemplateService.list();

        return Result.success(list);
    }

    @ApiOperation("分页查找")
    @GetMapping("/{page}/{limit}")
    public Result getProcessTemplateByPage(@PathVariable("page") Long page,
                                           @PathVariable("limit") Long limit){
        Page<ProcessTemplate> myPage = new Page<>(page,limit);
        //封装模板名称
        Page<ProcessTemplate> cPage = processTemplateService.wrapperWithTemplateName(myPage);

        return Result.success(cPage);
    }

    @ApiOperation("根据id查找")
    @GetMapping("/get{id}")
    public Result getProcessTemplateById(@PathVariable("id") Long id){
        ProcessTemplate singleResult = processTemplateService.getOne(new LambdaQueryWrapper<ProcessTemplate>()
                .eq(ProcessTemplate::getId, id));

        if(singleResult == null)
            return Result.fail("查找失败");
        else
            return Result.success(singleResult);
    }

    @ApiOperation("增加")
    @PostMapping("/add")
    public Result addProcessTemplate(@RequestBody ProcessTemplate processTemplate){
        boolean isSave = processTemplateService.saveOrUpdate(processTemplate);

        if(!isSave)
            return Result.fail("添加失败");
        else
            return Result.success();
    }

    @ApiOperation("修改")
    @PutMapping("/update")
    public Result updateProcessTemplate(@RequestBody ProcessTemplate processTemplate){
        boolean isUpdate = processTemplateService.updateById(processTemplate);

        if(!isUpdate)
            return Result.fail("更新失败");
        else
            return Result.success();
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    public Result updateProcessTemplate(@PathVariable("id") Long id){
        boolean isDelete = processTemplateService.removeById(id);

        if(!isDelete)
            return Result.fail("删除失败");
        else
            return Result.success();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation("上传zip文件到classpath中")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadZipFile(@RequestParam("a") MultipartFile multipartFile) throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        String fileName = multipartFile.getOriginalFilename();

        // 上传目录
        File tempFile = new File(path + "/processes/");
        // 判断目录是否存着
        if (!tempFile.exists()) {
            tempFile.mkdirs();//创建目录
        }
        //创建文件接受
        File zipFile = new File(path + "/processes/" + fileName);
        try {
            multipartFile.transferTo(zipFile);
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }


        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
        return Result.success(map);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    @ApiOperation(value = "发布")
    @GetMapping("/publish/{id}")
    public  Result publish(@PathVariable("id") Long id) throws RuntimeException{
        Boolean isPublish = processTemplateService.publish(id);
        if(isPublish)
            return Result.success();
        else
            return Result.fail();
    }

}
