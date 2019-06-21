package com.qianfeng.tempspringbootrabbitmq;

import java.io.Serializable;

/**
 * @author huangguizhao
 */
public class Book implements Serializable{

    private Integer id;
    private String name;
    private Integer price;

    public Book(Integer id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
