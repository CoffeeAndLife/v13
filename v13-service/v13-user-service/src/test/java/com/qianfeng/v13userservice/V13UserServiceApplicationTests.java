package com.qianfeng.v13userservice;

import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.entity.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13UserServiceApplicationTests {

	@Autowired
	private IUserService userService;

	@Test
	public void contextLoads() {
		TUser user = new TUser();
		user.setUsername("admin");
		user.setPassword("123123");
		ResultBean resultBean = userService.checkLogin(user);
		System.out.println(resultBean.getStatusCode());
		System.out.println(resultBean.getData());
	}

	@Test
	public void checkIsLoginTest(){
		ResultBean resultBean = userService.checkIsLogin("4b2b2d9d-44a8-4bb3-a2f8-a003f9dac8da");
		System.out.println(resultBean.getStatusCode());
		TUser user = (TUser) resultBean.getData();
		if(user != null){
			System.out.println(user.getUsername());
		}
	}


}
