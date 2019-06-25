package com.qianfeng.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
public class JedisTest {

    @Test
    public void stringTest(){
        Jedis jedis = new Jedis("192.168.142.134",6379);
        //设置密码
        jedis.auth("java1902");
        //
        /*jedis.set("target","javacaozuoredis");
        String target = jedis.get("target");
        System.out.println(target);*/

        jedis.mset("k1","v1","k2","v2");
        List<String> values = jedis.mget("k1", "k2");
        for (String value : values) {
            System.out.println(value);
        }
    }

    @Test
    public void otherTest(){
        Jedis jedis = new Jedis("192.168.142.134",6379);
        //设置密码
        jedis.auth("java1902");
        //
        Map<String, String> map = new HashMap<>();
        map.put("name","愚昧之巅");
        map.put("price","9999");
        jedis.hmset("book:1",map);

        Map<String, String> resultMap = jedis.hgetAll("book:1");
        Set<Map.Entry<String, String>> entries = resultMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}
