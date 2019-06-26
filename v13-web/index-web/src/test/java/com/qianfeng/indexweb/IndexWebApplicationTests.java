package com.qianfeng.indexweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.indexweb.pojo.SMSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexWebApplicationTests {

	@Test
	public void contextLoads() throws IOException {
		String json = "{\"respCode\":\"00000\",\"respDesc\":\"请求成功。\",\"failCount\":\"0\",\"failList\":[],\"smsId\":\"9ba28fb753994b42aa6d09c4dbd19776\"}";

		ObjectMapper objectMapper = new ObjectMapper();

		SMSResponse smsResponse = objectMapper.readValue(json, SMSResponse.class);
		System.out.println(smsResponse);

		System.out.println(objectMapper.writeValueAsString(smsResponse));
	}

}
