package com.oa.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.process.ProcessTemplate;

/**
 * @author Charles
 * @create 2023-05-28-14:18
 */
public interface ProcessTemplateService extends IService<ProcessTemplate> {
    Page<ProcessTemplate> wrapperWithTemplateName(Page<ProcessTemplate> myPage);

    Boolean publish(Long id);
}
