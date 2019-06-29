package com.qianfeng.v13cartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.ICartService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    //
    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean add(@PathVariable("productId") Long productId,
                          @PathVariable("count") Integer count,
                          @CookieValue(name = "user_cart",required = false) String uuid,
                          HttpServletRequest request,
                          HttpServletResponse response){

        TUser currentUser = (TUser) request.getAttribute("user");
        if(currentUser != null){
            //用户属于已登录状态
            uuid = currentUser.getId().toString();
            //2.将商品添加到购物车
            ResultBean resultBean = cartService.add(uuid, productId, count);

            return resultBean;
        }
        //1.判断当前购物车是否存在
        if(uuid == null || "".equals(uuid)){
            uuid = UUID.randomUUID().toString();
        }

        ResultBean resultBean = cartService.add(uuid, productId, count);

        //3.判断
        if("200".equals(resultBean.getStatusCode())){
            //4.写cookie到客户端
            reflushCookie(uuid, response);
        }
        return resultBean;
    }


    @RequestMapping("query")
    @ResponseBody
    public ResultBean query(@CookieValue(name = "user_cart",required = false) String uuid,
                            HttpServletRequest request,
                            HttpServletResponse response){
        TUser currentUser = (TUser) request.getAttribute("user");
        if(currentUser != null){
            //用户属于已登录状态
            uuid = currentUser.getId().toString();
            //2.查询购物车信息
            ResultBean resultBean = cartService.query(uuid);

            return resultBean;
        }

        //1.判断
        if (uuid == null || "".equals(uuid)){
            return new ResultBean("404","购物车为空");
        }

        //2.查询购物车信息
        ResultBean resultBean = cartService.query(uuid);

        //3.判断
        if("200".equals(resultBean.getStatusCode())){
            reflushCookie(uuid, response);
        }
        //遍历 集合 对象 （名称，价格，图片，数量）
        //把信息配齐
        return resultBean;
    }


    @RequestMapping("del/{productId}")
    @ResponseBody
    public ResultBean del(@CookieValue(name = "user_cart",required = false) String uuid,
                          @PathVariable("productId") Long productId,
                          HttpServletResponse response){
        //1.判断
        if (uuid == null || "".equals(uuid)){
            return new ResultBean("404","购物车为空");
        }
        //2.执行删除的操作
        ResultBean resultBean = cartService.del(uuid, productId);
        //3.
        //3.判断
        if("200".equals(resultBean.getStatusCode())){
            reflushCookie(uuid, response);
        }
        return resultBean;
    }

    @RequestMapping("update/{productId}/{count}")
    @ResponseBody
    public ResultBean update(@CookieValue(name = "user_cart",required = false) String uuid,
                             @PathVariable("productId") Long productId,
                             @PathVariable("count") Integer count,
                             HttpServletResponse response){
        //1.判断
        if (uuid == null || "".equals(uuid)){
            return new ResultBean("404","购物车为空");
        }
        //2.执行删除的操作
        ResultBean resultBean = cartService.update(uuid,productId,count);
        //3.
        //3.判断
        if("200".equals(resultBean.getStatusCode())){
            reflushCookie(uuid, response);
        }
        return resultBean;
    }

    private void reflushCookie(@CookieValue(name = "user_cart", required = false) String uuid, HttpServletResponse response) {
        //4.写cookie到客户端
        Cookie cookie = new Cookie("user_cart",uuid);
        cookie.setPath("/");
        cookie.setDomain("qf.com");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7*24*60*60);
        //
        response.addCookie(cookie);
    }

    //TODO
    //1.修改删除和更新，区分登录和未登录
    //2.cart-service 提供一个合并购物车的接口，将未登录合并到已登录上面，干掉未登录
    //3.登录成功之后，调用该接口，实现购物车合并
}
