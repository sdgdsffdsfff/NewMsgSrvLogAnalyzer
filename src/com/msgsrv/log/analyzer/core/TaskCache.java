/**
 * <b>包名：</b>com.msgsrv.log.analyzer.core<br/>
 * <b>文件名：</b>TaskCache.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-24-上午11:22:17<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.core;

import java.util.HashMap;
import java.util.Map;

import com.msgsrv.log.analyzer.client.task.AnalyzerLogFileTask;

/**
 * 
 * <b>类名称：</b>TaskCache<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-24 上午11:22:17<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class TaskCache {

	public static final Map<String, AnalyzerLogFileTask> ANALYZER_FILE_CHACE = new HashMap<String, AnalyzerLogFileTask>();

}
