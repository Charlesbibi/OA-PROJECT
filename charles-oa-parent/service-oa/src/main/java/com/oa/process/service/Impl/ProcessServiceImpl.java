package com.oa.process.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.process.Process;
import com.charles.model.process.ProcessRecord;
import com.charles.model.process.ProcessTemplate;
import com.charles.model.system.SysUser;
import com.charles.vo.process.ApprovalVo;
import com.charles.vo.process.ProcessFormVo;
import com.charles.vo.process.ProcessQueryVo;
import com.charles.vo.process.ProcessVo;
import com.oa.auth.service.SysUserService;
import com.oa.auth.service.impl.SysUserServiceImpl;
import com.oa.process.mapper.ProcessMapper;
import com.oa.security.custom.LoginUserInfoHelper;
import com.oa.wechat.service.MessageService;
import com.oa.wechat.service.impl.MessageServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import com.oa.process.service.ProcessService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author Charles
 * @create 2023-06-07-0:31
 */

@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private ProcessTemplateServiceImpl processTemplateService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessRecordServiceImpl processRecordService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private MessageServiceImpl messageService;

    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> myPage, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> page = processMapper.selectPage(myPage, processQueryVo);
        return page;
    }

    //发步模板
    @Override
    public void publishProcess(String fileName) {
        //获取输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        //封装为zip
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //部署流程
        Deployment deploy = repositoryService.createDeployment().
                addZipInputStream(zipInputStream).
                deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //启动流程实例
    @Override
    @Transactional
    public void start(ProcessFormVo processFormVo) {
        //从ThreadLocal中取出保存的用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        //根据processTemplateId获取到模板
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());
        Process process = new Process();
        //复制到新对象中
        BeanUtils.copyProperties(processFormVo, process);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        process.setStatus(1);
        //更新oa-process表
        processMapper.insert(process);

        //绑定业务id
        String businessKey = String.valueOf(process.getId());
        //流程参数
        Map<String, Object> variables = new HashMap<>();
        //将表单数据放入流程实例中
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>();
        //循环转换
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        variables.put("data", map);
        //启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processTemplate.getProcessDefinitionKey(), businessKey, variables);

        //业务表关联当前流程实例id
        String processInstanceId = processInstance.getId();
        process.setProcessInstanceId(processInstanceId);

        //获取下一个推送人
        List<Task> currentTaskList = this.getCurrentTaskList(processInstanceId);
        if(!CollectionUtils.isEmpty(currentTaskList)){
            List<String> assignList = new ArrayList<>();
            for(Task task : currentTaskList){
                SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, task.getAssignee()));
                assignList.add(user.getName()); //真实姓名

                //微信公众号推送
                messageService.pushPendingMessage(process.getId(),user.getId(),task.getId());
            }

            process.setDescription("等待 " + StringUtils.join(assignList,",") + " 审批");
        }
        processMapper.updateById(process);

        //同步到process-record中
        processRecordService.record(process.getId(),process.getStatus(),"发起申请");
    }

    @Override
    public IPage<ProcessVo> findPending(Page<Process> pageParam) {
        //根据id查询
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime() //根据创建时间降序排列
                .desc();

        List<Task> list = query.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()),
                                                    (int) pageParam.getSize());
        long totalCount = query.count();

        List<ProcessVo> processList = new ArrayList<>();
        // 根据流程的业务ID查询实体并关联
        for(Task item : list){
            String processInstanceId = item.getProcessInstanceId();

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                continue;
            }
            // 业务key
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            //获取到当前process
            Process process = this.getById(Long.parseLong(businessKey));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId(item.getId());

            processList.add(processVo);
        }

        IPage<ProcessVo> page = new Page<ProcessVo>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        page.setRecords(processList);
        return page;
    }

    //展示详情信息
    @Override
    public Map<String, Object> show(Long id) {
        //获取对应的process
        Process process = processMapper.selectById(id);

        //获取对应的审批记录
        LambdaQueryWrapper<ProcessRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProcessRecord::getProcessId,process.getId());
        List<ProcessRecord> processRecordList = processRecordService.list(lambdaQueryWrapper);
        //获取模板数据
        ProcessTemplate template = processTemplateService.getById(process.getProcessTemplateId());
        //存放数据
        Map<String,Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", template);

        //计算当前用户是否可以审批，能够查看详情的用户不是都能审批，审批后也不能重复审批
        Boolean isApprove = false;
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtils.isEmpty(currentTaskList)){
            for(Task task : currentTaskList){
                String username = LoginUserInfoHelper.getUsername();
                if(task.getAssignee().equals(username))
                    isApprove = true;
            }
        }

        map.put("isApprove",isApprove);
        return map;
    }

    //审批
    @Override
    public void approve(ApprovalVo approvalVo){
        Map<String, Object> variables1 = taskService.getVariables(approvalVo.getTaskId());

        String taskId = approvalVo.getTaskId();
        if(approvalVo.getStatus() == 1){
            //审批通过 1
            Map<String, Object> variables = new HashMap<String, Object>();
            taskService.complete(taskId, variables);
        }else{
            //审批驳回 -1
            this.endTask(taskId);
        }

        String description = approvalVo.getStatus() == 1 ? "已通过" : "驳回";
        //记录到record中
        processRecordService.record(approvalVo.getProcessId(),approvalVo.getStatus(),description);

        //获取下一个审批人
        Process process = this.getById(approvalVo.getProcessId());
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if(CollectionUtils.isEmpty(currentTaskList)){
            //没有下一个任务人了
            if(approvalVo.getStatus() == 1){
                process.setStatus(2);
                process.setDescription("审批完成（同意）");
            }else{
                process.setStatus(-1);
                process.setDescription("审批完成（拒绝）");
            }
        }else{
            //得到下一个任务人
            List<String> assigneeList = new ArrayList<>(); //可能有多个审批人
            for(Task task : currentTaskList){
                SysUser sysUser = sysUserService.getBaseMapper().getUserByUsername(task.getAssignee());
                assigneeList.add(sysUser.getName());

                //推送消息给下一个审批人
                messageService.pushPendingMessage(process.getId(),sysUser.getId(),task.getId());
            }
            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
            process.setStatus(1);
        }

        //更新当前流程
        this.updateById(process);

        //向提出申请的人推送消息
        messageService.pushProcessMessage(process.getId(),process.getUserId(),process.getProcessInstanceId());
    }

    //获取所有处理过已完成任务
    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        //获取历史记录query
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

        //获取process
        int current = (int)((pageParam.getCurrent()-1)* pageParam.getSize());
        int size = (int)pageParam.getSize();
        List<HistoricTaskInstance> historicTaskInstances = query.listPage(current, size);
        long totalCount = query.count();

        List<ProcessVo> processVoList = new ArrayList<>();
        for(HistoricTaskInstance historicTaskInstance : historicTaskInstances){
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            Process process = this.getOne(new LambdaQueryWrapper<Process>().eq(Process::getProcessInstanceId, processInstanceId));

            //将process封装为processVo
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId("0");
            processVoList.add(processVo);
        }

        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(),pageParam.getSize(),totalCount);
        page.setRecords(processVoList);

        return page;
    }

    //查找所有发起的任务
    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> page = processMapper.selectPage(pageParam, processQueryVo);
        for(ProcessVo item : page.getRecords()){
            item.setTaskId("0");
        }

        return page;
    }

    //结束当前task
    private void endTask(String taskId) {
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        // 并行任务可能为null
        if(CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());
    }

    //获取当前所有的任务列表，可能是集合（并行审批）
    private List<Task> getCurrentTaskList(String processInstanceId) {
        List<Task> taskQuery = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return taskQuery;
    }

}
