package com.qianfeng.javase;

import java.io.FileInputStream;

/**
 * @author huangguizhao
 */
public class MyFilter extends FilterAdapter{

    @Override
    public void pre() {
        System.out.println("pre....");
    }
}
