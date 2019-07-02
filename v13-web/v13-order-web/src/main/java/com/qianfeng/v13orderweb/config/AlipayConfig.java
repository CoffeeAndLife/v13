package com.qianfeng.v13orderweb.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class AlipayConfig {

    @Value("${alipay.privateKey}")
    private String privateKey;

    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    @Bean
    public AlipayClient getAlipayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016091100489896",
                privateKey,
                "json",
                "utf-8",
                alipayPublicKey,
                "RSA2"); //获得初始化的AlipayClient
        return alipayClient;
    }
}
