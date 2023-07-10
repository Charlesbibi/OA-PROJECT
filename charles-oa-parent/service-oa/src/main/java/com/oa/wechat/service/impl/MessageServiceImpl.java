package com.oa.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.charles.model.process.Process;
import com.charles.model.process.ProcessTemplate;
import com.charles.model.system.SysUser;
import com.oa.auth.service.impl.SysUserServiceImpl;
import com.oa.process.service.Impl.ProcessServiceImpl;
import com.oa.process.service.Impl.ProcessTemplateServiceImpl;
import com.oa.process.service.ProcessService;
import com.oa.process.service.ProcessTemplateService;
import com.oa.wechat.service.MessageService;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Charles
 * @create 2023-06-19-0:10
 */

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ProcessServiceImpl processService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private ProcessTemplateServiceImpl processTemplateService;

    @Autowired
    private WxMpService wxMpService;

    //推送审批人员
    @SneakyThrows
    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {
        //获取对应process
        Process process = processService.getById(processId);
        //获取要推送给的用户
        SysUser sysUser = sysUserService.getById(userId);
        //和区域当前任务模板
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        //获取发送请求的用户
        SysUser submitSysUser = sysUserService.getById(process.getUserId());


        //生成微信消息模板
        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
                .toUser(sysUser.getOpenId()) //微信号唯一标识
                .templateId("Pw0t_AnN5oPvAFqU7tDsga5Srtbs9q3vlTcgE0vev0w") //在微信定义消息的模板
                .url("http://charlesoa.free.idcfengye.com/#/show/" + processId + "/" + taskId) //点击模板消息要访问的网址
                .build();

        StringBuffer content = getBuffer(process.getFormValues());
        System.out.println("content " + content);

        wxMpTemplateMessage.addData(new WxMpTemplateData("process", submitSysUser.getName() + "提交了" + processTemplate.getName() + "审批申请，请注意查看。", ""));
        wxMpTemplateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), ""));
        wxMpTemplateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#E47833"));
        wxMpTemplateMessage.addData(new WxMpTemplateData("content", content.toString(), "#E47833"));

        //发送提示
        wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
    }

    //审批后 推送给发出审批的人
    @SneakyThrows
    @Override
    public void pushProcessMessage(Long processId, Long userId, String taskId) {
        //获取对应process
        Process process = processService.getById(processId);
        //获取要推送给的用户
        SysUser currentSysUser = sysUserService.getById(userId);
        //和区域当前任务模板
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        //生成微信消息模板
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .templateId("CZlN8YEzmoMH3Bb22RJotrEo-6gamSkYSJOfeZTaKnE")
                .toUser(currentSysUser.getOpenId())
                .url("http://charlesoa.free.idcfengye.com/#/show/" + processId + "/" + taskId)
                .build();

        StringBuffer content = getBuffer(process.getFormValues());

        templateMessage.addData(new WxMpTemplateData("first", "你发起的"+processTemplate.getName()+"审批申请已经被处理了，请注意查看。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", currentSysUser.getName(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword4", process.getStatus() == 1 ? "审批通过" : "审批拒绝", process.getStatus() == 1 ? "#009966" : "#FF0033"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
    }

    //通过输入流转换JSON数据为指定格式
    private StringBuffer getBuffer(String formValues){
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }

        return content;
    }
}
