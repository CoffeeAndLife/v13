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
    }
}
