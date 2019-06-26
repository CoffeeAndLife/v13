package com.qianfeng.temptimer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempTimerApplicationTests {

	@Test
	public void contextLoads() throws InterruptedException {
		System.out.println("main:"+new Date());
	}

}
