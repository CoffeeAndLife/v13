package com.qianfeng.miaosha.orderhandler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author huangguizhao
 */
@Component
public class OrderHandler {

    @RabbitListener(queues = "order-queue")
    @RabbitHandler
    public void processMsg(Map data, Channel channel, Message message){
        System.out.println("接收到的消息为："+data);
        System.out.println(data.get("userId"));
        System.out.println(data.get("orderNo"));

        System.out.println("根据这些信息生成订单.....");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("生成订单成功！");
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            //手工确认回复
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
