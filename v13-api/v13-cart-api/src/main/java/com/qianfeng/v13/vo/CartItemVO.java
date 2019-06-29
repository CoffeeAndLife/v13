package com.qianfeng.v13.vo;

import com.qianfeng.v13.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangguizhao
 */
public class CartItemVO implements Serializable,Comparable<CartItemVO>{

    private TProduct product;

    private Integer count;

    private Date updateTime;

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public CartItemVO(TProduct product, Integer count, Date updateTime) {
        this.product = product;
        this.count = count;
        this.updateTime = updateTime;
    }

    public CartItemVO() {
    }

    @Override
    public int compareTo(CartItemVO o) {
        return (int) (o.getUpdateTime().getTime()-this.getUpdateTime().getTime());
    }
}
