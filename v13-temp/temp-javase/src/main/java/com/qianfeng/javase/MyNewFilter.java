package com.qianfeng.javase;

/**
 * @author huangguizhao
 */
public class MyNewFilter implements NewFilter{

    @Override
    public void pre() {
        System.out.println("pre....");
    }
}
