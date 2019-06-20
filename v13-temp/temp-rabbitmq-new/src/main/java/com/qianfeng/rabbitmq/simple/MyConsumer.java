package com.qianfeng.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 */
public class MyConsumer {

    private static String queue_name = "simple_queue";

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
        //3.消息者监听队列，获取消息
        Consumer consumer = new DefaultConsumer(channel){
            //等着有消息回调该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("接收到消息："+msg);
            }
        };
        //4.消息者需要来监听队列
        //autoAck：自动回复
        channel.basicConsume(queue_name,true,consumer);
    }
}
