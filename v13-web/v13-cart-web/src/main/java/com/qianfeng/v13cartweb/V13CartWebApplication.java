package com.qianfeng.v13cartweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class V13CartWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13CartWebApplication.class, args);
	}

}
