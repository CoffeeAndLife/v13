package com.qianfeng.v13cartweb.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.pojo.ResultBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangguizhao
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{

    @Reference
    private IUserService userService;

    /**
     * 前置拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.判断当前用户是否已经登录
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    if("200".equals(resultBean.getStatusCode())){
                        //保存当前登录用户的状态信息
                        request.setAttribute("user",resultBean.getData());
                    }
                }
            }
        }
        //2.最终都是放行
        return true;
    }
}
