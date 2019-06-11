package com.qianfeng.v13.api;

import com.github.pagehelper.PageInfo;
import com.qianfeng.v13.common.base.IBaseService;
import com.qianfeng.v13.entity.TProduct;

import java.util.List;

/**
 * @author huangguizhao
 */
public interface IProductService extends IBaseService<TProduct>{
    //单独扩展特殊的方法
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize);
}
