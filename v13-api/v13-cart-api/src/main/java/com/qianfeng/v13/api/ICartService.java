package com.qianfeng.v13.api;

import com.qianfeng.v13.common.pojo.ResultBean;

/**
 * @author huangguizhao
 */
public interface ICartService {

    /**
     *
     * @param uuid  定位购物车的标识
     * @param productId 商品id
     * @param count 购买该商品的数量
     * @return
     */
    public ResultBean add(String uuid,Long productId,Integer count);

    /**
     *
     * @param uuid  定位购物车的标识
     * @param productId 商品id
     * @return
     */
    public ResultBean del(String uuid,Long productId);

    /**
     *
     * @param uuid 定位购物车的标识
     * @param productId 商品id
     * @param count 重置后的购买数量
     * @return
     */
    public ResultBean update(String uuid,Long productId,Integer count);

    /**
     *
     * @param uuid 定位购物车的标识
     * @return
     */
    public ResultBean query(String uuid);
}
