package com.qianfeng.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 */
public class Producer {

    private static String exchange_name = "topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接对象
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.142.134");
        factory.setPort(5672);
        factory.setVirtualHost("/java1902");
        factory.setUsername("java1902");
        factory.setPassword("123");

        Connection connection = factory.newConnection();
        //2.创建本次交互的通道
        Channel channel = connection.createChannel();
        //3.声明交换机
        channel.exchangeDeclare(exchange_name,"topic");
        //4.将消息发送到交换机
        channel.basicPublish(exchange_name,"nba.news",null,"猛龙总冠军".getBytes());
        channel.basicPublish(exchange_name,"cba.news.man",null,"广东9冠王".getBytes());

        System.out.println("发送消息成功！");
    }
}
