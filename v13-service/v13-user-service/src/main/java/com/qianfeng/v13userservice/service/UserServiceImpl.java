package com.qianfeng.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.base.BaseServiceImpl;
import com.qianfeng.v13.common.base.IBaseDao;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TUser;
import com.qianfeng.v13.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService{

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

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

    @Override
    public ResultBean checkLogin(TUser user) {
        //业务方法，操作两块的存储层
        //数据库，redis
        //1.查询数据库做判断
        TUser currnetUser = userMapper.selectByUsername(user.getUsername());
        if(currnetUser != null){
            if(currnetUser.getPassword().equals(user.getPassword())){
                //合法账号
                //2.操作Redis保存凭证
                String uuid = UUID.randomUUID().toString();
                String key = new StringBuilder("user:token:").append(uuid).toString();
                //去掉密码信息
                user.setPassword(null);
                redisTemplate.opsForValue().set(key,user);
                //设置有效期
                redisTemplate.expire(key,30, TimeUnit.MINUTES);
                //返回信息
                return new ResultBean("200",uuid);
            }
        }
        //验证不通过
        return new ResultBean("404","");
    }

    @Override
    public ResultBean checkIsLogin(String uuid) {
        //1.组装key
        String key = new StringBuilder("user:token:").append(uuid).toString();
        //2.根据key查询redis
        TUser currentUser = (TUser) redisTemplate.opsForValue().get(key);
        //3.判断
        if (currentUser != null){
            //4.刷新凭证的有效期
            redisTemplate.expire(key,30,TimeUnit.MINUTES);
            return new ResultBean("200",currentUser);
        }
        return new ResultBean("404",null);
    }

    @Override
    public ResultBean logout(String uuid) {
        //1.拼接key
        String key = new StringBuilder("user:token:").append(uuid).toString();
        //2.删除
        Boolean delete = redisTemplate.delete(key);
        if (delete){
           return new ResultBean("200","删除成功！");
        }
        return new ResultBean("404","删除失败！");
    }
}
