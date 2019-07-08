package com.qianfeng.miaosha.service;

import com.qianfeng.miaosha.entity.TProduct;

/**
 * @author huangguizhao
 */
public interface IProductService {

    public TProduct getById(Long id);

    boolean sale(Long id);
}
