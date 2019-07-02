package com.qianfeng.v13orderweb.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.v13orderweb.pojo.PayContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("aliPay")
public class PayContorller {

    @Autowired
    private AlipayClient alipayClient;

    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    @RequestMapping("toPay")
    public void toPay(HttpServletResponse response,String orderNo) throws IOException {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        alipayRequest.setNotifyUrl("http://u52gu3.natappfree.cc/aliPay/notifyPayResult");//在公共参数中设置回跳和通知地址

        //创建一个支付业务对象
        PayContent payContent = new PayContent(orderNo,"FAST_INSTANT_TRADE_PAY",
                "8888","家庭影院","超大高清屏幕");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(payContent);

        alipayRequest.setBizContent(json);
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    //TODO 编写一个处理异步通知的接口
    //TODO 内网穿透（让外网能访问内网）NatAPP 看教程

    @RequestMapping("notifyPayResult")
    public void notifyPayResult(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        //1.获取到所有的请求参数
        Map<String, String[]> sourceMap = request.getParameterMap();//{1,2,3}
        //2.SDK需要的参数类型
        Map<String, String> paramsMap = new HashMap<>(); //将异步通知中收到的所有参数都存放到map中
        //3.sourceMap->paramsMap
        Set<Map.Entry<String, String[]>> entries = sourceMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            //4.构建value
            StringBuilder value = new StringBuilder();
            String[] sourctArray = entry.getValue();
            for(int i=0;i<sourctArray.length-1;i++){
                value.append(sourctArray[i]).append(",");
            }
            value.append(sourctArray[sourctArray.length-1]);

            //保存到paramsMap
            paramsMap.put(entry.getKey(),value.toString());
        }
        //4.验签，确实是否为支付宝发送过来的信息
        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, alipayPublicKey, "utf-8", "RSA2"); //调用SDK验证签名

        if(signVerified){
            System.out.println("是支付宝发过来的信息");
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            //核对订单号和金额是否跟你自己的数据库的数据是否匹配
            //1.获取到订单号
            String orderNo = request.getParameter("out_trade_no");
            //2.获取到订单金额
            String money = request.getParameter("total_amount");
            //3.根据订单号去查询我们数据库的订单金额
            //4.支付宝发送过来的订单金额跟我们的订单金额匹配，说明没问题
            //5.修改订单的状态为已支付
            //6.给支付宝返回一个表示success
            System.out.println(orderNo);
            System.out.println(money);
            String trade_no = request.getParameter("trade_no");
            System.out.println(trade_no);

            PrintWriter writer = response.getWriter();
            writer.write("success");
            writer.flush();
            writer.close();

            //假如金额对不上,本地要记录支付流水（对不上） insert into
            //返回failure
            //对账
            // 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("不是支付宝发过来的");
            PrintWriter writer = response.getWriter();
            writer.write("failure");
            writer.flush();
            writer.close();
        }

    }
}
