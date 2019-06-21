package com.qianfeng.tempspringbootrabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempSpringbootRabbitmqApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	public void contextLoads() {
		//rabbitTemplate.convertAndSend("","springBoot-simple-queue","SpringBoot整合RabbitMQ");
		//rabbitTemplate.convertAndSend("fanout_exchange","","gaoding!");
		Book book = new Book(1,"终生学习",99);
		//
		rabbitTemplate.convertAndSend("","springBoot-simple-queue",book);
	}

}
