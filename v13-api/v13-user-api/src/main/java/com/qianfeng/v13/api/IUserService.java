package com.qianfeng.v13.api;

import com.qianfeng.v13.common.base.IBaseService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TUser;

/**
 * @author huangguizhao
 */
public interface IUserService extends IBaseService<TUser> {

    /**
     *
     * @param user
     * @return  resultBean里面的data，保存在redis中的凭证key（uuid）
     */
    public ResultBean checkLogin(TUser user);

    /**
     *
     * @param uuid 保存在客户端的凭证信息
     * @return
     */
    public ResultBean checkIsLogin(String uuid);

}
