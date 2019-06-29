package com.qianfeng.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.base.BaseServiceImpl;
import com.qianfeng.v13.common.base.IBaseDao;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TUser;
import com.qianfeng.v13.mapper.TUserMapper;
import com.qianfeng.v13userservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
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

    @Override
    public ResultBean checkLogin(TUser user) {
        //业务方法，操作两块的存储层
        //数据库，redis
        //1.查询数据库做判断
        TUser currnetUser = userMapper.selectByUsername(user.getUsername());
        if(currnetUser != null){
            if(currnetUser.getPassword().equals(user.getPassword())){
                JwtUtils jwtUtils = new JwtUtils();
                jwtUtils.setSecretKey("java1902");
                jwtUtils.setTtl(30*60*1000);

                String jwtToken = jwtUtils.createJwtToken(currnetUser.getId().toString(), currnetUser.getUsername());

                //将生成的令牌传回给客户端
                return new ResultBean("200",jwtToken);
            }
        }
        //验证不通过
        return new ResultBean("404","");
    }

    @Override
    public ResultBean checkIsLogin(String uuid) {
        try {
            JwtUtils jwtUtils = new JwtUtils();
            jwtUtils.setSecretKey("java1902");
            Claims claims = jwtUtils.parseJwtToken(uuid);
            //TODO 刷新凭证的有效期
            TUser user = new TUser();
            user.setId(Long.parseLong(claims.getId()));
            user.setUsername(claims.getSubject());

            return new ResultBean("200",user);
        }catch (Exception e){
            //当令牌不正确或者过期都会抛出异常
            return new ResultBean("404",null);
        }
    }
}
