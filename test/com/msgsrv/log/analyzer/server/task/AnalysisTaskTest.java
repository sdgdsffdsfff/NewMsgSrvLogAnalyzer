package com.msgsrv.log.analyzer.server.task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.junit.Test;

import com.msgsrv.log.analyzer.client.entity.LogInfo;
import com.msgsrv.log.analyzer.client.task.AnalyzerLogFileTask;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.entity.StepVo;

public class AnalysisTaskTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SystemConfig.load();
		AnalyzerCoreMemory.TIME_POINT = new Date();
		
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		AnalysisTask analysisTask = new AnalysisTask();
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		
		FileInputStream input = new FileInputStream("F:/logs/OrderHandle_IFTran.log");
		InputStreamReader in = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while ((line = reader.readLine()) != null) {
			LogInfo validLog = task.isValidLog(line);
			if (validLog != null) {
				StepVo analysisLog = task.analysisLog(validLog);
				System.out.println(analysisLog);
			}
		}
		analysisTask.run();
		sqlCacheTask.run();
		reader.close();
	}
	
	@Test
	public void testOrderHandle_IFTran() throws Exception {
		SystemConfig.load();
		AnalyzerCoreMemory.TIME_POINT = new Date();
		
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		AnalysisTask analysisTask = new AnalysisTask();
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		
		FileInputStream input = new FileInputStream("F:/logs/OrderHandle_IFTran.log");
		InputStreamReader in = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while ((line = reader.readLine()) != null) {
			LogInfo validLog = task.isValidLog(line);
			if (validLog != null) {
				StepVo analysisLog = task.analysisLog(validLog);
				System.out.println(analysisLog);
			}
		}
		analysisTask.run();
		sqlCacheTask.run();
		reader.close();
	}
	
	@Test
	public void testOrderHandle_IFTran_CallBack() throws Exception {
		SystemConfig.load();
		AnalyzerCoreMemory.TIME_POINT = new Date();
		
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		AnalysisTask analysisTask = new AnalysisTask();
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		
		FileInputStream input = new FileInputStream("F:/logs/OrderHandle_IFTran_CallBack.log");
		InputStreamReader in = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while ((line = reader.readLine()) != null) {
			LogInfo validLog = task.isValidLog(line);
			if (validLog != null) {
				StepVo analysisLog = task.analysisLog(validLog);
				System.out.println(analysisLog);
			}
		}
		analysisTask.run();
		sqlCacheTask.run();
		reader.close();
	}
	
	@Test
	public void testOrderHandle_SchMaster() throws Exception {
		SystemConfig.load();
		AnalyzerCoreMemory.TIME_POINT = new Date();
		
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		AnalysisTask analysisTask = new AnalysisTask();
		SqlCacheTask sqlCacheTask = new SqlCacheTask();
		
		FileInputStream input = new FileInputStream("F:/logs/OrderHandle_SchMaster.log");
		InputStreamReader in = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(in);
		String line = null;
		while ((line = reader.readLine()) != null) {
			LogInfo validLog = task.isValidLog(line);
			if (validLog != null) {
				StepVo analysisLog = task.analysisLog(validLog);
				System.out.println(analysisLog);
			}
		}
		analysisTask.run();
		sqlCacheTask.run();
		reader.close();
	}

}
