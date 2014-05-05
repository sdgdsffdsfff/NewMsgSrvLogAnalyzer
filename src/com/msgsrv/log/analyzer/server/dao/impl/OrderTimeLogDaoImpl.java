/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.dao.impl<br/>
 * <b>文件名：</b>OrderTimeLogDaoImpl.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-25-上午11:07:31<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.server.dao.DBManage;
import com.msgsrv.log.analyzer.server.dao.OrderTimeLogDao;
import com.msgsrv.log.analyzer.server.entity.OrderTimeLogVo;

/**
 * 
 * <b>类名称：</b>OrderTimeLogDaoImpl<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-25 上午11:07:31<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class OrderTimeLogDaoImpl implements OrderTimeLogDao {

	private static final Logger logger = Logger.getLogger(OrderTimeLogDaoImpl.class);

	@Override
	public void insert(OrderTimeLogVo vo, String path) {
		if (vo != null) {
			Connection connection = DBManage.getConnection();
			String sql = "";
			Statement statement = null;
			//ResultSet generatedKeys = null;
			try {
				statement = connection.createStatement();
				String identifyValue = vo.getIdentifyValue();
				sql = "INSERT INTO OrderTimeLog(identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time) VALUES('" + identifyValue + "','" + path + "','" + vo.getOrderIndex() + "','" + vo.getBeginTime() + "','" + vo.getEndTime() + "','" + vo.getSelfUseTime() + "','" + vo.getUseTime() + "','" + vo.getParentId() + "','" + vo.getExactFlag() + "','" + vo.getContent() + "',now());";
				logger.info("SQL:" + sql);
				statement.executeUpdate(sql);
//				generatedKeys = statement.getGeneratedKeys();
//				if (generatedKeys.next()) {
//					long id = generatedKeys.getLong(1);
//					vo.setId(id);
//				}
			} catch (Exception e) {
				// logger.error("异常sql语句:" + sql);
				logger.error(e.getMessage(), e);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
					}
				}
//				if (generatedKeys != null) {
//					try {
//						generatedKeys.close();
//					} catch (SQLException e) {
//					}
//				}
			}

		}
	}

}
