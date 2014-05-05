/**
 * <b>包名：</b>com.msgsrv.log.analyzer.server.dao.impl<br/>
 * <b>文件名：</b>OrderTimeLogDaoDBsrv.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-27-上午10:58:45<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.dao.impl;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.core.MsgSrvService;
import com.msgsrv.log.analyzer.server.dao.OrderTimeLogDao;
import com.msgsrv.log.analyzer.server.entity.OrderTimeLogVo;

/**
 * 
 * <b>类名称：</b>OrderTimeLogDaoDBsrv<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-27 上午10:58:45<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 * 
 */
public class OrderTimeLogDaoNewDBsrvImpl implements OrderTimeLogDao {

	private static final Logger logger = Logger.getLogger(OrderTimeLogDaoNewDBsrvImpl.class);
	private static final String appname ="DBServerRecv ";
	private static final String command = "NewNNKQuery ";
	
	private static final String type = "0";
	private static final String mysqlConnId = "6";
	
	@Override
	public void insert(OrderTimeLogVo vo, String path) {
		if (vo != null) {
			String sql = "";
			try {
				String identifyValue = vo.getIdentifyValue();
				if (identifyValue.indexOf("@") > 0) {
					identifyValue = identifyValue.substring(identifyValue.indexOf("@") + 1);
				}
				sql = "INSERT INTO OrderTimeLog(identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time) VALUES('" + identifyValue + "','" + path + "','" + vo.getOrderIndex() + "','" + vo.getBeginTime() + "','" + vo.getEndTime() + "','" + vo.getSelfUseTime() + "','" + vo.getUseTime() + "','" + vo.getParentId() + "','" + vo.getExactFlag() + "','" + vo.getContent() + "',now());";
				if (logger.isDebugEnabled()) {
					logger.debug("插入OrderTimeLog：sql = " + sql);
				}
				StringBuilder message = new StringBuilder();
				
				String sn = UUID.randomUUID().toString();
				
				message.append(appname);
				message.append(command);
				message.append(sn + " ");
				message.append("\"");
				message.append(sql );
				message.append("\""+ " ");
				message.append(type + " ");
				message.append(mysqlConnId);
				
				MsgSrvService.send(message.toString());
			} catch (Exception e) {
				logger.error("异常sql语句:" + sql);
				logger.error(e.getMessage(), e);
			}
		}
	}
}

