/**
 * <b>包名：</b>com.msgsrv.log.analyzer.client.task<br/>
 * <b>文件名：</b>AnalyzerLogFileTaskTest.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-24-下午2:50:50<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.client.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.common.SingleThreadExecutors;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.core.TaskCache;
import com.msgsrv.log.analyzer.server.dao.DBManage;
import com.msgsrv.log.analyzer.server.task.AnalysisTask;
import com.msgsrv.log.analyzer.server.task.CacheClearTask;
import com.msgsrv.log.analyzer.server.task.SqlCacheTask;
import com.msgsrv.log.analyzer.server.task.TimePointTask;

/**
 * 
 * <b>类名称：</b>AnalyzerLogFileTaskTest<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-24 下午2:50:50<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class CacheSqlTaskTest {

	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	@Test
	public void testCacheSql() throws Exception {
		Thread.sleep(1000);
		SystemConfig.load();
		List<String> clients = SystemConfig.CLIENTS;
		Map<String, String> filePath = SystemConfig.FILE_PATH;
		Map<String, String> filePrefix = SystemConfig.FILE_PREFIX;
		for (String client : clients) {
			TaskCache.ANALYZER_FILE_CHACE.put(client, new AnalyzerLogFileTask(client, filePath.get(client), filePrefix.get(client)));
		}

		AnalyzerCoreMemory.TIME_POINT = DateUtil.parse("20130704000000");

		AnalysisTask analysisTask = new AnalysisTask();//
		CacheClearTask cacheClearTask = new CacheClearTask();
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		TimePointTask timePointTask = new TimePointTask();
		while (true) {
			MONITOR_LOGGER.info("=======================================================");
			for (String client : clients) {
				AnalyzerLogFileTask analyzerLogFileTask = TaskCache.ANALYZER_FILE_CHACE.get(client);
				SingleThreadExecutors.execute(analyzerLogFileTask);
			}
			SingleThreadExecutors.execute(analysisTask);
			SingleThreadExecutors.execute(cacheClearTask);
			SingleThreadExecutors.execute(timePointTask);
			SingleThreadExecutors.execute(sqlCacheTask);
			System.err.println("当前分析时间:" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT));
			synchronized (timePointTask) {
				timePointTask.wait();
			}
		}
	}

	// @Test
	// public void test2() throws Exception {
	// AnalyzerLogFileTask analyzerLogFileTask = new AnalyzerLogFileTask("6004",
	// "F:/logs/MsgSrv20/6004", filePrefix)
	// }

	@Test
	public void testBatch() throws Exception {
		Statement statement = null;
		try {
			statement = DBManage.getConnection().createStatement();
			statement.addBatch("INSERT INTO test(test) VALUES('1');");
			statement.addBatch("INSERT INTO test(test) VALUES('1');");
			statement.addBatch("INSERT INTO test(test) VALUES('2');");
			statement.addBatch("INSERT INTO test(test) VALUES('1');");
			statement.addBatch("INSERT INTO test(test) VALUES('3');");
			statement.addBatch("INSERT INTO test(test) VALUES('1');");
			statement.addBatch("INSERT INTO test(test) VALUES('4');");
			statement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Before
	public void test() throws Exception {
		DBManage.getConnection();
		AnalyzerCoreMemory.TIME_POINT = new Date();
		for (int i = 0; i < 10000; i++) {
			AnalyzerCoreMemory.SQL_CACHE
					.add("INSERT INTO OrderTimeLog(identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time) VALUES('"
							+ UUID.randomUUID()
							+ "','query','16','20140212010746.464419','20140212010746.464576','157','157','0','1','M NewOrderReceiver to OrderVerify',now());");
		}
	}

	@Test
	public void testCache10000() throws Exception {
		System.out.println(AnalyzerCoreMemory.SQL_CACHE.size());
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		sqlCacheTask.run();
	}

	@Test
	public void testJDBC10000() throws Exception {
		System.out.println(AnalyzerCoreMemory.SQL_CACHE.size());
		Connection connection = DBManage.getConnection();
		Statement statement = connection.createStatement();
		String sql = null;
		while ((sql = AnalyzerCoreMemory.SQL_CACHE.poll()) != null) {
			statement.execute(sql);
		}
	}

	public static void main(String[] args) {

	}
}
