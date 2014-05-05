/**

 * <b>包名：</b>com.msgsrv.log.analyzer.client.task<br/>
 * <b>文件名：</b>AnalysisTaskTest.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-28-下午5:13:53<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.client.task;

import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.ASS_KEYS_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.NOKEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.START_STEPS;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.containsKey;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.getAssKeys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.entity.AnluzerServerConfig;
import com.msgsrv.log.analyzer.server.entity.StepVo;
import com.msgsrv.log.analyzer.server.task.AnalysisTask;

/**
 * 
 * <b>类名称：</b>AnalysisTaskTest<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-28 下午5:13:53<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class AnalysisTaskTest {

	public static void main(String[] args) {
		AnalyzerCoreMemory.TIME_POINT = new Date();
		List<StepVo> vos = new ArrayList<StepVo>();
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "21", "20130620125256.716349", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "22", "20130620125256.716428", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "23", "20130620125256.726876", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "24", "20130620125256.726955", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "25", "20130620125256.728425", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "26", "20130620125256.728502", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "27", "20130620125256.730673", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "28", "20130620125256.730761", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "29", "20130620125256.731402", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "30", "20130620125256.731484", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "31", "20130620125256.731909", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092", "32", "20130620125256.732078", true));
		vos.add(new StepVo("NA", "33", "20130620125256.734638", false));
		vos.add(new StepVo("NA", "34", "20130620125256.734718", false));
		vos.add(new StepVo("NA", "35", "20130620125256.736965", false));
		vos.add(new StepVo("NA", "36", "20130620125256.737052", false));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092,key_2@137170397609898", "37", "20130620125256.738508", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092,key_2@137170397609898", "38", "20130620125256.738602", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092,key_2@137170397609898", "39", "20130620125256.741753", true));
		vos.add(new StepVo("key_1@1000000001_1200059501201306205606686092,key_2@137170397609898", "40", "20130620125256.741840", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "41", "20130620125256.743401", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "42", "20130620125256.743542", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "43", "20130620125256.743954", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "44", "20130620125256.744033", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "45", "20130620125256.744794", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "46", "20130620125256.744882", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "47", "20130620125256.750562", true));
		vos.add(new StepVo("key_2@137170397609898,key_1@1000000001_1200059501201306205606686092", "48", "20130620125256.750653", true));

		for (StepVo vo : vos) {
			// 如果是非KEY的步骤
			if ("NA".equals(vo.getIdentifyValue())) {
				List<StepVo> steps = NOKEY_RESULT_CACHE.get(vo.getStep());
				if (steps == null) {
					steps = new ArrayList<StepVo>();
					steps.add(vo);
					NOKEY_RESULT_CACHE.put(vo.getStep(), steps);
				} else {
					steps.add(vo);
				}
			} else {
				String[] keys = vo.getIdentifyValue().split(",");// 存在多个KEY的情况采用","分隔
				boolean isContainsStep = false;// 集合中是否包含此步骤
				for (String key : keys) {
					if (KEY_RESULT_CACHE.containsKey(key)) {
						isContainsStep = true;
						KEY_RESULT_CACHE.get(key).add(vo);
						break;
					}
				}
				// 如果在集合中找不到对应的步骤，则默认使用第一个key存储
				if (!isContainsStep) {
					List<StepVo> steps = new ArrayList<StepVo>();
					steps.add(vo);
					KEY_RESULT_CACHE.put(keys[0], steps);
				}
				// 如果存在多个key的情况，构造关联key关系
				if (keys.length > 1) {
					// 设置key之间的关联关系
					setAssociateKyes(keys);
				}

				AnluzerServerConfig config = AnluzerServerConfig.load();// 加载配置文件
				// 如果该步骤属于起始步骤
				if (config.getStartSteps().contains(vo.getStep())) {
					START_STEPS.add(vo);
				}
			}
		}

		AnalysisTask task = new AnalysisTask();
		task.run();
	}

	// 设置关联KEY
	public static void setAssociateKyes(String[] keys) {
		if (keys.length > 1) {
			for (String key : keys) {
				if (!containsKey(key)) {
					Set<String> associate_keys = new HashSet<String>();
					for (String key2 : keys) {
						if (!key.equals(key2)) {
							associate_keys.add(key2);
						}
					}
					ASS_KEYS_CACHE.put(key, associate_keys);
				}
			}
			Set<String> keySet = new HashSet<String>();
			for (String key : keys) {
				keySet.add(key);
				if (containsKey(key)) {
					keySet.addAll(getAssKeys(key));
				}
			}
			for (String key : keySet) {
				if (containsKey(key)) {
					getAssKeys(key).addAll(keySet);
					getAssKeys(key).remove(key);
				}
			}
		}
	}
}
