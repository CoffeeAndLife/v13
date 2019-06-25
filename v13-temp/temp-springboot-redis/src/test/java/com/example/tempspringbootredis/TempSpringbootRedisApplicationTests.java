package com.example.tempspringbootredis;

import com.example.tempspringbootredis.entity.ProductType;
import com.example.tempspringbootredis.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempSpringbootRedisApplicationTests {

	@Resource(name = "redisTemplate2")
	private RedisTemplate<String,Object> redisTemplate;

	@Test
	public void stringTest() {
		//redisTemplate.setKeySerializer(new StringRedisSerializer());
		/*redisTemplate.opsForValue().set("target","springBoot整合redis");
		System.out.println(redisTemplate.opsForValue().get("target"));*/

		//
		redisTemplate.opsForValue().set("student",new Student("zhangwuyi"));
		Object student = redisTemplate.opsForValue().get("student");//多态
		System.out.println(student);
	}

	@Test
	public void otherTest(){
		//redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.opsForValue().set("target","springBoot整合redis");
		System.out.println(redisTemplate.opsForValue().get("target"));
	}

	//有可能出现：100次访问，有十几次访问了数据库（只允许1次访问数据库）
	@Test
	public void cacheTest(){
		//TODO 改造商品服务获取商品类别数据的逻辑
		//1.先问缓存有没有
		String key = "product:types";
		List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get(key);
		if(types == null){
			//2.缓存中没有，则需要去数据库查找
			System.out.println("缓存中没有，则需要去数据库查找。。。。。");
			types = new ArrayList<>();
			types.add(new ProductType(1,"电子数码"));
			types.add(new ProductType(2,"黑科技"));
			//3.放入缓存
			redisTemplate.opsForValue().set(key,types);
			System.out.println("放入缓存成功！");
		}else{
			System.out.println("从缓存中获取到数据");
			for (ProductType type : types) {
				System.out.println(type.getName());
			}
		}
	}

}
