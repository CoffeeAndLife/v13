package com.qianfeng.miaosha.mapper;

import com.qianfeng.miaosha.entity.TProduct;

public interface TProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TProduct record);

    int insertSelective(TProduct record);

    TProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TProduct record);

    int updateByPrimaryKey(TProduct record);

    int selectStoreById(Long id);

    void updateStoreById(Long id);

    TProduct selectStoreById2(Long id);
}