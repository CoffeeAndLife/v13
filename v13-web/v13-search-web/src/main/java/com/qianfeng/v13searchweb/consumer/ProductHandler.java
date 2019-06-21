package com.qianfeng.v13searchweb.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.ISearchService;
import com.qianfeng.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huangguizhao
 * 针对平台系统商品业务发送的消息做处理的消费端
 */
@Component
public class ProductHandler {

    @Reference
    private ISearchService searchService;

    //这个只是处理了添加的消息
    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.PRODUCT_SEARCH_QUEUE)
    public void processAdd(Long id){
        System.out.println(id);
        searchService.synDataById(id);
    }

    //处理删除的消息
    public void processDel(Long id){
        //searchService.delById(id);
    }
}
