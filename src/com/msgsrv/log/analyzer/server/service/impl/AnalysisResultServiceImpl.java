/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.service.impl<br/>
 * <b>文件名：</b>AnalysisResultServiceImpl.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-25-上午10:13:28<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.common.StringUtil;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.core.MsgSrvService;
import com.msgsrv.log.analyzer.server.dao.OrderTimeLogDao;
import com.msgsrv.log.analyzer.server.dao.OrderTimeoutLogDao;
import com.msgsrv.log.analyzer.server.dao.impl.LoadDataInFileImpl;
import com.msgsrv.log.analyzer.server.dao.impl.OrderTimeLogDaoCacheImpl;
import com.msgsrv.log.analyzer.server.dao.impl.OrderTimeLogDaoImpl;
import com.msgsrv.log.analyzer.server.dao.impl.OrderTimeLogDaoNewDBsrvImpl;
import com.msgsrv.log.analyzer.server.dao.impl.OrderTimeoutLogDaoCacheImpl;
import com.msgsrv.log.analyzer.server.dao.impl.OrderTimeoutLogDaoImpl;
import com.msgsrv.log.analyzer.server.entity.MonitorVo;
import com.msgsrv.log.analyzer.server.entity.OrderTimeLogVo;
import com.msgsrv.log.analyzer.server.entity.OrderTreeConfig;
import com.msgsrv.log.analyzer.server.entity.PathConfig;
import com.msgsrv.log.analyzer.server.entity.StepVo;
import com.msgsrv.log.analyzer.server.entity.UseTimeResult;
import com.msgsrv.log.analyzer.server.service.AnalysisResultService;

