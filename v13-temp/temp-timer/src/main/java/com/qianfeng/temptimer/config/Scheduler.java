package com.qianfeng.temptimer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author huangguizhao
 */
@Configuration
public class Scheduler implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(getTaskExecutor());
    }
    @Bean
    public Executor getTaskExecutor(){
        //改成手工创建线程池的方式来创建，参照我们讲的静态页面生成视频
        return Executors.newScheduledThreadPool(100);
    }
}
