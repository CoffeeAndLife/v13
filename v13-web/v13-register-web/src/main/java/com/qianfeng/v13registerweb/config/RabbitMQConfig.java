package com.qianfeng.v13registerweb.config;

import com.qianfeng.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.REGISTER_EXCHANGE);
    }
}
