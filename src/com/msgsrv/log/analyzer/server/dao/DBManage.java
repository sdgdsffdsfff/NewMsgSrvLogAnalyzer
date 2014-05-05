/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.account.check.dao.impl<br/>
 * <b>文件名：</b>DBUtil.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-4-26-上午10:27:33<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.MultiThreadExecutors;
import com.msgsrv.log.analyzer.common.YhbfShell;

/**
 * 
 * <b>类名称：</b>DBUtil<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-4-26 上午10:27:33<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class DBManage {

	private static final Logger log = Logger.getLogger(DBManage.class);

	private static String url;

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static String user;
	private static String password;

	private static Connection conn = null;

	static {
		try {
			File file = new File("config/db.properties");
			if (!file.exists()) {
				throw new RuntimeException("config目录下不存在此文件[db.properties]");
			} else if (!file.canRead()) {
				throw new RuntimeException("config目录下文件[db.properties]没有读取权限");
			}
			FileInputStream in = new FileInputStream(file);
			Properties p = new Properties();
			p.load(in);
			url = p.getProperty("db.url");
			user = p.getProperty("db.user");
			password = p.getProperty("db.password");
			// 解密字符串
			password = YhbfShell.decoder(password);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private DBManage() {
		throw new AssertionError();
	}

	public static synchronized Connection getConnection() {
		if (conn == null) {
			conn = createConnection();
			MultiThreadExecutors.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						log.info("发送MYSQL心跳包，避免MYSQL连接超时......");
						Statement statement = conn.createStatement();
						statement.execute("SELECT 1;");
					} catch (SQLException e) {
						conn = createConnection();
						log.error(e.getMessage(), e);
					}
				}
			}, 1, 2, TimeUnit.HOURS);
		}
		return conn;
	}

	private static Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return conn;
	}

}
