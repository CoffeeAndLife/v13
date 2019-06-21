package com.qianfeng.rabbitmq.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 */
public class Producer {

    private static String queue_name = "work_queue";

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
        //3.声明队列
        channel.queueDeclare(queue_name,false,false,false,null);
        //4.将消息发送到队列
        for (int i = 1; i <= 10; i++) {
            String message = "这是第"+i+"次说，我爱你Java";
            channel.basicPublish("",queue_name,null,message.getBytes());
        }
        System.out.println("发送消息成功！");
    }
}
