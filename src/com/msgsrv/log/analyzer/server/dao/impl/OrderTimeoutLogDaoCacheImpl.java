/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.dao.impl<br/>
 * <b>文件名：</b>OrderTimeoutLogDaoImpl.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-25-上午9:42:47<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.dao.impl;

import java.util.List;

import com.msgsrv.log.analyzer.server.dao.OrderTimeoutLogDao;
import com.msgsrv.log.analyzer.server.entity.StepVo;

/**
 * 
 * <b>类名称：</b>OrderTimeoutLogDaoImpl<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-25 上午9:42:47<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class OrderTimeoutLogDaoCacheImpl implements OrderTimeoutLogDao {

	// private static final Logger logger =
	// Logger.getLogger(OrderTimeoutLogDaoCacheImpl.class);

	@Override
	public void insert(List<StepVo> setpVos) {
		// if (setpVos != null) {
		// for (StepVo stepVo : setpVos) {
		// int exactFlag = 0;
		// if (stepVo.isExactFlag()) {
		// exactFlag = 1;
		// }
		// String identifyValue = stepVo.getIdentifyValue();
		// String sql =
		// "INSERT INTO OrderTimeoutLog(identifyValue,step,occTime,exactFlag) VALUES('"
		// + identifyValue + "','" + stepVo.getStep() + "','" +
		// stepVo.getOccTime() + "','" + exactFlag + "');";
		// AnalyzerCoreMemory.SQL_CACHE.add(sql);
		// }
		// }
	}

	@Override
	public void insert(StepVo setpVo) {
		// if (setpVo != null) {
		// int exactFlag = 0;
		// if (setpVo.isExactFlag()) {
		// exactFlag = 1;
		// }
		// String identifyValue = setpVo.getIdentifyValue();
		// String sql =
		// "INSERT INTO OrderTimeoutLog(identifyValue,step,occTime,exactFlag,insert_time) VALUES('"
		// + identifyValue + "','" + setpVo.getStep() + "','" +
		// setpVo.getOccTime() + "','" + exactFlag + "',now());";
		// if (logger.isDebugEnabled()) {
		// logger.debug("插入OrderTimeoutLog：sql = " + sql);
		// }
		// AnalyzerCoreMemory.SQL_CACHE.add(sql);
		// }

	}

}
