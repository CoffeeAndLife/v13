package com.qianfeng.v13itemweb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ItemWebApplicationTests {

	@Autowired
	private Configuration configuration;

	@Test
	public void createHTMLTest() throws IOException, TemplateException {
		//1.模板
		Template template = configuration.getTemplate("hello.ftl");
		//2.数据
		Map<String,Object> data = new HashMap<>();
		data.put("username","java1902");
		//保存对象
		Student student = new Student(1,"zhangsan",new Date());
		data.put("student",student);
		//保存集合
		List<Student> list = new ArrayList<>();
		list.add(new Student(1,"zhangsan",new Date()));
		list.add(new Student(2,"lisi",new Date()));
		list.add(new Student(3,"wangwu",new Date()));
		data.put("list",list);
		//判断条件
		data.put("money",100000);
		//空值的问题
		data.put("msg",null);
		//3.输出
		FileWriter out = new FileWriter("D:\\dev\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\hello.html");
		//4.集结
		template.process(data,out);
		System.out.println("生成静态页面成功！");
	}

}
