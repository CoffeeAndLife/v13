package com.qianfeng.v13searchweb.config;

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
        return new Queue(RabbitMQConstant.PRODUCT_SEARCH_QUEUE,true,false,false);
    }

    //还需要处理删除的逻辑
    //声明一个队列，来接收删除的消息

    //声明交换机，如果交换机已经存在，则不需要做操作
    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE);
    }

    //绑定交换机
    @Bean
    public Binding initBinding(Queue initQueue,TopicExchange initExchange){
        return BindingBuilder.bind(initQueue).to(initExchange).with("product.add");
    }

    //交换机只有一个，绑定的关系，多个队列绑定一个交换机，只是这个routingkey不同
}
