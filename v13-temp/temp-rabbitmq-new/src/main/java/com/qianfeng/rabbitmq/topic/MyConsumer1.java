package com.qianfeng.rabbitmq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 */
public class MyConsumer1 {

    private static String exchange_name = "topic_exchange";
    private static String queue_name = "topic_exchange_queue1";

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

        //起到限流作用。每次只接受一个消息
        channel.basicQos(1);
        //3.消息者监听队列，获取消息
        Consumer consumer = new DefaultConsumer(channel){
            //等着有消息回调该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("消费者1接收到消息："+msg);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //处理结束之后，回复结果
                //multiple: fasle 是否批量确认
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //4.消息者需要来监听队列
        //autoAck：自动回复 ---》 手工回复
        //绑定队列跟交换机
        channel.queueBind(queue_name,exchange_name,"nba.*");
        channel.queueBind(queue_name,exchange_name,"cba.*");

        channel.basicConsume(queue_name,false,consumer);
    }
}
