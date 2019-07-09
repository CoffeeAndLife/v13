package com.qianfeng.miaosha.scheduling;

import com.qianfeng.miaosha.entity.TSeckill;
import com.qianfeng.miaosha.mapper.TSeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huangguizhao
 */
@Component
public class SeckillTask {

    @Autowired
    private TSeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //1.定时扫描，开启秒杀活动
    @Scheduled(cron = "0/10 * * * * *")
    public void startSeckill(){
        //System.out.println("开始扫描秒杀活动表，根据时间开启符合条件的秒杀活动");
        //1.查询秒杀表
        List<TSeckill> seckills = seckillMapper.getCanStartSeckill();
        //2.获取到符合条件的记录(已到点，但未开启的秒杀活动)
        if(seckills != null && !seckills.isEmpty()){
            //3.修改这些记录的状态，标记为已开启
            for (TSeckill seckill : seckills) {
                seckill.setStatus("1");
                seckillMapper.updateByPrimaryKeySelective(seckill);
                //System.out.println("秒杀活动："+seckill.getId()+"-》已经开启！");
                //创建一个待秒杀的商品队列
                String key = new StringBuilder("seckill:product:").append(seckill.getId()).toString();
                //确定队列的长度
                Integer count = seckill.getCount();
                for (Integer i = 0; i < count; i++) {
                    redisTemplate.opsForList().leftPush(key,seckill.getProductId());
                }
                //System.out.println("redis秒杀队列准备就绪！");
                //seckill:product:2 --------  list(1,1,1,1,1)

                //保存秒杀活动的信息到缓存中，避免后续的时候反复查询数据库
                String seckillKey = new StringBuilder("seckill:").append(seckill.getId()).toString();
                redisTemplate.opsForValue().set(seckillKey,seckill);
            }
        }
    }


    //2.定时扫描，关闭秒杀活动
    @Scheduled(cron = "0/10 * * * * *")
    public void stopSeckill(){
        //System.out.println("开始扫描秒杀活动表，根据时间关闭符合条件的秒杀活动");
        //1.查询秒杀表
        List<TSeckill> seckills = seckillMapper.getCanStopSeckill();
        //2.获取到符合条件的记录(已到点，但未结束的秒杀活动)
        if(seckills != null && !seckills.isEmpty()){
            //3.修改这些记录的状态，标记为已结束
            for (TSeckill seckill : seckills) {
                seckill.setStatus("2");
                seckillMapper.updateByPrimaryKeySelective(seckill);
                System.out.println("秒杀活动："+seckill.getId()+"->已经关闭！");

                //清除掉Redis中保存的秒杀队列
                String key = new StringBuilder("seckill:product:").append(seckill.getId()).toString();
                redisTemplate.delete(key);
                System.out.println("清除掉秒杀队列！");
            }
        }
    }
}
