package com.qianfeng.temptimer.jdk;

import java.util.Date;
import java.util.Timer;

/**
 * @author huangguizhao
 */
public class Main {
    public static void main(String[] args){
        Timer timer = new Timer();
        MyTimerTask task = new MyTimerTask();
        System.out.println("main:"+new Date());
        //timer.schedule(task,1000);

        timer.schedule(task,1000,1000);

        //每个月1号8:00发工资。周期性不固定的这种需求，很难搞定
    }
}
