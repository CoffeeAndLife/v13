package com.qianfeng.classloader;

/**
 * @author huangguizhao
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.String");
        System.out.println(clazz.getClassLoader());//null
    }
}
