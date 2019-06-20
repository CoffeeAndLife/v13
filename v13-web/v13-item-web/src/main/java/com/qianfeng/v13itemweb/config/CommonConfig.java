package com.qianfeng.v13itemweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        return pool;
    }
}
