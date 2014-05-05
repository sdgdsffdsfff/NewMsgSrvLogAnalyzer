package com.msgsrv.log.analyzer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.StringUtil;
import com.msgsrv.log.analyzer.common.YhbfShell;

/**
 * 
 * <b>类名称：</b>SystemConfig<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-21 下午2:25:06<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class SystemConfig {

	private static final Logger LOGGER = Logger.getLogger(SystemConfig.class);

	public static final List<String> CLIENTS = new ArrayList<String>();// 分析客户端名称
	public static final Map<String, String> FILE_PATH = new HashMap<String, String>();// 分析客户端文件路径
	public static final Map<String, String> FILE_PREFIX = new HashMap<String, String>();// 分析客户端文件路径

	public static String START_TIME_POINT = null;// 日志文件的文件的开始时间
	public static int NEXT_TIME = 5;// 每次分析多长时间的日志信息(单位:分钟)
	public static int RESULT_TIMEOUT = 10 * 60 * 1000;// 日志在缓存中的超时时间(单位:分钟)
	public static String DATABASE_TYPE = "cache";
	public static String ALARM_KEY = "";// 报警系统的协议签名KEY
	public static boolean ALARM_ENABLED = true;// 是否启用报警
	public static long ALARM_TIMEOUT = 120;// 报警超时时间
	public static long ALARM_FREQUENCY = 120;// 报警检查频率
	public static boolean FILE_DELETE_ENABLED = true;

	public static void load() {
		File file = new File("config/system.properties");
		if (!file.exists()) {
			LOGGER.warn("config目录下不存在此文件[system.properties]");
		} else if (!file.canRead()) {
			LOGGER.warn("config目录下文件[system.properties]没有读取权限");
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			Properties p = new Properties();
			p.load(in);
			if (StringUtil.isNotEmpty(p.getProperty("system.msgsrv.clients"))) {
				CLIENTS.clear();
				String[] clients = p.getProperty("system.msgsrv.clients").split(",");
				for (String client : clients) {
					if (StringUtil.isNotEmpty(client)) {
						CLIENTS.add(client);
						String path = p.getProperty(client + ".analyzer.log.file.path");
						String prefix = p.getProperty(client + ".analyzer.file.prefix");
						FILE_PATH.put(client, path);
						FILE_PREFIX.put(client, prefix);
					}
				}
			}

			// =20130704000000
			// #每次分析多长时间的日志信息(单位:分钟)
			// = 5
			// #
			// analyzer.result.timeout = 60
			// 日志文件的文件的开始时间
			if (StringUtil.isNotEmpty(p.getProperty("start.time.point"))) {
				START_TIME_POINT = p.getProperty("start.time.point").trim();
			}
			// 日志在缓存中的超时时间(单位:分钟)
			if (StringUtil.isNotEmpty(p.getProperty("next.time.point.length"))) {
				NEXT_TIME = Integer.parseInt(p.getProperty("next.time.point.length").trim());
			}
			// 日志在缓存中的超时时间(单位:分钟)
			if (StringUtil.isNotEmpty(p.getProperty("analyzer.result.timeout"))) {
				RESULT_TIMEOUT = Integer.parseInt(p.getProperty("analyzer.result.timeout").trim()) * 60 * 1000;
			}
			// 日志在缓存中的超时时间(单位:分钟)
			if (StringUtil.isNotEmpty(p.getProperty("database.type"))) {
				DATABASE_TYPE = p.getProperty("database.type").trim();
			}
			// 报警系统协议签名key
			if (StringUtil.isNotEmpty(p.getProperty("alarm.key"))) {
				ALARM_KEY = YhbfShell.decoder(p.getProperty("alarm.key"));
			}

			// #是否启用报警系统
			if (StringUtil.isNotEmpty(p.getProperty("alarm.enabled"))) {
				ALARM_ENABLED = Boolean.parseBoolean(p.getProperty("alarm.enabled"));
			}
			// #分析时长超过多少报警一次
			if (StringUtil.isNotEmpty(p.getProperty("alarm.timeout"))) {
				ALARM_TIMEOUT = Long.parseLong(p.getProperty("alarm.timeout"));
			}
			// #报警检查频率
			if (StringUtil.isNotEmpty(p.getProperty("alarm.frequency"))) {
				ALARM_FREQUENCY = Long.parseLong(p.getProperty("alarm.frequency"));
			}
			if (StringUtil.isNotEmpty(p.getProperty("file.delete.enabled"))) {
				FILE_DELETE_ENABLED = Boolean.parseBoolean(p.getProperty("file.delete.enabled"));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(-1);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		builder.append("\n============================================\n");
		for (String client : CLIENTS) {
			builder.append("Client [" + client + "] 日志文件位置 [" + FILE_PATH.get(client) + "] 日志格式  [" + FILE_PREFIX.get(client) + "] \n");
		}
		builder.append("日志起始分析时间[" + START_TIME_POINT + "] 每次累计分析  [" + NEXT_TIME + "分钟] 分析结果超时时间 [" + RESULT_TIMEOUT + "毫秒] \n");
		builder.append("数据可存储方案[" + DATABASE_TYPE + "] \n");
		builder.append("是否启用报警系统[" + ALARM_ENABLED + "] \n");
		builder.append("分析超时时间报警[" + ALARM_TIMEOUT + "] \n");
		builder.append("超时报警检查频率[" + ALARM_FREQUENCY + "] \n");
		builder.append("============================================");
		LOGGER.info(builder.toString());
	}
}
