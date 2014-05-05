/**
 * <b>包名：</b>com.msgsrv.log.analyzer<br/>
 * <b>文件名：</b>Main.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-24-上午10:47:46<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.client.task.AnalyzerLogFileTask;
import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.common.TimeUitl;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AlarmService;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.core.TaskCache;
import com.msgsrv.log.analyzer.server.task.AnalysisTask;
import com.msgsrv.log.analyzer.server.task.CacheClearTask;
import com.msgsrv.log.analyzer.server.task.LoadDataTask;
import com.msgsrv.log.analyzer.server.task.SqlCacheTask;
import com.msgsrv.log.analyzer.server.task.TimePointTask;
import com.nnk.msgsrv.client.common.SingleThreadExecutors;
import com.nnk.msgsrv.client.core.MsgSrvManager;

/**
 * 
 * <b>类名称：</b>Main<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-24 上午10:47:46<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	public static void main(String[] args) throws Exception {
		SystemConfig.load();
		MsgSrvManager.start("config/msgsrv.xml");
		Thread.sleep(2000);
		if (SystemConfig.ALARM_ENABLED) {
			SingleThreadExecutors.scheduleAtFixedRate((new Runnable() {
				@Override
				public void run() {
					if (AnalyzerCoreMemory.TIME_POINT != null) {
						long currentTimeMillis = System.currentTimeMillis();
						long timePoint = AnalyzerCoreMemory.TIME_POINT.getTime();
						long timeout = currentTimeMillis - timePoint;
						if (timeout > SystemConfig.ALARM_TIMEOUT * TimeUitl.MINUTES) {
							timeout = timeout / TimeUitl.MINUTES;
							AlarmService.alarm("[日志分析系统] 当前分析时间[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 超时分析[" + timeout + "]分钟");
						}
					}
				}
			}), SystemConfig.ALARM_FREQUENCY, SystemConfig.ALARM_FREQUENCY, TimeUnit.MINUTES);
		}
		try {
			start();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private static void start() throws Exception {
		List<String> clients = SystemConfig.CLIENTS;
		Map<String, String> filePath = SystemConfig.FILE_PATH;
		Map<String, String> filePrefix = SystemConfig.FILE_PREFIX;
		for (String client : clients) {
			TaskCache.ANALYZER_FILE_CHACE.put(client, new AnalyzerLogFileTask(client, filePath.get(client), filePrefix.get(client)));
		}

		AnalyzerCoreMemory.TIME_POINT = DateUtil.parse(SystemConfig.START_TIME_POINT);

		AnalysisTask analysisTask = new AnalysisTask();//
		CacheClearTask cacheClearTask = new CacheClearTask();
		LoadDataTask loadDataTask = new LoadDataTask();
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		TimePointTask timePointTask = new TimePointTask();
		timePointTask.run();

		while (true) {
			long startTime = System.nanoTime();
			for (String client : clients) {
				AnalyzerLogFileTask analyzerLogFileTask = TaskCache.ANALYZER_FILE_CHACE.get(client);
				analyzerLogFileTask.run();
			}
			analysisTask.run();
			cacheClearTask.run();
			loadDataTask.run();
			sqlCacheTask.run();
			timePointTask.run();
			long endTime = System.nanoTime();
			long useTime = (endTime - startTime) / 1000000;
			MONITOR_LOGGER.info("================ 耗时[" + useTime
					+ "] ==================================================================================================");
		}

	}
}
