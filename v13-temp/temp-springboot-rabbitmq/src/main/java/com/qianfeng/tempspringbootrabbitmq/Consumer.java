package com.qianfeng.tempspringbootrabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huangguizhao
 */
@RabbitListener(queues = "springBoot-simple-queue")
@Component
public class Consumer {

    @RabbitHandler
    public void process(Book book){
        System.out.println("----->"+book.getName());
    }


}
