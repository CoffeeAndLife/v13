package com.qianfeng.v13commonservice.consumer;

import com.qianfeng.v13.api.IEmailService;
import com.qianfeng.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.util.Map;

/**
 * @author huangguizhao
 */
@Component
public class Consumer {

    @Autowired
    private IEmailService emailService;

    @RabbitListener(queues = RabbitMQConstant.EMAIL_QUEUE)
    @RabbitHandler
    public void process(Map<String,String> map){
        System.out.println(map.get("to"));
        System.out.println(map.get("subject"));
        System.out.println(map.get("text"));

        emailService.send(map.get("to"),map.get("subject"),map.get("text"));
        //卡死了
        //dcba

        //理性分析
        //方案
        //123@123.com
        //加异常处理（处理）
        //让程序正常结束--消息消化掉 + 记录日志（发送失败的日志）
        //日志表(id,to,subject,text,create_time,replay_times(3),error_message)

        //定时任务，扫描邮件日志表（多线程处理批量发送邮件）
        //发送短信，告知系统管理员（告警信息）
    }
}
