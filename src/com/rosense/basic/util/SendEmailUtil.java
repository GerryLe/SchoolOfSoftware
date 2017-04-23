package com.rosense.basic.util;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class SendEmailUtil {


	public static void sendMail(String title, String content, String[] str) {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		// 设定mail server
		senderImpl.setHost("smtp.mxhichina.com");
		senderImpl.setPort(25);
		senderImpl.setProtocol("smtp");
		// 建立邮件消息
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// 设置收件人，寄件人 用数组发送多个邮件
		//String[] array = new String[] { to};
		mailMessage.setTo(str);
		mailMessage.setFrom("honylong@lingnan.com");
		mailMessage.setSubject(title);
		mailMessage.setText(content);

		senderImpl.setUsername("honylong@lingnan.com");
		senderImpl.setPassword("Dragon520");

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
		prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.setProperty("mail.smtp.port", "465");
		prop.setProperty("mail.smtp.socketFactory.port", "465");
		// 发送邮件
		senderImpl.send(mailMessage);

	}
	
	
	
}
