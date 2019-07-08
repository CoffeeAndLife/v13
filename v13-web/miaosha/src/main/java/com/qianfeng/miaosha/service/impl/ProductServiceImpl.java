package com.qianfeng.miaosha.service.impl;

import com.qianfeng.miaosha.entity.TProduct;
import com.qianfeng.miaosha.mapper.TProductMapper;
import com.qianfeng.miaosha.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author huangguizhao
 */
@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Override
    @Cacheable(value = "product",key = "#id")
    public TProduct getById(Long id) {
        //如果缓存中有，就用缓存的数据
        //没有，再查数据库
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public boolean sale(Long id) {
        //1.查看库存的信息
        int store = productMapper.selectStoreById(id);
        //2.更新库存
        if (store > 0){
           productMapper.updateStoreById(id);
           return true;
        }
        return false;
    }

    //@Override
    @Transactional
    public boolean sale2(Long id) {
        //1.查看库存的信息
        TProduct product = productMapper.selectStoreById2(id);
        //2.更新库存
        if (product.getStore() > 0){//t1 t2
            //int count = productMapper.updateStoreById2(id,product.getVersion());
            int count = 0;
            //update t_product set store=store-1,version=version+1 where id=#{id,jdbcType=BIGINT} and version=#{old_version}
            if(count > 0){
                return true;
            }else{
                //重试机制，因为可能出现库存还存在，但因为版本号冲突而无法抢购的情况
                //基于时间戳的方式
                //基于固定次数，三次
            }
        }
        return false;
    }
}
