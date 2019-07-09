package com.qianfeng.miaosha.exception;

/**
 * @author huangguizhao
 * 自定义异常用于描述秒杀过程的业务问题
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String msg){
        super(msg);
    }
}
