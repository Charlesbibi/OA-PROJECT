package com.oa.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.process.Process;
import com.charles.vo.process.ApprovalVo;
import com.charles.vo.process.ProcessFormVo;
import com.charles.vo.process.ProcessQueryVo;
import com.charles.vo.process.ProcessVo;

import java.util.Map;

/**
 * @author Charles
 * @create 2023-06-07-0:31
 */

public interface ProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> myPage, ProcessQueryVo processQueryVo);

    void publishProcess(String fileName);

    void start(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
