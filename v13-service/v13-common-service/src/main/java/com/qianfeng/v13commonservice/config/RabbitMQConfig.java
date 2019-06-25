package com.qianfeng.v13commonservice.config;

import com.qianfeng.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class RabbitMQConfig {

    //声明队列
    @Bean
    public Queue initQueue(){
        return new Queue(RabbitMQConstant.EMAIL_QUEUE,true,false,false);
    }

    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.REGISTER_EXCHANGE);
    }

    //绑定交换机
    @Bean
    public Binding initBinding(Queue initQueue,TopicExchange initExchange){
        return BindingBuilder.bind(initQueue).to(initExchange).with("user.add");
    }
}