/**
 * 
 * <b>类名称：</b>AnalysisResultServiceImpl<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-25 上午10:13:28<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class AnalysisResultServiceImpl implements AnalysisResultService {

	private static final Logger logger = Logger.getLogger(AnalysisResultServiceImpl.class);

	private OrderTimeLogDao orderTimeLogDao = null;
	private OrderTimeoutLogDao orderTimeoutLogDao = null;

	private static final String appname = "DBServerRecv ";
	private static final String command = "NewNNKQuery ";

	private static final String type = "0";
	private static final String mysqlConnId = "6";

	public AnalysisResultServiceImpl() {
		if ("jdbc".equals(SystemConfig.DATABASE_TYPE)) {
			orderTimeLogDao = new OrderTimeLogDaoImpl();
			orderTimeoutLogDao = new OrderTimeoutLogDaoImpl();
		} else if ("dbsrv".equals(SystemConfig.DATABASE_TYPE)) {
			// orderTimeLogDao = new OrderTimeLogDaoDBsrvImpl(String.class,
			// DBSrvUtils.getDatabaseManager());
			orderTimeLogDao = new OrderTimeLogDaoNewDBsrvImpl();
			orderTimeoutLogDao = new OrderTimeoutLogDaoImpl();
		} else if ("cache".equals(SystemConfig.DATABASE_TYPE)) {
			orderTimeLogDao = new OrderTimeLogDaoCacheImpl();
			orderTimeoutLogDao = new OrderTimeoutLogDaoCacheImpl();
		} else if ("load".equals(SystemConfig.DATABASE_TYPE)) {
			orderTimeLogDao = new LoadDataInFileImpl();
			orderTimeoutLogDao = new OrderTimeoutLogDaoImpl();
		} else {
			orderTimeLogDao = new OrderTimeLogDaoImpl();
			orderTimeoutLogDao = new OrderTimeoutLogDaoImpl();
		}
	}

	@Override
	public void analysisResult(PathConfig pc, Collection<StepVo> stepVos) {
		// 得到identifyValue
		String identifyValue = "";
		String orderTime = "";
		String identifyStep = pc.getIdentifyStep();
		for (StepVo stepVo : stepVos) {
			if (stepVo.getStep().equals(identifyStep)) {
				identifyValue = stepVo.getIdentifyValue();
				break;
			}
		}
		if (identifyValue.indexOf("@") > 0) {
			identifyValue = identifyValue.substring(identifyValue.indexOf("@") + 1);
		}
		if ("".equals(identifyValue)) {
			logger.info("路径:" + pc + "配置的[identifyStep=" + identifyStep + "]得到的[identifyValue=empty]不能确定此key");
			return;
		}
		// 遍历OrderTree
		List<OrderTreeConfig> orderTrees = pc.getOrderTrees();

		UseTimeResult reuslt = null;

		for (OrderTreeConfig otc : orderTrees) {
			String[] orderIndexs = otc.getOrderIndexs();
			OrderTimeLogVo vo = new OrderTimeLogVo();
			vo.setIdentifyValue(identifyValue);
			vo.setOrderIndex(otc.getId());
			vo.setContent(otc.getContent());

			// 设置上级环节
			vo.setParentId(0);
			// 设置环节耗时
			if (orderIndexs.length == 1) {
				StepVo stepVo = getStepVo(orderIndexs[0], stepVos);
				if (stepVo != null) {
					vo.setBeginTime(stepVo.getOccTime());
					vo.setEndTime(stepVo.getOccTime());
					vo.setUseTime(0);
					int exactFlag = 0;
					if (stepVo.isExactFlag()) {
						exactFlag = 1;
					}
					vo.setExactFlag(exactFlag);
				}
			} else if (orderIndexs.length == 2) {
				StepVo beginStepVo = getStepVo(orderIndexs[0], stepVos);
				StepVo endStepVo = getStepVo(orderIndexs[1], stepVos);
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

				// 如果该环节使用的步骤时间超过报警时间，则需要发送到服务端进行报警处理
				if (vo.getUseTime() > otc.getAlarm()) {

				}

				int exactFlag = 0;
				// 需要开始步骤和结束步骤都要是精确的，此环节才算是精确的
				if (beginStepVo.isExactFlag() && endStepVo.isExactFlag()) {
					exactFlag = 1;
				}
				vo.setExactFlag(exactFlag);
			}

			// 修改自耗时算法，自耗时=环节耗时-所有子环节耗时
			long childStepTimes = getChildStepTimes(otc.getId(), orderTrees, stepVos);
			// 如果它没有子环节，则自耗时就的当前环节耗时
			if (childStepTimes == 0) {
				vo.setSelfUseTime(vo.getUseTime());
			} else {
				vo.setSelfUseTime(vo.getUseTime() - childStepTimes);
			}
			if (StringUtil.isEmpty(orderTime)) {
				orderTime = vo.getBeginTime();
			}

			orderTimeLogDao.insert(vo, pc.getName());

			if (reuslt == null) {
				int useType = 0;
				if ("query".equals(pc.getName())) {
					useType = 1;// 查询
				} else if ("OrderReceiver".equals(pc.getName())) {
					useType = 2;// 接单
				} else {
					useType = 3;// 处理
				}
				reuslt = new UseTimeResult(useType, identifyValue, vo.getUseTime(), vo.getBeginTime());
			}
		}

		String sql = "";
		if (reuslt != null) {
			switch (reuslt.getUseType()) {
			case 1:
				sql = "INSERT OrderUseTime(identifyValue,queryTime,orderTime) VALUES ('" + identifyValue + "', " + reuslt.getUseTime() + ",'" + orderTime
						+ "') ON DUPLICATE KEY UPDATE queryTime=" + reuslt.getUseTime() + ";";
				break;
			case 2:
				sql = "INSERT OrderUseTime(identifyValue,receiveTime,orderTime) VALUES ('" + identifyValue + "', " + reuslt.getUseTime() + ",'" + orderTime
						+ "') ON DUPLICATE KEY UPDATE receiveTime=" + reuslt.getUseTime() + ";";
				break;
			case 3:
				sql = "INSERT OrderUseTime(identifyValue,handleTime,orderTime) VALUES ('" + identifyValue + "', " + reuslt.getUseTime() + ",'" + orderTime
						+ "') ON DUPLICATE KEY UPDATE handleTime=" + reuslt.getUseTime() + ";";
				break;
			default:
				break;
			}
		}
		if (StringUtil.isNotEmpty(sql)) {
			AnalyzerCoreMemory.SQL_CACHE.add(sql);
		}
	}

	private long getChildStepTimes(String pid, List<OrderTreeConfig> orderTrees, Collection<StepVo> stepVos) {
		long childStepTimes = 0;
		for (OrderTreeConfig otc : orderTrees) {
			if (!otc.getPid().equals(pid)) {
				continue;
			}
			// String[] orderIndexs = otc.getOrderIndex().split(",");
			String[] orderIndexs = otc.getOrderIndexs();
			// 设置环节耗时
			if (orderIndexs.length == 2) {
				StepVo beginStepVo = getStepVo(orderIndexs[0], stepVos);
				StepVo endStepVo = getStepVo(orderIndexs[1], stepVos);
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
					childStepTimes = useTime + childStepTimes;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return childStepTimes;
	}

	private StepVo getStepVo(String orderIndex, Collection<StepVo> stepVos) {
		StepVo vo = null;
		if (StringUtil.isEmpty(orderIndex)) {
			return vo;
		}
		for (StepVo stepVo : stepVos) {
			if (orderIndex.equals(stepVo.getStep())) {
				if (vo == null) {
					vo = stepVo;
				}
			}
		}
		return vo;
	}

	@Override
	public void timeout(StepVo setpVo) {
		orderTimeoutLogDao.insert(setpVo);
	}

	@Override
	public void analysisResult(PathConfig pc, Collection<StepVo> stepVos, List<OrderTimeLogVo> orderTimeLogVos) {
		// 得到identifyValue
		String identifyValue = "";
		String orderTime = "";
		String identifyStep = pc.getIdentifyStep();
		for (StepVo stepVo : stepVos) {
			if (stepVo.getStep().equals(identifyStep)) {
				identifyValue = stepVo.getIdentifyValue();
				break;
			}
		}
		if (identifyValue.indexOf("@") > 0) {
			identifyValue = identifyValue.substring(identifyValue.indexOf("@") + 1);
		}

		Map<String, Long> childUseTime = new HashMap<String, Long>();

		if (orderTimeLogVos != null && !orderTimeLogVos.isEmpty()) {
			for (OrderTimeLogVo orderTimeLogVo : orderTimeLogVos) {
				orderTimeLogVo.setIdentifyValue(identifyValue);
				if (childUseTime.containsKey(orderTimeLogVo.getPid())) {
					long useTime = childUseTime.get(orderTimeLogVo.getPid());
					useTime = useTime + orderTimeLogVo.getUseTime();
					childUseTime.put(orderTimeLogVo.getPid(), useTime);
				} else {
					childUseTime.put(orderTimeLogVo.getPid(), orderTimeLogVo.getUseTime());
				}
				orderTimeLogDao.insert(orderTimeLogVo, pc.getName());
			}
		}

		if ("".equals(identifyValue)) {
			logger.info("路径:" + pc + "配置的[identifyStep=" + identifyStep + "]得到的[identifyValue=empty]不能确定此key");
			return;
		}
		// 遍历OrderTree
		List<OrderTreeConfig> orderTrees = pc.getOrderTrees();

		UseTimeResult reuslt = null;

		for (OrderTreeConfig otc : orderTrees) {
			String[] orderIndexs = otc.getOrderIndexs();
			OrderTimeLogVo vo = new OrderTimeLogVo();
			vo.setIdentifyValue(identifyValue);
			vo.setOrderIndex(otc.getId());
			vo.setContent(otc.getContent());

			// 设置上级环节
			vo.setParentId(0);
			// 设置环节耗时
			if (orderIndexs.length == 1) {
				StepVo stepVo = getStepVo(orderIndexs[0], stepVos);
				if (stepVo != null) {
					vo.setBeginTime(stepVo.getOccTime());
					vo.setEndTime(stepVo.getOccTime());
					vo.setUseTime(0);
					int exactFlag = 0;
					if (stepVo.isExactFlag()) {
						exactFlag = 1;
					}
					vo.setExactFlag(exactFlag);
				}
			} else if (orderIndexs.length == 2) {
				StepVo beginStepVo = getStepVo(orderIndexs[0], stepVos);
				StepVo endStepVo = getStepVo(orderIndexs[1], stepVos);
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

				// 如果该环节使用的步骤时间超过报警时间，则需要发送到服务端进行报警处理
				if (vo.getUseTime() > otc.getAlarm()) {

				}

				int exactFlag = 0;
				// 需要开始步骤和结束步骤都要是精确的，此环节才算是精确的
				if (beginStepVo.isExactFlag() && endStepVo.isExactFlag()) {
					exactFlag = 1;
				}
				vo.setExactFlag(exactFlag);
			}

			// 修改自耗时算法，自耗时=环节耗时-所有子环节耗时
			long childStepTimes = getChildStepTimes(otc.getId(), orderTrees, stepVos);
			// 如果它没有子环节，则自耗时就的当前环节耗时
			if (childUseTime.containsKey(otc.getId())) {
				long useTime = childUseTime.get(otc.getId());
				childStepTimes = childStepTimes + useTime;
			}
			vo.setSelfUseTime(vo.getUseTime() - childStepTimes);
			if (StringUtil.isEmpty(orderTime)) {
				orderTime = vo.getBeginTime();
			}

			orderTimeLogDao.insert(vo, pc.getName());

			if (reuslt == null) {
				int useType = 0;
				if ("query".equals(pc.getName())) {
					useType = 1;// 查询
				} else if ("OrderReceiver".equals(pc.getName())) {
					useType = 2;// 接单
				} else {
					useType = 3;// 处理
				}
				reuslt = new UseTimeResult(useType, identifyValue, vo.getUseTime(), vo.getBeginTime());
			}
		}

		String sql = "";
		if (reuslt != null) {
			switch (reuslt.getUseType()) {
			case 1:
				sql = "INSERT OrderUseTime(identifyValue,queryTime,orderTime) VALUES ('" + identifyValue + "', " + reuslt.getUseTime() + ",'" + orderTime
						+ "') ON DUPLICATE KEY UPDATE queryTime=" + reuslt.getUseTime() + ";";
				break;
			case 2:
				sql = "INSERT OrderUseTime(identifyValue,receiveTime,orderTime) VALUES ('" + identifyValue + "', " + reuslt.getUseTime() + ",'" + orderTime
						+ "') ON DUPLICATE KEY UPDATE receiveTime=" + reuslt.getUseTime() + ";";
				break;
			case 3:
				sql = "INSERT OrderUseTime(identifyValue,handleTime,orderTime) VALUES ('" + identifyValue + "', " + reuslt.getUseTime() + ",'" + orderTime
						+ "') ON DUPLICATE KEY UPDATE handleTime=" + reuslt.getUseTime() + ";";
				break;
			default:
				break;
			}
		}
		if (StringUtil.isNotEmpty(sql)) {
			AnalyzerCoreMemory.SQL_CACHE.add(sql);
		}
	}

	@Override
	public void analysisHandleResult(List<OrderTimeLogVo> orderTimeLogVos) {
		if (!orderTimeLogVos.isEmpty()) {
			
			MonitorVo.ANALYSIS_COMPLETE_COUNTER ++;
			
			OrderTimeLogVo orderKeepIndex = orderTimeLogVos.get(orderTimeLogVos.size()-1);
			String sql = "INSERT OrderUseTime(identifyValue,handleTime,orderTime) VALUES ('" + orderKeepIndex.getIdentifyValue() + "', "
					+ orderKeepIndex.getUseTime() + ",'" + orderKeepIndex.getBeginTime() + "') ON DUPLICATE KEY UPDATE handleTime="
					+ orderKeepIndex.getUseTime() + ";";
			AnalyzerCoreMemory.SQL_CACHE.add(sql);

		}

		for (OrderTimeLogVo otlv : orderTimeLogVos) {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO OrderTimeLog(identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time) VALUES('");
			sql.append(otlv.getIdentifyValue());
			sql.append("','");
			sql.append("handle");
			sql.append("','");
			sql.append(otlv.getOrderIndex());
			sql.append("','");
			sql.append(otlv.getBeginTime());
			sql.append("','");
			sql.append(otlv.getEndTime());
			sql.append("','");
			sql.append(otlv.getSelfUseTime());
			sql.append("','");
			sql.append(otlv.getUseTime());
			sql.append("','");
			sql.append(otlv.getParentId());
			sql.append("','");
			sql.append(otlv.getExactFlag());
			sql.append("','");
			sql.append(otlv.getContent());
			sql.append("',now());");
			System.out.println(sql);
			StringBuilder message = new StringBuilder();
			String sn = UUID.randomUUID().toString();
			message.append(appname);
			message.append(command);
			message.append(sn + " ");
			message.append("\"");
			message.append(sql.toString());
			message.append("\"" + " ");
			message.append(type + " ");
			message.append(mysqlConnId);

			MsgSrvService.send(message.toString());
		}

	}
}
