package com.oa.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.process.ProcessType;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-28-13:32
 */
public interface ProcessTypeService extends IService<ProcessType> {
    List<ProcessType> findAllProcess();
}
