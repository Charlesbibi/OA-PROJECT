package com.oa.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.process.ProcessRecord;

/**
 * @author Charles
 * @create 2023-06-12-21:49
 */
public interface ProcessRecordService extends IService<ProcessRecord> {

    //添加一个流程记录
    void record(Long processId, Integer status, String description);
}
