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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.server.dao.DBManage;
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
public class OrderTimeoutLogDaoImpl implements OrderTimeoutLogDao {

	private static final Logger logger = Logger.getLogger(OrderTimeoutLogDaoImpl.class);

	@Override
	public void insert(List<StepVo> setpVos) {
		if (setpVos != null) {
			Connection connection = DBManage.getConnection();
			String sql = "";
			Statement statement = null;
			try {
				statement = connection.createStatement();
				for (StepVo stepVo : setpVos) {
					int exactFlag = 0;
					if (stepVo.isExactFlag()) {
						exactFlag = 1;
					}
					String identifyValue = stepVo.getIdentifyValue();
					sql = "INSERT INTO OrderTimeoutLog(identifyValue,step,occTime,exactFlag) VALUES('" + identifyValue + "','" + stepVo.getStep() + "','" + stepVo.getOccTime() + "','" + exactFlag + "');";
					statement.addBatch(sql);
					if (logger.isDebugEnabled()) {
						logger.debug("插入OrderTimeoutLog：sql = " + sql);
					}
				}
				statement.executeBatch();
			} catch (Exception e) {
				logger.error("异常sql:" + sql.toString());
				logger.error(e.getMessage(), e);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
					}
				}
			}

		}
	}

	@Override
	public void insert(StepVo setpVo) {
		if (setpVo != null) {
			Connection connection = DBManage.getConnection();
			String sql = "";
			Statement statement = null;
			try {
				statement = connection.createStatement();
				int exactFlag = 0;
				if (setpVo.isExactFlag()) {
					exactFlag = 1;
				}
				String identifyValue = setpVo.getIdentifyValue();
				sql = "INSERT INTO OrderTimeoutLog(identifyValue,step,occTime,exactFlag,insert_time) VALUES('" + identifyValue + "','" + setpVo.getStep() + "','" + setpVo.getOccTime() + "','" + exactFlag + "',now());";
				if (logger.isDebugEnabled()) {
					logger.debug("插入OrderTimeoutLog：sql = " + sql);
				}
				statement.addBatch(sql);
				statement.executeBatch();
			} catch (Exception e) {
				logger.info("异常sql:" + sql.toString());
				logger.error(e.getMessage(), e);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
					}
				}
			}
		}

	}

}
