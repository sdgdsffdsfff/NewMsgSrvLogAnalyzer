/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.client.entity<br/>
 * <b>文件名：</b>MonitorVo.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-12-23-上午9:20:37<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.entity;


/**
 * 
 * <b>类名称：</b>MonitorVo<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-12-23 上午9:20:37<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class MonitorVo {

	public static int MSGSRV_COUNTER = 0;// MsgSrv消息接收数量
	public static long CMD_CACHE_SIZE = 0;// 命令缓存的大小
	public static long DATA_MEMORY_SIZE = 0;// 数据缓存的大小
	public static long ANALYSIS_COMPLETE_COUNTER = 0;// 分析完毕计数器

//	public static String print() {
////		DATA_MEMORY_SIZE = AnalyzerMemory.DATA_MEMORY.size();
////		CMD_CACHE_SIZE = CommandCache.size();
////
////		long KEY_RESULT_COUNTER = 0;// 关键步骤
////		Set<String> keySet = Analyzer.KEY_RESULT_CACHE.keySet();
////		for (String key : keySet) {
////			KEY_RESULT_COUNTER = KEY_RESULT_COUNTER + Analyzer.KEY_RESULT_CACHE.get(key).size();
////		}
////
////		long NOKEY_RESULT_COUNTER = 0;// 非关键步骤
////		Set<String> keySet2 = Analyzer.NOKEY_RESULT_CACHE.keySet();
////		for (String key : keySet2) {
////			NOKEY_RESULT_COUNTER = NOKEY_RESULT_COUNTER + Analyzer.NOKEY_RESULT_CACHE.get(key).size();
////		}
////
////		int START_STEPS_SIZE = Analyzer.START_STEPS.size();// 起始步骤大小
////
////		StringBuilder builder = new StringBuilder();
////		builder.append("\n-------------------开始输出监控数据--------- --------------");
////		builder.append("\n当前服务端[接收消息数量][" + MSGSRV_COUNTER + "]");
////		builder.append("\n当前服务端[命令缓存大小][" + CMD_CACHE_SIZE + "]");
////		builder.append("\n当前服务端[消息队列大小][" + DATA_MEMORY_SIZE + "]");
////		builder.append("\n当前服务端[内存关键步骤][" + KEY_RESULT_COUNTER + "]");
////		builder.append("\n当前服务端[内存非关键步骤][" + NOKEY_RESULT_COUNTER + "]");
////		builder.append("\n当前服务端[内存开始步骤][" + START_STEPS_SIZE + "]");
////		builder.append("\n当前服务端[分析完毕数量][" + ANALYSIS_COMPLETE_COUNTER + "]");
////		builder.append("\n-------------------监控数据输出完毕-----------------------");
////		return builder.toString();
//	}
}
