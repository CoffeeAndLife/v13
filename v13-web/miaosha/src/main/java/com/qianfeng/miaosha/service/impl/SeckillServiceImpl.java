package com.qianfeng.miaosha.service.impl;

import com.qianfeng.miaosha.entity.TSeckill;
import com.qianfeng.miaosha.exception.SeckillException;
import com.qianfeng.miaosha.mapper.TSeckillMapper;
import com.qianfeng.miaosha.service.ISeckillService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangguizhao
 */
@Service
public class SeckillServiceImpl implements ISeckillService{

    @Autowired
    private TSeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sale(Long seckillId, Long userId) {
        //1.查看当前秒杀活动的状态
        /*TSeckill seckill = seckillMapper.selectByPrimaryKey(seckillId);
        if("2".equals(seckill.getStatus())){
            //2.抛出自定义异常
            throw new SeckillException("当前活动已结束，下次记得早点来哦！");
        }
        if("0".equals(seckill.getStatus())){
            throw new SeckillException("当前活动还未开启，不要太心急哦！");
        }*/

        //数据库查询 --》 查询缓存
        String seckillKey = new StringBuilder("seckill:").append(seckillId).toString();
        TSeckill seckill = (TSeckill) redisTemplate.opsForValue().get(seckillKey);
        if("2".equals(seckill.getStatus())){
            //2.抛出自定义异常
            throw new SeckillException("当前活动已结束，下次记得早点来哦！");
        }
        if("0".equals(seckill.getStatus())){
            throw new SeckillException("当前活动还未开启，不要太心急哦！");
        }

        //说明活动已经开启，参与秒杀
        //3，从秒杀队列中获取商品信息
        String key = new StringBuilder("seckill:product:").append(seckill.getId()).toString();
        Long productId = (Long) redisTemplate.opsForList().leftPop(key);
        if(productId == null){
            throw new SeckillException("很遗憾，商品已经被抢购一空，下次记得早点来！");
        }

        //4.保存当前抢购成功的用户信息
        //当前用户是否已经抢购过一件
        String luckManKey = new StringBuilder("seckill:user:").append(seckill.getId()).toString();
        //
        Boolean isExists = redisTemplate.opsForSet().isMember(luckManKey, userId);
        if (isExists){
            //将这个机会放回原位
            redisTemplate.opsForList().leftPush(key,productId);
           throw new SeckillException("您已经抢购成功了，把机会留给其他小伙伴吧！");
        }

        redisTemplate.opsForSet().add(luckManKey,userId);
        System.out.println(userId+":用户抢购成功！");

    }

    @Override
    public String sendOrderMsg(Long seckillId, Long userId) {
        /**
         * 用户ID
         商品ID
         秒杀ID
         购买数量：1件
         商品秒杀价格：1元
         订单编号：
         */
        //1.生成订单编号，采用redis提供incr来生成
        Long orderno = redisTemplate.opsForValue().increment("orderno");
        String orderNo = orderno.toString();

        //2.获取秒杀活动的信息
        String seckillKey = new StringBuilder("seckill:").append(seckillId).toString();
        TSeckill seckill = (TSeckill) redisTemplate.opsForValue().get(seckillKey);

        //3.组合最终给交换机发送的消息
        Map<String,Object> data = new HashMap<>();
        data.put("userId",userId);
        data.put("productId",seckill.getProductId());
        data.put("seckillId",seckill.getId());
        data.put("count",1);
        data.put("price",seckill.getSalePrice());
        data.put("orderNo",orderNo);

        //4.发送到交换机
        rabbitTemplate.convertAndSend("seckill-exchange","",data);

        //5.返回订单编号
        return orderNo;
    }
}
