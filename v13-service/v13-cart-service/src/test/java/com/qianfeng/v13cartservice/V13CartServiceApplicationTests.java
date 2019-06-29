package com.qianfeng.v13cartservice;

import com.qianfeng.v13.api.ICartService;
import com.qianfeng.v13.common.pojo.ResultBean;
import com.qianfeng.v13.pojo.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CartServiceApplicationTests {

	@Autowired
	private ICartService cartService;

	@Resource(name = "redisTemplate1")
	private RedisTemplate<String,Object> redisTemplate;

	@Test
	public void addTest(){
		String uuid = "d0f2046c-30cc-4d49-bd63-6cecaa9e706a";
		ResultBean resultBean = cartService.add(uuid, 1L, 10);
		System.out.println(resultBean.getStatusCode());
	}

	@Test
	public void queryTest(){
		ResultBean resultBean = cartService.query("d0f2046c-30cc-4d49-bd63-6cecaa9e706a");
		TreeSet<CartItem> cartItems = (TreeSet<CartItem>) resultBean.getData();

		for (CartItem cartItem : cartItems) {
			System.out.println(cartItem);
		}
	}

	@Test
	public void delTest(){
		ResultBean resultBean = cartService.del("d0f2046c-30cc-4d49-bd63-6cecaa9e706a", 2L);
		System.out.println(resultBean.getStatusCode());
	}

	@Test
	public void updateTest(){
		ResultBean resultBean = cartService.update("d0f2046c-30cc-4d49-bd63-6cecaa9e706a", 1L, 10000);
		System.out.println(resultBean.getStatusCode());
	}



}
