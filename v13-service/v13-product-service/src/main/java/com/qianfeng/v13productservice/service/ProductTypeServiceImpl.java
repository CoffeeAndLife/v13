package com.qianfeng.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.v13.api.IProductTypeService;
import com.qianfeng.v13.common.base.BaseServiceImpl;
import com.qianfeng.v13.common.base.IBaseDao;
import com.qianfeng.v13.entity.TProductType;
import com.qianfeng.v13.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author huangguizhao
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService{

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }
}
