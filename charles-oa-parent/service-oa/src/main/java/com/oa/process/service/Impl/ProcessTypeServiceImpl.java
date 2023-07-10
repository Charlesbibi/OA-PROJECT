package com.oa.process.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.process.ProcessTemplate;
import com.charles.model.process.ProcessType;
import com.oa.process.mapper.ProcessTypeMapper;
import com.oa.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-28-13:33
 */

@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService  {

    @Autowired
    private ProcessTemplateServiceImpl processTemplateService;

    //获取全部ProcessType
    @Override
    public List<ProcessType> findAllProcess() {
        //获取全部ProcessType
        List<ProcessType> processTypes = baseMapper.selectList(null);

        //遍历所有ProcessType
        for(ProcessType processType : processTypes){
            Long processTypeId = processType.getId();

            //封装template id
            LambdaQueryWrapper<ProcessTemplate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ProcessTemplate::getProcessTypeId,processTypeId);
            //查找出对应所有的模板
            List<ProcessTemplate> processTemplateList = processTemplateService.list(lambdaQueryWrapper);

            //封装到对应对象中
            processType.setProcessTemplateList(processTemplateList);

        }
        return processTypes;
    }
}
