package com.qianfeng.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.v13.api.IProductService;
import com.qianfeng.v13.common.base.BaseServiceImpl;
import com.qianfeng.v13.common.base.IBaseDao;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.mapper.TProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageIndex,pageSize);
        //2.获取数据
        List<TProduct> list = list();
        //3.构建一个分页对象
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list,2);
        return pageInfo;
    }
}
