package com.qianfeng.v13ssoweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangguizhao
 *
 * TODO 将这些常量管理起来
 */
@Controller
@RequestMapping("sso")
public class SsoController {

    @Reference
    private IUserService userService;
    //1，登录的认证接口
    //1.1 第一种是返回ResultBean的方式（Ajax）
    //1.2 第二种是跳转到相关页面的方式（form）
    @RequestMapping("checkLogin")
    public String checkLogin(TUser user, HttpServletResponse response){
        //用户的认证+redis中保存凭证 uuid---user
        ResultBean resultBean = userService.checkLogin(user);
        if("200".equals(resultBean.getStatusCode())){
            //cookie(user_token-----uuid)
            //1.创建cookie
            Cookie cookie = new Cookie("user_token",resultBean.getData().toString());
            cookie.setPath("/");
            //表示只能通过后端的方式来访问到这个cookie
            cookie.setHttpOnly(true);
            //避免客户端通过document.cookie获取到我们的cookie
            //2.将这个cookie写到客户端
            response.addCookie(cookie);

            //3.返回一个视图
            //登录成功，返回到首页系统
            return "redirect:http://localhost:9091/index/home";
        }
        //验证失败，回到登录页
        return "index";
    }

    @RequestMapping("checkLogin2")
    @ResponseBody
    public ResultBean checkLogin2(TUser user,HttpServletResponse response){
        //用户的认证+redis中保存凭证 uuid---user
        ResultBean resultBean = userService.checkLogin(user);
        if("200".equals(resultBean.getStatusCode())){
            //cookie(user_token-----uuid)
            //1.创建cookie
            Cookie cookie = new Cookie("user_token",resultBean.getData().toString());
            cookie.setPath("/");
            //表示只能通过后端的方式来访问到这个cookie
            cookie.setHttpOnly(true);
            //避免客户端通过document.cookie获取到我们的cookie
            //2.将这个cookie写到客户端
            response.addCookie(cookie);

            //3.返回一个视图
            //登录成功，返回到首页系统
            return new ResultBean("200",resultBean.getData());
        }
        //验证失败，回到登录页
        return new ResultBean("400","");
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        //1.解析cookie，获取到凭证信息uuid
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            //2.遍历比较
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    //3.通过uuid验证用户的登录状态
                    return userService.checkIsLogin(uuid);
                }
            }
        }
        //3.返回找不到的结果
        return new ResultBean("404",null);
    }


    @RequestMapping("checkIsLogin2")
    @ResponseBody
    public ResultBean checkIsLogin2(@CookieValue(name = "user_token",required = false) String uuid){
        //简化获取cookie的方式
        //1.解析cookie，获取到凭证信息uuid
        if(uuid != null){
            return userService.checkIsLogin(uuid);
        }
        //3.返回找不到的结果
        return new ResultBean("404",null);
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = "user_token",required = false) String uuid,
                             HttpServletResponse response){
        if(uuid != null){
            //删除cookie
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            //设置为0，表示失效
            cookie.setMaxAge(0);
            //把cookie写到客户端
            response.addCookie(cookie);
            //删除redis的凭证
            return userService.logout(uuid);
        }
        return new ResultBean("404","注销失败！");
    }
}
