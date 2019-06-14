package com.qianfeng.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qianfeng.v13.api.IProductService;
import com.qianfeng.v13.api.IProductTypeService;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.entity.TProductType;
import com.qianfeng.v13.pojo.TProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

	@Autowired
	private IProductService productService;

	@Autowired
	private IProductTypeService productTypeService;

	@Autowired
	private DataSource dataSource;

	@Test
	public void connectionTest() throws SQLException {
		System.out.println(dataSource.getConnection());
	}



	@Test
	public void contextLoads() {
		List<TProductType> list = productTypeService.list();
		System.out.println(list.size());
	}


}
