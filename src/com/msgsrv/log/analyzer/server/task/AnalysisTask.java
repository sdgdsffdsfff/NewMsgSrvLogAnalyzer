/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.task<br/>
 * <b>文件名：</b>AnalysisTask.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-7-18-上午10:39:37<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.task;

import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.MULTI_KEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.NOKEY_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.SELECTABLE_RESULT_CACHE;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.START_STEPS;
import static com.msgsrv.log.analyzer.core.AnalyzerCoreMemory.getAssKeys;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.common.StringUtil;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.entity.AnluzerServerConfig;
import com.msgsrv.log.analyzer.server.entity.OrderTimeLogVo;
import com.msgsrv.log.analyzer.server.entity.OrderTreeConfig;
import com.msgsrv.log.analyzer.server.entity.PathConfig;
import com.msgsrv.log.analyzer.server.entity.SelectableStepGroupConfig;
import com.msgsrv.log.analyzer.server.entity.StepVo;
import com.msgsrv.log.analyzer.server.service.AnalysisResultService;
import com.msgsrv.log.analyzer.server.service.impl.AnalysisResultServiceImpl;

/**
 * 
 * <b>类名称：</b>AnalysisTask<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-7-18 上午10:39:37<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class AnalysisTask implements Runnable {

	private static final Logger logger = Logger.getLogger(AnalysisTask.class);
	private static final Logger MONITOR_LOGGER = Logger.getLogger("monitor");

	private AnalysisResultService analysisResultService = new AnalysisResultServiceImpl();

	private int counter = 0;

	private static final List<String> CHARGE_LIST_STEPS = new ArrayList<String>();// 通过
																					// charge_list.id
																					// 关联起来的步骤信息
	static {
		CHARGE_LIST_STEPS.add("112");
		CHARGE_LIST_STEPS.add("113");
		CHARGE_LIST_STEPS.add("114");
		CHARGE_LIST_STEPS.add("115");
		CHARGE_LIST_STEPS.add("116");
		CHARGE_LIST_STEPS.add("117");
	}

	@Override
	public void run() {
		long startTime = System.nanoTime();
		try {
			analysis();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
		long endTime = System.nanoTime();
		long useTime = (endTime - startTime) / 1000000;
		MONITOR_LOGGER.info("[" + DateUtil.format(AnalyzerCoreMemory.TIME_POINT) + "] 耗时[" + useTime + "] 分析完毕的订单数量 [" + counter + "]");
		counter = 0;
	}

	// 从起始步骤开始进行分析
	private void analysis() {
		if (START_STEPS.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("缓存中不存在需要分析的起始步骤信息....");
			}
			return;
		}
		// 先对起始路径下的步骤进行排序，从时间最小的步骤开始进行搜寻
		if (logger.isDebugEnabled()) {
			logger.debug("开始从起始步骤进行结果的分析...");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("缓存中的起始步骤有：" + START_STEPS);
		}
		AnluzerServerConfig config = AnluzerServerConfig.load();// 加载配置文件

		Iterator<StepVo> iterator = START_STEPS.iterator();
		while (iterator.hasNext()) {
			StepVo startStep = iterator.next();
			if (startStep.getAnalyzeTimeout() != 0) {
				long timeout = AnalyzerCoreMemory.TIME_POINT.getTime() - startStep.getOccDateTime();
				if (timeout < startStep.getAnalyzeTimeout()) {
					continue;
				}
			}
			Set<String> multiKeys = new HashSet<String>();// 存储multiKey
			multiKeys.addAll(startStep.getMultiKeys());

			PathConfig stepPath = config.getStartStepPath(startStep.getStep());

			if (stepPath == null) {
				logger.info("[" + startStep + "]不是起始步骤信息");
				continue;
			}

			String identifyValue = startStep.getIdentifyValue().split(",")[0];

			// 得到它所关联的KEY
			Set<String> associateKeys = getAssKeys(identifyValue);
			// 路径之间的分隔符采用","分隔开来
			String[] stepSets = stepPath.getKeyStepSets();// 路径之间的分隔符采用","分隔开来
			Set<StepVo> findStepVos = new HashSet<StepVo>();// 记录通过关联关系查找的步骤信息

			if (KEY_RESULT_CACHE.get(identifyValue) != null) {
				findStepVos.addAll(KEY_RESULT_CACHE.get(identifyValue));
			}
			if (associateKeys != null) {
				Iterator<String> it = associateKeys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					if (KEY_RESULT_CACHE.get(key) != null) {
						findStepVos.addAll(KEY_RESULT_CACHE.get(key));
					}
				}
			} else {
				associateKeys = Collections.emptySet();
			}

			// 对处理耗时的订单单独分析
			if ("100".equals(startStep.getStep())) {
				analyzeHandle(findStepVos, stepPath);
				continue;
			}

			// 逐步查询每个步骤是否都是查找齐全的
			boolean matchComplete = true;
			Map<String, StepVo> matchCompleteSteps = new HashMap<String, StepVo>();
			matchCompleteSteps.put(startStep.getStep(), startStep);
			if (findStepVos.size() >= stepSets.length) {
				for (int i = 1; i < stepSets.length; i++) {
					String step = stepSets[i];
					StepVo vo = existSteps(step, findStepVos);
					if (vo == null) {// 得到符合的步骤集合
						matchComplete = false;
						break;
					}
					multiKeys.addAll(vo.getMultiKeys());
					matchCompleteSteps.put(step, vo);
				}
			}
			if (!matchComplete) {
				continue;
			} else {
				List<StepVo> vos = KEY_RESULT_CACHE.get(identifyValue);
				if (vos != null) {
					vos.removeAll(matchCompleteSteps.values());
					if (vos.isEmpty()) {
						KEY_RESULT_CACHE.remove(identifyValue);
					}
				}
				Iterator<String> it = associateKeys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					List<StepVo> steps = KEY_RESULT_CACHE.get(key);
					if (steps != null) {
						steps.removeAll(matchCompleteSteps.values());
						if (steps.isEmpty()) {
							it.remove();
						}
					}
				}
			}

			// 如果不需要分析 非KEY环节
			if (StringUtil.isEmpty(stepPath.getNoKeyStepSet())) {
				Collection<StepVo> values = matchCompleteSteps.values();
				if (doCheck(stepPath, values)) {
					iterator.remove();// 移除开始步骤
					counter++;
					analysisResultService.analysisResult(stepPath, values);
					continue;
				}
			} else {
				// String[] keySteps = stepPath.getKeyStepSets();

				// String[] noKeySteps = stepPath.getNoKeyStepSets();
				List<String> allSteps = stepPath.getAllKeySteps();

				// for (String step : keySteps) {
				// allSteps.add(step);
				// }
				// for (String step : noKeySteps) {
				// allSteps.add(step);
				// }

				// 得到所有能关联到的非KEY步骤信息
				boolean isFindAllNoKeyStep = true;
				for (String step : allSteps) {
					// 如果有一个步骤还未查关联到此路径上
					if (!matchCompleteSteps.containsKey(step)) {

						StepVo frontStep = findFrontStep(step, allSteps, matchCompleteSteps);// 查询它的前一个可以匹配到的步骤
						StepVo nextStep = findNextStep(step, allSteps, matchCompleteSteps);// 查询它的后一个可以匹配到的步骤

						if (frontStep == null || nextStep == null) {
							isFindAllNoKeyStep = false;
							break;
						}

						// 先从多值KEY 集合中寻找，如果找到了，就不在无KEY的集合中寻找了
						boolean findMultiKey = false;
						if (multiKeys != null) {
							for (String multiKey : multiKeys) {
								List<StepVo> vos = MULTI_KEY_RESULT_CACHE.get(multiKey);
								if (vos != null) {
									Collections.sort(vos);
									List<StepVo> centreStep = findCentreStep(frontStep, nextStep, vos);
									if (centreStep.size() == 1) {
										StepVo fitStep = centreStep.get(0);
										vos.remove(fitStep);
										matchCompleteSteps.put(step, fitStep);
										findMultiKey = true;
										break;
									}
									if (centreStep.size() > 1) {
										// StepVo vo =
										// getCentreStep(centreStep);
										StepVo vo = centreStep.get(0);// 采用第一个
										vos.remove(vo);
										matchCompleteSteps.put(step, vo);
										findMultiKey = true;
										break;
									}
								}
							}
						}
						// 如果从MultiKey集合中得到了所需要的步骤信息
						if (findMultiKey) {
							break;
						}
						List<StepVo> noKeyStepList = NOKEY_RESULT_CACHE.get(step);// 得到此步骤的集合
						if (noKeyStepList != null) {
							Collections.sort(noKeyStepList);
							List<StepVo> centreStep = findCentreStep(frontStep, nextStep, noKeyStepList);
							if (centreStep.size() == 1) {
								StepVo fitStep = centreStep.get(0);
								noKeyStepList.remove(fitStep);
								matchCompleteSteps.put(step, fitStep);
							} else if (centreStep.size() > 1) {
								// StepVo vo = getCentreStep(centreStep);
								StepVo vo = centreStep.get(0);// 采用第一个
								noKeyStepList.remove(vo);
								matchCompleteSteps.put(step, vo);
							} else {
								StepVo vo = new StepVo("NA", step, frontStep.getOccTime(), false);
								matchCompleteSteps.put(step, vo);
							}
						} else {
							isFindAllNoKeyStep = false;
						}
					}
				}

				List<OrderTimeLogVo> orderTimeLogVos = new ArrayList<OrderTimeLogVo>();

				if (isFindAllNoKeyStep) {
					Collection<StepVo> values = matchCompleteSteps.values();
					if (doCheck(stepPath, values)) {

						// 查找这条路径所能关联到的所有非必要结束步骤
						List<SelectableStepGroupConfig> selectableStepGroups = stepPath.getSelectableStepGroups();
						if (!selectableStepGroups.isEmpty()) {
							Collections.sort(SELECTABLE_RESULT_CACHE);
							List<StepVo> selectableVos = SELECTABLE_RESULT_CACHE;
							Set<String> eigenValueSet = new HashSet<String>();

							for (SelectableStepGroupConfig ssgc : selectableStepGroups) {
								Map<String, StepVo> machStep = new HashMap<String, StepVo>();

								List<String> steps = ssgc.getSteps();
								String before = ssgc.getBefore();
								String last = ssgc.getLast();
								StepVo frontStep = matchCompleteSteps.get(before);
								StepVo nextStep = matchCompleteSteps.get(last);
								Map<String, List<StepVo>> centreSteps = findCentreStep(steps, frontStep, nextStep, selectableVos);// 得到符合的步骤信息

								String eigenValue = frontStep.getEigenValue();
								if (StringUtil.isNotEmpty(eigenValue)) {
									eigenValueSet.add(eigenValue);
								}
								StepVo firstGroupStep = null;
								if (!eigenValueSet.isEmpty()) {
									String step = steps.get(0);
									List<StepVo> vos = centreSteps.get(step);
									for (StepVo sv : vos) {
										if (eigenValueSet.contains(sv.getEigenValue())) {
											firstGroupStep = sv;
											if (StringUtil.isNotEmpty(firstGroupStep.getEigenValue())) {
												eigenValueSet.add(firstGroupStep.getEigenValue());
											}
											machStep.put(step, firstGroupStep);
											selectableVos.remove(firstGroupStep);
											break;
										}
									}
								}

								if (firstGroupStep == null) {
									String step = steps.get(0);
									List<StepVo> vos = centreSteps.get(steps.get(0));
									if (!vos.isEmpty()) {
										firstGroupStep = vos.get(0);
										machStep.put(step, firstGroupStep);
										selectableVos.remove(firstGroupStep);
										if (StringUtil.isNotEmpty(firstGroupStep.getEigenValue())) {
											eigenValueSet.add(firstGroupStep.getEigenValue());
										}
									}
								}

								if (firstGroupStep == null) {
									continue;
								}

								for (int i = 1; i < steps.size(); i++) {
									String step = steps.get(i);
									List<StepVo> vos = centreSteps.get(steps.get(i));
									for (StepVo stepVo : vos) {
										if (stepVo.getSn().equals(firstGroupStep.getSn())) {
											StepVo nextGroupStep = stepVo;
											if (StringUtil.isNotEmpty(nextGroupStep.getEigenValue())) {
												eigenValueSet.add(nextGroupStep.getEigenValue());
											}
											machStep.put(step, nextGroupStep);
											selectableVos.remove(nextGroupStep);
											break;
										}
									}
								}

								// 构建耗时分析报告
								List<OrderTreeConfig> orderTrees = ssgc.getOrderTrees();
								for (OrderTreeConfig otc : orderTrees) {
									String[] orderIndexs = otc.getOrderIndexs();
									OrderTimeLogVo vo = new OrderTimeLogVo();
									vo.setIdentifyValue(identifyValue);
									vo.setOrderIndex(otc.getId());
									vo.setContent(otc.getContent());

									// 设置上级环节
									vo.setParentId(0);
									// 设置环节耗时
									{
										StepVo beginStepVo = machStep.get(orderIndexs[0]);
										StepVo endStepVo = machStep.get(orderIndexs[1]);
										if (beginStepVo == null || endStepVo == null) {
											continue;
										}
										vo.setBeginTime(beginStepVo.getOccTime());
										vo.setEndTime(endStepVo.getOccTime());
										String endOccTime = endStepVo.getOccTime();
										String beginOccTime = beginStepVo.getOccTime();

										String[] s1 = endOccTime.split("\\.");
										String[] s2 = beginOccTime.split("\\.");
										try {
											Date endOccDate = DateUtil.parse(s1[0]);
											Date beginOccDate = DateUtil.parse(s2[0]);

											int endOcc = Integer.parseInt(s1[1]);
											int beginOcc = Integer.parseInt(s2[1]);

											long useTime = (endOccDate.getTime() - beginOccDate.getTime()) * 1000 + (endOcc - beginOcc);
											vo.setUseTime(useTime);
										} catch (Exception e) {
											logger.error(e.getMessage(), e);
										}
									}

									// 需要开始步骤和结束步骤都要是精确的，此环节才算是精确的
									vo.setExactFlag(0);

									// 设置自耗时
									{
										String[] selfUseTimes = otc.getSelfUseTimes();
										StepVo beginStepVo = machStep.get(selfUseTimes[0]);
										StepVo endStepVo = machStep.get(selfUseTimes[1]);
										vo.setBeginTime(beginStepVo.getOccTime());
										vo.setEndTime(endStepVo.getOccTime());
										String endOccTime = endStepVo.getOccTime();
										String beginOccTime = beginStepVo.getOccTime();

										String[] s1 = endOccTime.split("\\.");
										String[] s2 = beginOccTime.split("\\.");
										try {
											Date endOccDate = DateUtil.parse(s1[0]);
											Date beginOccDate = DateUtil.parse(s2[0]);

											int endOcc = Integer.parseInt(s1[1]);
											int beginOcc = Integer.parseInt(s2[1]);

											long useTime = (endOccDate.getTime() - beginOccDate.getTime()) * 1000 + (endOcc - beginOcc);
											vo.setSelfUseTime(useTime);
										} catch (Exception e) {
											logger.error(e.getMessage(), e);
										}
									}
									vo.setPid(otc.getPid());
									orderTimeLogVos.add(vo);
								}
							}
						}

						iterator.remove();// 移除开始步骤
						// 移除关键步骤
						// Iterator<String> it = associateKeys.iterator();
						// while (it.hasNext()) {
						// String key = it.next();
						// List<StepVo> vos = KEY_RESULT_CACHE.get(key);
						// if (vos != null) {
						// vos.removeAll(values);
						// }
						// }
						// List<StepVo> list =
						// KEY_RESULT_CACHE.get(identifyValue);
						// if (list != null) {
						// list.removeAll(values);
						// }
						counter++;
						analysisResultService.analysisResult(stepPath, values, orderTimeLogVos);
					}
				}
			}
		}
	}

	private boolean doCheck(PathConfig stepPath, Collection<StepVo> stepVos) {
		boolean result = true;
		Map<String, StepVo> stepMaps = new HashMap<String, StepVo>();
		for (StepVo stepVo : stepVos) {
			stepMaps.put(stepVo.getStep(), stepVo);
		}

		List<OrderTreeConfig> orderTrees = stepPath.getOrderTrees();
		Set<String> essentialSteps = stepPath.getEssentialSteps();

		for (OrderTreeConfig otc : orderTrees) {
			String[] split = otc.getOrderIndex().split(",");
			if (!essentialSteps.contains(split[0]) || !essentialSteps.contains(split[1])) {
				continue;
			}
			StepVo beginStepVo = stepMaps.get(split[0]);
			StepVo endStepVo = stepMaps.get(split[1]);
			if (beginStepVo == null || endStepVo == null) {
				return false;
			}
			if (beginStepVo.getOccTime().compareTo(endStepVo.getOccTime()) > 0) {
				return false;
			}
		}
		return result;
	}

	private StepVo findNextStep(String step, List<String> allSteps, Map<String, StepVo> matchCompleteSteps) {
		int site = Integer.parseInt(step);
		for (int i = 0; i < allSteps.size(); i++) {
			int index = Integer.parseInt(allSteps.get(i));
			if (index > site && matchCompleteSteps.get(allSteps.get(i)) != null) {
				return matchCompleteSteps.get(allSteps.get(i));
			}
		}
		return null;
	}

	private StepVo findFrontStep(String step, List<String> allSteps, Map<String, StepVo> matchCompleteSteps) {
		for (int i = 0; i < allSteps.size(); i++) {
			if (step.equals(allSteps.get(i))) {
				return matchCompleteSteps.get(allSteps.get(i - 1));
			}
		}
		return null;
	}

	// TODO 暂时弃用查找中间步骤的规则
	// 查找起始步骤和结束步骤之间的合适步骤
	@SuppressWarnings({ "unused" })
	private StepVo getCentreStep(List<StepVo> centreStep) {
		StepVo step = null;
		String occTime = null;
		long prefix = 0;
		String prefixStr = null;
		long suffix = 0;
		String suffixStr = null;
		for (StepVo vo : centreStep) {
			if (step == null) {
				step = new StepVo(vo.getIdentifyValue(), vo.getStep(), occTime, false);
			}
			String[] split = vo.getOccTime().split("\\.");
			try {
				prefix = prefix + DateUtil.parse(split[0]).getTime();
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
			suffix = suffix + Long.parseLong(split[1]);
		}
		prefixStr = DateUtil.format(new Date(prefix / centreStep.size()));
		suffixStr = suffix / centreStep.size() + "";
		if (suffixStr.length() > 6) {
			suffixStr = suffixStr.substring(0, 6);
		} else {
			int length = suffixStr.length();
			for (int i = length; i < 6; i++) {
				suffixStr = "0" + suffixStr;
			}
		}
		occTime = prefixStr + "." + suffixStr;
		step.setOccTime(occTime);
		return step;
	}

	// 查找起始步骤和结束步骤之间的合适步骤
	private List<StepVo> findCentreStep(StepVo startStep, StepVo nextStep, List<StepVo> fitSteps) {
		List<StepVo> steps = new ArrayList<StepVo>();
		if (startStep != null && nextStep != null && fitSteps != null) {
			try {
				long startTime = startStep.getOccDateMicrosecond();
				long endTime = nextStep.getOccDateMicrosecond();
				for (StepVo vo : fitSteps) {
					long occDateTime = vo.getOccDateMicrosecond();
					if (occDateTime >= startTime && occDateTime <= endTime) {
						steps.add(vo);
					} else if (occDateTime > endTime) {
						break;
					}
				}
			} catch (Exception e) {

			}
		}
		return steps;
	}

	// 查找起始步骤和结束步骤之间的合适步骤
	private Map<String, List<StepVo>> findCentreStep(List<String> steps, StepVo startStep, StepVo nextStep, List<StepVo> fitSteps) {
		Map<String, List<StepVo>> centreSteps = new HashMap<String, List<StepVo>>();
		for (String step : steps) {
			List<StepVo> stepList = new ArrayList<StepVo>();
			centreSteps.put(step, stepList);
		}

		if (startStep != null && nextStep != null && fitSteps != null) {
			try {
				long startTime = startStep.getOccDateMicrosecond();
				long endTime = nextStep.getOccDateMicrosecond();
				for (StepVo vo : fitSteps) {
					long occDateTime = vo.getOccDateMicrosecond();
					if (occDateTime >= startTime && occDateTime <= endTime) {
						if (steps.contains(vo.getStep())) {
							centreSteps.get(vo.getStep()).add(vo);
						}
					} else if (occDateTime > endTime) {
						break;
					}
				}
			} catch (Exception e) {

			}
		}
		return centreSteps;
	}

	// 查询步骤是否存在
	public StepVo existSteps(String step, Set<StepVo> findStepVos) {
		StepVo result = null;
		for (StepVo sv : findStepVos) {
			if (sv != null && sv.getStep().equals(step)) {
				result = sv;
				break;
			}
		}
		return result;
	}

	private void analyzeHandle(Set<StepVo> findStepVos, PathConfig stepPath) {
		List<StepVo> stepVos = new ArrayList<StepVo>();
		stepVos.addAll(findStepVos);
		Collections.sort(stepVos);
		Map<String, List<StepVo>> stepsMapping = new HashMap<String, List<StepVo>>();
		for (StepVo sv : stepVos) {
			if (!stepsMapping.containsKey(sv.getStep())) {
				List<StepVo> vos = new ArrayList<StepVo>();
				vos.add(sv);
				stepsMapping.put(sv.getStep(), vos);
			} else {
				stepsMapping.get(sv.getStep()).add(sv);
			}
		}

		List<StepVo> matchSteps = new ArrayList<StepVo>();
		Map<String, StepVo> iftranMapping = new HashMap<String, StepVo>();
		Map<String, StepVo> callbackMapping = new HashMap<String, StepVo>();

		// 必须存在OrderKeep - > Broker2的步骤信息
		if (stepsMapping.containsKey("100") && stepsMapping.containsKey("101")) {
			// 如果存在Broker2 - > SchMaster的环节
			// Broker2 -> SchMaster NewTask 30977027 22139497040362669
			// 22139497041304884|
			StepVo step100 = stepsMapping.get("100").get(0);
			StepVo step101 = stepsMapping.get("101").get(0);

			matchSteps.add(step100);
			matchSteps.add(step101);

			String identifyValue = step100.getIdentifyValue().split(",")[1].substring(7);
			List<OrderTimeLogVo> indexs = new ArrayList<OrderTimeLogVo>();

			// 将SchMaster中的一些步骤信息关联到Broker2上去。
			// 将通过IFTran转发的步骤信息关联到Broker2上来
			// CallBack环节并不一定会包含，但是可能存在多次IFTran请求CallBack的情况。
			boolean existIFTran = false;
			if (stepsMapping.containsKey("102") && stepsMapping.containsKey("103") && stepsMapping.containsKey("104") && stepsMapping.containsKey("105")) {
				existIFTran = true;
				StepVo step102 = stepsMapping.get("102").get(0);
				StepVo step103 = stepsMapping.get("103").get(0);
				StepVo step104 = stepsMapping.get("104").get(0);
				StepVo step105 = stepsMapping.get("105").get(0);

				matchSteps.add(step102);
				matchSteps.add(step103);
				matchSteps.add(step104);
				matchSteps.add(step105);

				iftranMapping.put("102", step102);
				iftranMapping.put("103", step103);
				iftranMapping.put("104", step104);
				iftranMapping.put("105", step105);

			}

			boolean existCallback = false;
			if (existIFTran && stepsMapping.containsKey("106") && stepsMapping.containsKey("107") && stepsMapping.containsKey("108")
					&& stepsMapping.containsKey("109")) {
				existCallback = true;
				StepVo step106 = stepsMapping.get("106").get(0);
				StepVo step107 = stepsMapping.get("107").get(0);
				StepVo step108 = stepsMapping.get("108").get(0);
				StepVo step109 = stepsMapping.get("109").get(0);

				matchSteps.add(step106);
				matchSteps.add(step107);
				matchSteps.add(step108);
				matchSteps.add(step109);

				callbackMapping.put("106", step106);
				callbackMapping.put("107", step107);
				callbackMapping.put("108", step108);
				callbackMapping.put("109", step109);
			}

			if (stepsMapping.containsKey("111")) {
				List<StepVo> schMasters = stepsMapping.get("111");
				List<Map<String, StepVo>> schMasterSteps = new ArrayList<Map<String, StepVo>>();
				for (StepVo stepVo : schMasters) {
					Map<String, StepVo> vos = new HashMap<String, StepVo>();
					String chargeListId = stepVo.getSn();
					vos.put(stepVo.getStep(), stepVo);
					schMasterSteps.add(vos);

					matchSteps.add(stepVo);

					for (String step : CHARGE_LIST_STEPS) {
						List<StepVo> steps = stepsMapping.get(step);
						StepVo vo = find(steps, chargeListId);
						if (vo == null) {
							break;
						}
						vos.put(vo.getStep(), vo);
						matchSteps.add(vo);
					}

				}

				if (!schMasterSteps.isEmpty()) {
					int groupId = 1;
					for (Map<String, StepVo> schMasterIndexs : schMasterSteps) {
						// 如果存在SchMaster 的环节
						if (schMasterIndexs.containsKey("111") && schMasterIndexs.containsKey("112")) {
							OrderTimeLogVo schMasterIndex = new OrderTimeLogVo();
							schMasterIndex.setIdentifyValue(identifyValue);
							schMasterIndex.setOrderIndex("3");
							schMasterIndex.setBeginTime(schMasterIndexs.get("111").getOccTime());
							schMasterIndex.setEndTime(schMasterIndexs.get("112").getOccTime());
							schMasterIndex.setUseTime(schMasterIndexs.get("112").getOccDateMicrosecond() - schMasterIndexs.get("111").getOccDateMicrosecond());
							schMasterIndex.setSelfUseTime(schMasterIndex.getUseTime());
							schMasterIndex.setParentId(groupId);

							indexs.add(schMasterIndex);

							// 如果存在MsgDispatcher环节
							if (schMasterIndexs.containsKey("113") && schMasterIndexs.containsKey("114")) {
								OrderTimeLogVo msgDispatcherIndex = new OrderTimeLogVo();
								msgDispatcherIndex.setIdentifyValue(identifyValue);
								msgDispatcherIndex.setOrderIndex("4");
								msgDispatcherIndex.setBeginTime(schMasterIndexs.get("113").getOccTime());
								msgDispatcherIndex.setEndTime(schMasterIndexs.get("114").getOccTime());
								msgDispatcherIndex.setUseTime(schMasterIndexs.get("114").getOccDateMicrosecond()
										- schMasterIndexs.get("113").getOccDateMicrosecond());
								msgDispatcherIndex.setSelfUseTime(msgDispatcherIndex.getUseTime());
								msgDispatcherIndex.setParentId(groupId);

								indexs.add(msgDispatcherIndex);

								// 如果存在RcgRst环节
								if (schMasterIndexs.containsKey("115") && schMasterIndexs.containsKey("116")) {
									OrderTimeLogVo rcgRstIndex = new OrderTimeLogVo();
									rcgRstIndex.setIdentifyValue(identifyValue);
									rcgRstIndex.setOrderIndex("5");
									rcgRstIndex.setBeginTime(schMasterIndexs.get("115").getOccTime());
									rcgRstIndex.setEndTime(schMasterIndexs.get("116").getOccTime());
									rcgRstIndex.setUseTime(schMasterIndexs.get("116").getOccDateMicrosecond()
											- schMasterIndexs.get("115").getOccDateMicrosecond());
									rcgRstIndex.setSelfUseTime(rcgRstIndex.getUseTime());
									rcgRstIndex.setParentId(groupId);

									indexs.add(rcgRstIndex);

								}

								// 如果存在callback环节
								if (schMasterIndexs.containsKey("117")) {
									StepVo stepVo111 = schMasterIndexs.get("111");
									String keys = stepVo111.getIdentifyValue();
									Set<String> keySets = new HashSet<String>();
									String[] ks = keys.split(",");
									for (int i = 1; i < ks.length; i++) {
										keySets.add(ks[1]);
									}
									StepVo step117 = schMasterIndexs.get("117");
									StepVo step118 = null;
									StepVo step119 = null;

									List<StepVo> steps118 = stepsMapping.get("118");
									for (StepVo stepVo : steps118) {
										if (keySets.contains(stepVo.getIdentifyValue())) {
											step118 = stepVo;
											break;
										}
									}

									List<StepVo> steps119 = stepsMapping.get("119");
									for (StepVo stepVo : steps119) {
										if (keySets.contains(stepVo.getIdentifyValue())) {
											step119 = stepVo;
											break;
										}
									}

									// 是否存在callback环节
									if (step118 != null) {
									
										matchSteps.add(step118);
										
										OrderTimeLogVo callbackIndex = new OrderTimeLogVo();
										callbackIndex.setIdentifyValue(identifyValue);
										callbackIndex.setOrderIndex("6");
										callbackIndex.setBeginTime(step117.getOccTime());
										callbackIndex.setEndTime(step118.getOccTime());
										callbackIndex.setUseTime(step118.getOccDateMicrosecond() - step117.getOccDateMicrosecond());
										callbackIndex.setSelfUseTime(callbackIndex.getUseTime());
										callbackIndex.setParentId(groupId);

										indexs.add(callbackIndex);

										if (step119 != null) {

											matchSteps.add(step119);
											
											OrderTimeLogVo httpCallerIndex = new OrderTimeLogVo();
											httpCallerIndex.setIdentifyValue(identifyValue);
											httpCallerIndex.setOrderIndex("7");
											httpCallerIndex.setBeginTime(step119.getOccTime());
											httpCallerIndex.setEndTime(step119.getOccTime());
											httpCallerIndex.setUseTime(0);
											httpCallerIndex.setSelfUseTime(0);
											httpCallerIndex.setParentId(groupId);

											indexs.add(httpCallerIndex);

										}

									} else {
										OrderTimeLogVo callbackIndex = new OrderTimeLogVo();
										callbackIndex.setIdentifyValue(identifyValue);
										callbackIndex.setOrderIndex("6");
										callbackIndex.setBeginTime(step117.getOccTime());
										callbackIndex.setEndTime(step117.getOccTime());
										callbackIndex.setUseTime(0);
										callbackIndex.setSelfUseTime(0);
										callbackIndex.setParentId(groupId);

										indexs.add(callbackIndex);
									}
								}
							}
						}
						groupId++;
					}
				}
			}

			Collections.sort(matchSteps);
			StepVo lastStepVo = matchSteps.get(matchSteps.size() - 1);

			OrderTimeLogVo orderKeepIndex = new OrderTimeLogVo();
			orderKeepIndex.setIdentifyValue(identifyValue);
			orderKeepIndex.setOrderIndex("1");
			orderKeepIndex.setBeginTime(step100.getOccTime());
			orderKeepIndex.setEndTime(lastStepVo.getOccTime());
			orderKeepIndex.setUseTime(lastStepVo.getOccDateMicrosecond() - step100.getOccDateMicrosecond());
			orderKeepIndex.setSelfUseTime(0);

			OrderTimeLogVo broke2Index = new OrderTimeLogVo();
			broke2Index.setIdentifyValue(identifyValue);
			broke2Index.setOrderIndex("2");
			broke2Index.setBeginTime(step101.getOccTime());
			broke2Index.setEndTime(lastStepVo.getOccTime());
			broke2Index.setUseTime(lastStepVo.getOccDateMicrosecond() - step101.getOccDateMicrosecond());
			// 如果存在 IFTran，则Broke2的自耗时 - IFtran的环节耗时
			if (existIFTran) {
				long selfUseTime = broke2Index.getUseTime()
						- (iftranMapping.get("105").getOccDateMicrosecond() - iftranMapping.get("102").getOccDateMicrosecond());
				broke2Index.setSelfUseTime(selfUseTime);
			}

			indexs.add(broke2Index);

			if (existIFTran) {
				OrderTimeLogVo iftranIndex = new OrderTimeLogVo();
				iftranIndex.setIdentifyValue(identifyValue);
				iftranIndex.setOrderIndex("3");
				iftranIndex.setBeginTime(iftranMapping.get("103").getOccTime());
				iftranIndex.setEndTime(iftranMapping.get("104").getOccTime());
				iftranIndex.setUseTime(iftranMapping.get("104").getOccDateMicrosecond() - iftranMapping.get("103").getOccDateMicrosecond());
				iftranIndex.setSelfUseTime(broke2Index.getUseTime());

				indexs.add(iftranIndex);
			}

			if (existCallback) {
				OrderTimeLogVo callbackIndex = new OrderTimeLogVo();
				callbackIndex.setIdentifyValue(identifyValue);
				callbackIndex.setOrderIndex("4");
				callbackIndex.setBeginTime(callbackMapping.get("107").getOccTime());
				callbackIndex.setEndTime(callbackMapping.get("108").getOccTime());
				callbackIndex.setUseTime(callbackMapping.get("108").getOccDateMicrosecond() - callbackMapping.get("107").getOccDateMicrosecond());
				callbackIndex.setSelfUseTime(broke2Index.getUseTime());

				indexs.add(callbackIndex);
			}
			
			indexs.add(orderKeepIndex);
			analysisResultService.analysisHandleResult(indexs);
		}
	}

	private StepVo find(List<StepVo> vos, String sn) {
		StepVo vo = null;
		if (vos != null && !vos.isEmpty()) {
			Iterator<StepVo> iterator = vos.iterator();
			while (iterator.hasNext()) {
				StepVo stepVo = iterator.next();
				if (sn.equals(stepVo.getSn())) {
					vo = stepVo;
					iterator.remove();
					break;
				}
			}
		}
		return vo;
	}
}