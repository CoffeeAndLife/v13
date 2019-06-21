package com.qianfeng.tempspringbootrabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class RabbitMQConfig {

    //声明队列和交换机
    //声明绑定关系
    @Bean
    public Queue initQueue(){
        return new Queue("springBoot-simple-queue",false,false,false);
    }

}
