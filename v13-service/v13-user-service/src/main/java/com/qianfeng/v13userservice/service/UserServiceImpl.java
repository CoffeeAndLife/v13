package com.qianfeng.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.base.BaseServiceImpl;
import com.qianfeng.v13.common.base.IBaseDao;
import com.qianfeng.v13.entity.TUser;
import com.qianfeng.v13.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author huangguizhao
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService{

    @Autowired
    private TUserMapper userMapper;

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }

    @Override
    public int insertSelective(TUser record) {
        super.insertSelective(record);
        //返回主键的信息
        return record.getId().intValue();
    }
}
