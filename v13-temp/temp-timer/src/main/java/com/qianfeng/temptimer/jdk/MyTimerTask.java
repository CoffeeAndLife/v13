package com.qianfeng.temptimer.jdk;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author huangguizhao
 */
public class MyTimerTask extends TimerTask{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"->"+new Date());
    }
}
