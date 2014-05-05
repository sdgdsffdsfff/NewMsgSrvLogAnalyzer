/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.task<br/>
 * <b>文件名：</b>CacheClearTask.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-7-24-下午7:30:54<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.task;

import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.MULTI_KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.NOKEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.START_STEPS;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.TIME_POINT;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.SELECTABLE_RESULT_CACHE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.entity.StepVo;
import com.msgsrv.log.analyzer.server.service.AnalysisResultService;
import com.msgsrv.log.analyzer.server.service.impl.AnalysisResultServiceImpl;

/**
 * 
 * <b>类名称：</b>CacheClearTask<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-7-24 下午7:30:54<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class CacheClearTask implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(CacheClearTask.class);
	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	private AnalysisResultService service = new AnalysisResultServiceImpl();

	@Override
	public void run() {
		long startTime = System.nanoTime();
		LOGGER.info("开始执行缓存清理任务.....");
		try {
			// 清除有KEY的步骤缓存
			Iterator<String> iterator = KEY_RESULT_CACHE.keySet().iterator();

			Set<String> removeKeys = new HashSet<String>();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if (KEY_RESULT_CACHE.get(key).isEmpty()) {
					// iterator.remove();
					// KEY_RESULT_CACHE.keySet().remove(key);
					// clearAssociateKeys(key);
					// ASSOCIATE_KEYS_CACHE.remove(key);
					removeKeys.add(key);
				} else {
					List<StepVo> steps = KEY_RESULT_CACHE.get(key);
					if (steps == null) {
						continue;
					}
					// steps = Collections.synchronizedList(steps);
					// synchronized (steps) {
					Iterator<StepVo> stepVoIterator = steps.iterator();
					List<StepVo> removeStepVos = new ArrayList<StepVo>();
					while (stepVoIterator.hasNext()) {
						StepVo stepVo = stepVoIterator.next();
						if (stepVo == null) {
							continue;
						}
						
						if (stepVo.getTimeout() != 0) {
							long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
							if (timeout < stepVo.getTimeout()) {
								continue;
							}
						}
						
						long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
						if (timeout > (SystemConfig.RESULT_TIMEOUT)) {
							LOGGER.info("步骤：" + stepVo + "超时清出缓存,当前分析时间[" + DateUtil.format(TIME_POINT) + "]");
							// 如果它是slave，将超时的信息发送到MASTER中进行分析
							service.timeout(stepVo);
							// steps.remove(stepVo);
							// stepVoIterator.remove();
							removeStepVos.add(stepVo);
						}
					}
					steps.removeAll(removeStepVos);
					if (steps.isEmpty()) {
						removeKeys.add(key);
					}
					// }
				}
			}

			KEY_RESULT_CACHE.keySet().removeAll(removeKeys);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		try {
			// 清除非MULTI_KEY的步骤缓存
			Iterator<String> it = MULTI_KEY_RESULT_CACHE.keySet().iterator();
			Set<String> removeKeys = new HashSet<String>();

			while (it.hasNext()) {
				String key = it.next();
				if (MULTI_KEY_RESULT_CACHE.get(key).isEmpty()) {
					removeKeys.add(key);
				} else {
					List<StepVo> steps = MULTI_KEY_RESULT_CACHE.get(key);
					if (steps == null || steps.isEmpty()) {
						removeKeys.add(key);
						continue;
					}

					Iterator<StepVo> stepVoIterator = steps.iterator();
					List<StepVo> removeStepVos = new ArrayList<StepVo>();
					while (stepVoIterator.hasNext()) {
						StepVo stepVo = stepVoIterator.next();
						if (stepVo == null) {
							continue;
						}
						long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
						if (timeout > SystemConfig.RESULT_TIMEOUT) {
							// logger.info("步骤：" + stepVo + "超时清除缓存,当前分析时间[" +
							// DateUtil.format(TIME_POINT) + "]");
							removeStepVos.add(stepVo);
							service.timeout(stepVo);
						} else {
							break;
						}
					}
					steps.removeAll(removeStepVos);
					if (steps.isEmpty()) {
						removeKeys.add(key);
					}
				}
			}
			MULTI_KEY_RESULT_CACHE.keySet().retainAll(removeKeys);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		try {
			// 清除非KEY的步骤缓存
			Iterator<String> it = NOKEY_RESULT_CACHE.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<StepVo> steps = NOKEY_RESULT_CACHE.get(key);
				Iterator<StepVo> stepVoIterator = steps.iterator();
				List<StepVo> removeStepVos = new ArrayList<StepVo>();
				while (stepVoIterator.hasNext()) {
					StepVo stepVo = stepVoIterator.next();
					if (stepVo == null) {
						continue;
					}
					long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
					if (timeout > SystemConfig.RESULT_TIMEOUT) {
						// logger.info("步骤：" + stepVo + "超时清除缓存,当前分析时间[" +
						// DateUtil.format(TIME_POINT) + "]");
						// 如果它是slave，将超时的信息发送到MASTER中进行分析
						// stepVoIterator.remove();
						removeStepVos.add(stepVo);
						service.timeout(stepVo);
					} else {
						break;
					}
				}
				steps.removeAll(removeStepVos);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		try {
			// 清除起始步骤超时的缓存信息
			Iterator<StepVo> ite = START_STEPS.iterator();

			List<StepVo> removeStepVos = new ArrayList<StepVo>();

			while (ite.hasNext()) {
				StepVo stepVo = ite.next();
				if (stepVo == null) {
					continue;
				}
				if (stepVo.getTimeout() != 0) {
					long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
					if (timeout < stepVo.getTimeout()) {
						continue;
					}
				}
				long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
				if (timeout > SystemConfig.RESULT_TIMEOUT) {
					LOGGER.info("步骤：" + stepVo + "超时清除缓存,当前分析时间[" + DateUtil.format(TIME_POINT) + "]");
					// START_STEPS.remove(stepVo);
					removeStepVos.add(stepVo);
					// 清除关联关系
					// clearAssociateKeys(stepVo.getIdentifyValue().split(",")[0]);
					// 如果它是slave，将超时的信息发送到MASTER中进行分析
					service.timeout(stepVo);
				} else {
					break;
				}
			}

			START_STEPS.removeAll(removeStepVos);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		// 清除非必须步骤
		try {
			Iterator<StepVo> iterator = SELECTABLE_RESULT_CACHE.iterator();
			while (iterator.hasNext()) {
				StepVo stepVo = iterator.next();
				long timeout = TIME_POINT.getTime() - stepVo.getOccDateTime();
				if (timeout > SystemConfig.RESULT_TIMEOUT) {
					// LOGGER.info("步骤：" + stepVo + "超时清除缓存,当前分析时间[" +
					// DateUtil.format(TIME_POINT) + "]");
					iterator.remove();
					service.timeout(stepVo);
				} else {
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		long endTime = System.nanoTime();
		long useTime = (endTime - startTime) / 1000000;
		MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] [缓存清理执行完毕]");
	}
}
