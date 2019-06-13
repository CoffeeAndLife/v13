package com.qianfeng.v13centerweb;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CenterWebApplicationTests {

	@Autowired
	private FastFileStorageClient client;

	@Test
	public void uploadTest() throws FileNotFoundException {
		//1.SpringBoot整合FastDFS，实现文件的上传
		File file = new File("D:\\dev\\v13\\v13-web\\v13-center-web\\1.png");
		FileInputStream fileInputStream =
				new FileInputStream(file);

		/*StorePath storePath =
				client.uploadFile(fileInputStream, file.length(), "html", null);*/

		StorePath storePath = client.uploadImageAndCrtThumbImage(fileInputStream, file.length(), "png", null);

		System.out.println(storePath.getFullPath());
		System.out.println(storePath.getGroup());
		System.out.println(storePath.getPath());
		//2.融入到项目中来


		//3.异步上传+uploadify

	}

	@Test
	public void delTest(){
		client.deleteFile("group1/M00/00/00/wKiOhl0B3zmAfp37AABR_7NB1m0040_100x100.png");
		System.out.println("delete success!");
	}

}
