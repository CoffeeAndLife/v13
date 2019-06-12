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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

	@Autowired
	private IProductService productService;

	@Test
	public void contextLoads() {
		//
		TProduct product = new TProduct();
		product.setName("高级Java程序员");
		product.setPrice(20000L);
		product.setSalePoint("多年的Java开发经验");
		product.setImages("暂无");
		product.setTypeId(1L);
		product.setTypeName("电子数码");
		product.setSalePrice(19999L);
		product.setFlag(true);

		TProductVO vo = new TProductVO();
		vo.setProductDesc("这是一个资深的菜鸟！");
		vo.setProduct(product);

		Long id = productService.save(vo);
		System.out.println(id);

	}


}
