package com.msgsrv.log.analyzer.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AlarmEmailService {

	private static final Logger LOGGER = Logger.getLogger(AlarmEmailService.class);

	private static final List<String> RECEIVER = new ArrayList<String>();
	static {
		RECEIVER.add("382269985@qq.com");// 袁湘武
		//RECEIVER.add("116124411@qq.com");// 李昌哲
	}

	public static void sendEmail(final String content) {
		LOGGER.info("发送报警邮件信息：" + content);
		// 应用示例：线程化发送邮件
		new Thread() {
			@Override
			public void run() {
				// 创建邮件对象
				JavaMail mail = new JavaMail();
				mail.setHost("220.181.15.112"); // 邮件服务器地址
				mail.setFrom("MsgSrvLogAnalyzer@126.com"); // 发件人邮箱
				mail.addTo(RECEIVER); // 收件人邮箱
				// mail.addCc("test@m1.com");//添加抄送
				// mail.addBcc("test@m2.com");//添加暗送
				mail.setSubject("[深圳年年卡] [日志分析程序] [报警通知]"); // 邮件主题
				mail.setUser("MsgSrvLogAnalyzer@126.com"); // 用户名
				mail.setPassword("8877007"); // 密码
				mail.setContent(content); // 邮件正文
				// mail.addAttachment("utf8中.txt"); // 添加附件
				// mail.addAttachment(
				// "e:/test.htm"); //
				// 添加附件
				String recode = mail.send(); // 发送
				LOGGER.info("ReturnCode:" + recode);
			}
		}.start();
	}
}
