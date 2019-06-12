package com.qianfeng.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qianfeng.v13.api.IProductService;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.pojo.TProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

	@Autowired
	private IProductService productService;

	@Test
	public void contextLoads() {
		//
		List<Long> ids = new ArrayList<>();
		ids.add(2L);
		ids.add(4L);

		System.out.println(productService.batchDel(ids));

	}


}
