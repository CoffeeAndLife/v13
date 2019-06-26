package com.qianfeng.temptimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TempTimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TempTimerApplication.class, args);
	}

}
