package com.qianfeng.thread;

import java.util.Random;

/**
 * @author huangguizhao
 */
public class ThreadLocalTest {

    /**
     * 为每个线程都会创建一个ThreadLocalMap
     * 当我们要放入数据的时候，以threadLocal为key，存放的值为value，放到属于这个线程的map中
     *
     * 每个线程的Map是互相独立的
     *
     * 获取的时候，通过当前线程获取到属于它的ThreadLocalMap，再通过这个map去获取里面的value
     */
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args){
        new Thread(new MyTask()).start();
        new Thread(new MyTask()).start();
    }

    static class MyTask implements Runnable{
        @Override
        public void run() {
            threadLocal.set((int) (Math.random()*100));
            System.out.println(Thread.currentThread().getName()+"->"+threadLocal.get());
        }
    }
}


