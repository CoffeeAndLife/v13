package com.qianfeng.jedis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest(){
        //在程序里面做单独的特殊设置
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        //redisTemplate 默认对保存进去的信息，都会做序列化操作
        //key -> target
        redisTemplate.opsForValue().set("dream","拥有美好的爱情和成功的事业");
        System.out.println(redisTemplate.opsForValue().get("dream"));
    }

    @Test
    public void incrTest(){
        //在做自增的时候，默认的JDK序列化方式不支持运算操作，所以要改为字符串的方式
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set("money","1000");
        redisTemplate.opsForValue().increment("money",1000);
        Object money = redisTemplate.opsForValue().get("money");
        System.out.println(money);
    }

    @Test
    public void otherTest(){
        //hash的key也是序列化方式的
        redisTemplate.opsForHash().put("book:1","name","开一间咖啡屋");

        redisTemplate.opsForList().leftPushAll("target:list","1","2","4","8");
        List rangList = redisTemplate.opsForList().range("target:list", 0, -1);
        for (Object o : rangList) {
            System.out.println(o);
        }
    }

    @Test
    public void noBatchTest(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForValue().set("k"+i,"v"+i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);//2239
    }

    @Test
    public void batchTest(){
        long start = System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 10000; i < 20000; i++) {
                    operations.opsForValue().set("k"+i,"v"+i);
                }
                return null;
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end-start);//2239  243
    }

    @Test
    public void ttlTest(){
        redisTemplate.opsForValue().set("import","gaokao");
        redisTemplate.expire("import",60, TimeUnit.SECONDS);
        Long expire = redisTemplate.getExpire("import", TimeUnit.SECONDS);
        System.out.println(expire);
    }
}
