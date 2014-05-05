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

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
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
public class OrderTimeLogDaoCacheImpl implements OrderTimeLogDao {

	private static final Logger logger = Logger.getLogger(OrderTimeLogDaoCacheImpl.class);

	@Override
	public void insert(OrderTimeLogVo vo, String path) {
		if (vo != null) {
			String sql = "";
			String identifyValue = vo.getIdentifyValue();
			sql = "INSERT INTO OrderTimeLog(identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time) VALUES('" + identifyValue + "','" + path + "','" + vo.getOrderIndex() + "','" + vo.getBeginTime() + "','" + vo.getEndTime() + "','" + vo.getSelfUseTime() + "','" + vo.getUseTime() + "','" + vo.getParentId() + "','" + vo.getExactFlag() + "','" + vo.getContent() + "',now());";
			if (logger.isDebugEnabled()) {
				logger.debug("插入OrderTimeLog：sql = " + sql);
			}
			AnalyzerCoreMemory.SQL_CACHE.add(sql);
		}
	}
}
