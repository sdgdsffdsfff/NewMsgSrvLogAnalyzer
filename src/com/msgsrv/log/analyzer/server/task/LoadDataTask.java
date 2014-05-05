package com.msgsrv.log.analyzer.server.task;

import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.LOAD_DATA_CACHE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.dao.DBManage;

public class LoadDataTask implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(SqlCacheTask.class);
	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	@Override
	public void run() {
		long startTime = System.nanoTime();
		if (LOAD_DATA_CACHE.isEmpty()) {
			MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[0] 插入数据[0] [LOAD DATA INFILE] 数据缓存大小[" + LOAD_DATA_CACHE.size()
					+ "]");
			return;
		}
		int size = LOAD_DATA_CACHE.size();
		String sqlFileName = "sql/" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT, "yyyyMMdd") + "/" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT)
				+ ".sql";
		File sqlFile = new File(sqlFileName);
		if (!sqlFile.getParentFile().exists()) {
			sqlFile.getParentFile().mkdirs();
		}
		sqlFileName = sqlFile.getAbsolutePath().replaceAll("\\\\", "/");
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(sqlFile));
			String data = null;
			while ((data = LOAD_DATA_CACHE.poll()) != null) {
				bufferedWriter.write(data);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.flush();
				} catch (IOException e) {
				}
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Statement statement = null;
		try {
			statement = DBManage.getConnection().createStatement();
			String sql = "load data local infile '"
					+ sqlFileName
					+ "' replace into table OrderTimeLog character set GBK fields terminated by ',' enclosed by '\\'' LINES TERMINATED BY '\\n' (identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time)";
			LOGGER.info("SQL:" + sql);
			statement.execute(sql);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
		long endTime = System.nanoTime();
		long useTime = (endTime - startTime) / 1000000;
		MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] 插入数据[" + size + "] [LOAD DATA INFILE] 数据缓存大小 ["
				+ LOAD_DATA_CACHE.size() + "]");
	}
}
