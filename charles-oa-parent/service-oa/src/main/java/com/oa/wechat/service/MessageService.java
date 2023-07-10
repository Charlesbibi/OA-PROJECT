package com.oa.wechat.service;

/**
 * @author Charles
 * @create 2023-06-19-0:07
 */
public interface MessageService {
    //推送审批人员
    void pushPendingMessage(Long processId, Long userId, String taskId);

    //审批后 推送给发出审批的人
    void pushProcessMessage(Long processId, Long userId, String taskId);
}
