package com.oa.process.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.process.ProcessRecord;
import com.charles.model.system.SysUser;
import com.oa.auth.mapper.SysUserMapper;
import com.oa.process.mapper.ProcessRecordMapper;
import com.oa.process.service.ProcessRecordService;
import com.oa.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charles
 * @create 2023-06-12-21:49
 */

@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ProcessRecordMapper processRecordMapper;

    //记录一条流程操作
    @Override
    public void record(Long processId, Integer status, String description) {
        //根据存储到的id值获取到该用户
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserMapper.selectById(userId);

        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setDescription(description);
        processRecord.setStatus(status);
        processRecord.setOperateUser(sysUser.getName());
        processRecord.setOperateUserId(userId);

        //insert
        processRecordMapper.insert(processRecord);
    }
}
