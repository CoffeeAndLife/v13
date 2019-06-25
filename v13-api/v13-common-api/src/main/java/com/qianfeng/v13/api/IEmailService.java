package com.qianfeng.v13.api;

import com.qianfeng.v13.common.pojo.ResultBean;

/**
 * @author huangguizhao
 */
public interface IEmailService {

    public ResultBean send(String to,String subject,String text);
}
