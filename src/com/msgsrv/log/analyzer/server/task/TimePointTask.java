/**
 * <b>包名：</b>com.msgsrv.log.analyzer.server.task<br/>
 * <b>文件名：</b>TimePointTask.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-27-上午8:34:46<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.task;

import java.util.Date;

import com.msgsrv.log.analyzer.common.TimeUitl;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.msgsrv.log.analyzer.core.AnalyzerCoreMemory;

/**
 * 
 * <b>类名称：</b>TimePointTask<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-27 上午8:34:46<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class TimePointTask implements Runnable {
	
	@Override
	public void run() {
		AnalyzerCoreMemory.TIME_POINT = new Date(AnalyzerCoreMemory.TIME_POINT.getTime() + SystemConfig.NEXT_TIME * TimeUitl.MINUTES);
		if (AnalyzerCoreMemory.TIME_POINT.getTime() > System.currentTimeMillis()) {
			try {
				Thread.sleep(SystemConfig.NEXT_TIME * TimeUitl.MINUTES);
			} catch (InterruptedException e) {
			}
		}
	}

}
