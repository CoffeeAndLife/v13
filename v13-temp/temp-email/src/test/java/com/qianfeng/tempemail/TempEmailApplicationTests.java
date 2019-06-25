package com.qianfeng.tempemail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempEmailApplicationTests {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Value("${mail.fromAddr}")
	private String fromAddr;

	@Test
	public void sendMailTest() {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(fromAddr);
		mail.setTo(fromAddr);
		mail.setSubject("高度机密邮件");
		mail.setText("今天欧文会加盟湖人,详细新闻链接<a href='https://www.baidu.com'>地址</a>");

		javaMailSender.send(mail);

		System.out.println("发送邮件成功！");

		//springboot整合mail 发送带HTML格式
	}

	@Test
	public void htmlTest(){
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			helper.setFrom(fromAddr);
			helper.setTo(fromAddr);
			helper.setSubject("巴特勒底薪加盟湖人");
			helper.setText("今天巴特勒会加盟湖人,详细新闻链接<a href='https://www.baidu.com'>地址</a>",true);
			javaMailSender.send(message);

			System.out.println("发送成功！");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void templateTest(){
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			helper.setFrom(fromAddr);
			helper.setTo(fromAddr);
			helper.setSubject("巴特勒底薪加盟湖人");
			//邮件的内容由模板来生成
			Context context = new Context();
			context.setVariable("username","佩奇");
			String text = templateEngine.process("jihuo.html", context);
			//
			//helper.setText("今天巴特勒会加盟湖人,详细新闻链接<a href='https://www.baidu.com'>地址</a>",true);
			helper.setText(text,true);
			javaMailSender.send(message);

			System.out.println("发送成功！");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
