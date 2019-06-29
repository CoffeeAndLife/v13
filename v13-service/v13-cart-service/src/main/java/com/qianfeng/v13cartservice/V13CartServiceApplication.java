package com.qianfeng.v13cartservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qianfeng.v13.mapper")
public class V13CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13CartServiceApplication.class, args);
	}

}
