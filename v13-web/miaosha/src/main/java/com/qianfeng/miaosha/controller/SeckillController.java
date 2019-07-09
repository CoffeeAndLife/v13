package com.qianfeng.miaosha.controller;

import com.qianfeng.miaosha.exception.SeckillException;
import com.qianfeng.miaosha.pojo.ResultBean;
import com.qianfeng.miaosha.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("seckill")
public class SeckillController {

    @Autowired
    private ISeckillService seckillService;

    @RequestMapping("sale")
    @ResponseBody
    public ResultBean sale(Long seckillId,Long userId){
        //说明：正常此处的userId应该是通过拦截器获取到的，类似我们之前讲订单或购物车的时候
        //此处作为参数，只是为了方便做调试用
        try {
            seckillService.sale(seckillId,userId);
            //秒杀成功之后，发送消息给到交换机
            String orderNo = seckillService.sendOrderMsg(seckillId,userId);
            //将生成的订单编号返回到页面
            return new ResultBean("200",orderNo);
        } catch (SeckillException e){
            return new ResultBean("404",e.getMessage());
        }
    }
}
