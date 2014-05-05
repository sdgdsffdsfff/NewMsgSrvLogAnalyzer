package com.msgsrv.log.analyzer.server.dao.impl;

import java.util.Date;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;
import com.msgsrv.log.analyzer.server.dao.OrderTimeLogDao;
import com.msgsrv.log.analyzer.server.entity.OrderTimeLogVo;

public class LoadDataInFileImpl  implements OrderTimeLogDao {

	@Override
	public void insert(OrderTimeLogVo vo, String path) {
		if (vo != null) {
			String data = "";
			String identifyValue = vo.getIdentifyValue();
			data = "'" + identifyValue + "','" + path + "','" + vo.getOrderIndex() + "','" + vo.getBeginTime() + "','" + vo.getEndTime() + "','" + vo.getSelfUseTime() + "','" + vo.getUseTime() + "','" + vo.getParentId() + "','" + vo.getExactFlag() + "','" + vo.getContent() + "','"+DateUtil.format(new Date())+"'\n";
			AnalyzerCoreMemory.LOAD_DATA_CACHE.add(data);
		}
	}

}
