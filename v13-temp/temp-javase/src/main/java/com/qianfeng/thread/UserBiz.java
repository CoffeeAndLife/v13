package com.qianfeng.thread;

/**
 * @author huangguizhao
 */
public class UserBiz {

    private Dao1 dao1 = new Dao1();
    private Dao2 dao2 = new Dao2();

    public void add(){
        dao1.add();
        dao2.add();
    }
}
