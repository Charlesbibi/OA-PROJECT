package com.oa.process.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.process.ProcessTemplate;
import com.charles.model.process.ProcessType;
import com.oa.process.mapper.ProcessTemplateMapper;
import com.oa.process.service.ProcessTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charles
 * @create 2023-05-28-14:19
 */

@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate>
        implements ProcessTemplateService {

    @Autowired
    private ProcessTemplateMapper processTemplateMapper;
    @Autowired
    private ProcessTypeServiceImpl processTypeService;
    @Autowired
    private ProcessServiceImpl processService;

    //封装模板名称
    @Override
    public Page<ProcessTemplate> wrapperWithTemplateName(Page<ProcessTemplate> pageParam) {
        //顺序排列
        LambdaQueryWrapper<ProcessTemplate> queryWrapper = new LambdaQueryWrapper<ProcessTemplate>();
        queryWrapper.orderByDesc(ProcessTemplate::getId);

        Page<ProcessTemplate> page = processTemplateMapper.selectPage(pageParam, queryWrapper);
        List<ProcessTemplate> processTemplateList = page.getRecords();


        //获取id集合
        List<Long> processTypeIds = processTemplateList.stream()
                .map(processTemplate -> processTemplate.getProcessTypeId())
                .collect(Collectors.toList());

        //遍历processId
        if(!CollectionUtils.isEmpty(processTypeIds)){
            Map<Long, String> nameMap = processTypeService.list(new LambdaQueryWrapper<ProcessType>()
                    .in(ProcessType::getId, processTypeIds)).stream()
                    .collect(Collectors.toMap(ProcessType::getId, ProcessType::getName));

            for(ProcessTemplate processTemplate : processTemplateList){
                String name = nameMap.get(processTemplate.getProcessTypeId());

                if(StringUtils.isEmpty(name))
                    continue;
                processTemplate.setProcessTypeName(name);
            }
        }

        return page;
    }

    @Override
    public Boolean publish(Long id) {
        ProcessTemplate processTemplate = processTemplateMapper.selectById(id);
        // 1代表已经发步
        processTemplate.setStatus(1);
        int success = baseMapper.updateById(processTemplate);

        //发步
        if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath()))
            processService.publishProcess(processTemplate.getProcessDefinitionPath());
        else
            throw new RuntimeException("文件路径没有指定");

        return success==1;
    }
}
