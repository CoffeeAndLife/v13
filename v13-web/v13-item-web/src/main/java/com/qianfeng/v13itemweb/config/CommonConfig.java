package com.qianfeng.v13itemweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Configuration
public class CommonConfig {

    @Bean
    public ThreadPoolExecutor initThreadPoolExecutor(){
        //查看当前硬件有多少核
        int cpus = Runtime.getRuntime().availableProcessors();
        //
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                cpus,cpus*2,10, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100));

        //最小线程10 11
        //排队 队列满了之后 11
        //最大线程 100
        //丢弃请求

        return pool;
    }
}
