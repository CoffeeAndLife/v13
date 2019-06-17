package com.qianfeng.v13.common.pojo;

import java.io.Serializable;

/**
 * @author huangguizhao
 */
public class ResultBean<T> implements Serializable{

    private String statusCode;
    private T data;

    public ResultBean() {
    }

    public ResultBean(String statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
