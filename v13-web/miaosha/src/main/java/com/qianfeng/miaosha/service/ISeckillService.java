package com.qianfeng.miaosha.service;

/**
 * @author huangguizhao
 */
public interface ISeckillService {

    /**
     *
     * @param seckillId 参与的秒杀活动
     * @param userId 当前登录的用户
     */
    public void sale(Long seckillId,Long userId);

    String sendOrderMsg(Long seckillId, Long userId);
}
