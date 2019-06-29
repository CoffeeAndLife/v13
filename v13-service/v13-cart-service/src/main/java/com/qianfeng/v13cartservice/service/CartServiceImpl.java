package com.qianfeng.v13cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.v13.api.ICartService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.mapper.TProductMapper;
import com.qianfeng.v13.pojo.CartItem;
import com.qianfeng.v13.vo.CartItemVO;
import com.qianfeng.v13cartservice.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Service
public class CartServiceImpl implements ICartService{

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private TProductMapper productMapper;

    @Override
    public ResultBean add(String uuid, Long productId, Integer count){
        //1.构建一个redis的key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //考虑当前的购物车是否已经存在该商品
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        if(cartItem != null){
            //存在该商品
            cartItem.setCount(cartItem.getCount()+count);
            cartItem.setUpdateTime(new Date());
        }else{
            //不存在该商品
            cartItem = new CartItem(productId,count,new Date());
        }
        //3.将对象保存到Redis中
        redisTemplate.opsForHash().put(key,productId.toString(),cartItem);
        //4.刷新有效期
        redisTemplate.expire(key,7, TimeUnit.DAYS);
        //5.返回结果
        return new ResultBean("200","添加到购物车成功！");
    }

    @Override
    public ResultBean del(String uuid, Long productId) {
        //1.构建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //2.查看是否存在该商品
        Long result = redisTemplate.opsForHash().delete(key, productId.toString());
        if(result > 0){
            //刷新整辆购物车的有效期
            redisTemplate.expire(key,7,TimeUnit.DAYS);
            return new ResultBean("200","删除成功");
        }
        return new ResultBean("404","不存在该商品，删除失败");
    }

    @Override
    public ResultBean update(String uuid, Long productId, Integer count) {
        //1.构建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //2.定位该购物项
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        //3.判断
        if (cartItem != null){
            //改变数量
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key,productId.toString(),cartItem);

            //刷新整辆购物车的有效期
            redisTemplate.expire(key,7,TimeUnit.DAYS);

            return new ResultBean("200","购物车商品数量更新成功！");
        }
        return new ResultBean("404","购物车不存在该商品，更新失败！");
    }


    @Override
    public ResultBean query(String uuid) {
        //1.构建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //2.获取购物车的信息
        Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(key);
        //3.排序的问题
        //hash是没有顺序的，但我们要求购物车的信息是有序的
        //存储排序的结果
        TreeSet<CartItemVO> cart = new TreeSet<>();
        //
        Set<Map.Entry<Object, Object>> entries = cartMap.entrySet();
        //第一部分：组装基本数据
        List<Long> ids = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries) {
            CartItem cartItem = (CartItem) entry.getValue();
            //cartItem --> cartItemVO
            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setCount(cartItem.getCount());
            cartItemVO.setUpdateTime(cartItem.getUpdateTime());

            //优化 改为批量查询
            //select * from t_product where id=1
            //select * from t_product where id=N
            cartItemVO.setProduct(productMapper.selectByPrimaryKey(cartItem.getProductId()));

            //查询缓存中是否存在该对象
            //key------------------------value
            //product:productId--------product
            /*String productKey = new StringBuilder("product:").append(cartItem.getProductId()).toString();
            TProduct product = (TProduct) redisTemplate.opsForValue().get(productKey);
            if (product != null){
                cartItemVO.setProduct(product);
            }else{
                //如果不存在，保存需要查询的id
                ids.add(cartItem.getProductId());
            }*/
            //cartItemVo-->cart
            cart.add(cartItemVO);
        }
        //第二部分：根据批量id获取批量的商品信息
        //select * from t_product where id in (1,2,...N)
        //参照我们讲过批量删除的例子 ids
        /*if (!ids.isEmpty()){
            //查询数据库
            List<TProduct> list = null;
            //保存到缓存中
        }*/
        //方案一：如果是一定要查询数据库，那么采用批量的方式减少跟数据库的交互次数
        //方案二：采用缓存来提高效率
        //刷新整辆购物车的有效期
        redisTemplate.expire(key,7,TimeUnit.DAYS);

        return new ResultBean("200",cart);
    }

    private ResultBean getCart(String uuid) {
        //1.构建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //2.获取购物车的信息
        Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(key);
        //3.排序的问题
        //hash是没有顺序的，但我们要求购物车的信息是有序的
        //存储排序的结果
        TreeSet<CartItem> cart = new TreeSet<>(new Comparator<CartItem>() {
            @Override
            public int compare(CartItem o1, CartItem o2) {
                return (int) (o1.getUpdateTime().getTime()-o2.getUpdateTime().getTime());
            }
        });
        //
        Set<Map.Entry<Object, Object>> entries = cartMap.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            CartItem cartItem = (CartItem) entry.getValue();
            cart.add(cartItem);
        }

        //刷新整辆购物车的有效期
        redisTemplate.expire(key,7,TimeUnit.DAYS);

        return new ResultBean("200",cart);
    }
}
