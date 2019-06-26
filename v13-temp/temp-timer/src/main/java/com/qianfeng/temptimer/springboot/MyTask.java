package com.qianfeng.temptimer.springboot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author huangguizhao
 */
@Component
public class MyTask {

    //cron通用性的表达式，用于描述定时任务执行的规律
    @Scheduled(cron = "* * * * * ?")
    public void doSomething(){
        System.out.println(Thread.currentThread().getName()+"->"+new Date());
    }
}
