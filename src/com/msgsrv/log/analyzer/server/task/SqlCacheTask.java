/**
 * <b>包名：</b>com.msgsrv.log.analyzer.server.task<br/>
 * <b>文件名：</b>SqlCacheTask.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-28-上午10:12:38<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.task;

import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.SQL_CACHE;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.common.TimeUitl;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.core.MsgSrvService;
import com.msgsrv.log.analyzer.server.dao.DBManage;

/**
 * 
 * <b>类名称：</b>SqlCacheTask<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-28 上午10:12:38<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class SqlCacheTask implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(SqlCacheTask.class);

	private static final int BATCH_SIZE = 100;
	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	@Override
	public void run() {
		long startTime = System.nanoTime();
		int counter = 0;
		Statement statement = null;
		try {
			statement = DBManage.getConnection().createStatement();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		int size = SQL_CACHE.size();
		String sql = null;
		while ((sql = SQL_CACHE.poll()) != null) {
			LOGGER.info("SQL:" + sql);
			try {
				statement.addBatch(sql);
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(), e);
			}
			counter++;
			if (counter % BATCH_SIZE == 0) {
				try {
					statement.executeBatch();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		try {
			statement.executeBatch();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

		// 判断分析时间是否是凌晨,凌晨备份数据库表
		if (TimeUitl.isWeeHours(AnalyzerCoreMemory.TIME_POINT)) {
			String newOrderTimeLogTableName = "OrderTimeLog_" + DateUtil.format(new Date(AnalyzerCoreMemory.TIME_POINT.getTime() - TimeUitl.DAYS));
			try {
				statement.execute("RENAME TABLE OrderTimeLog TO " + newOrderTimeLogTableName);
				MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 数据库表[ OrderTimeLog ] 更名  [ " + newOrderTimeLogTableName + " ]");
				// 创建新表
				statement.execute("CREATE TABLE OrderTimeLog LIKE " + newOrderTimeLogTableName);
				MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 建立数据库表[ OrderTimeLog ]");
				
				
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
			
			MsgSrvService.send("MsgSrvLogAnalyzerStatistics RenameTable " + newOrderTimeLogTableName);
		}
		long endTime = System.nanoTime();
		long useTime = (endTime - startTime) / 1000000;
		MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] 插入数据[" + size + "] [结束批量执行SQL] 数据库语句缓存大小 ["
				+ SQL_CACHE.size() + "]");
	}

}
