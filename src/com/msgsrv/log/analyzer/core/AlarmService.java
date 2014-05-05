package com.msgsrv.log.analyzer.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.msgsrv.log.analyzer.common.Base64Util;
import com.msgsrv.log.analyzer.common.JsonUtil;
import com.msgsrv.log.analyzer.common.MD5Util;
import com.msgsrv.log.analyzer.config.SystemConfig;
import com.nnk.msgsrv.client.core.MsgSrvContext;

public class AlarmService {

	private static final String alarmType = "1047";// 日志分析报警

	public static void alarm(String content) {
		// R:AlarmRuleMatchSystem AlarmInt Pid=pid
		// JsonData=base64(json(alarmType,sysname,content)) sign
		// pid: 为唯一值，表示包Id
		// alarmType:报警类型(待分配)
		// sysname 系统编号(附录2)
		// content 报警内容
		// sign签名字段，MD5(base64(json(......))+key)
		String pid = UUID.randomUUID().toString();

		Map<String, String> datas = new HashMap<String, String>();
		datas.put("alarmType", alarmType);
		datas.put("content", content);
		datas.put("alarmLevel", "2");
		String json = JsonUtil.buildJson(datas);
		String base64 = Base64Util.encode(json);
		String sign = MD5Util.getMD5String(base64 + SystemConfig.ALARM_KEY);

		com.nnk.msgsrv.client.core.MsgSrvService msgSrvService = MsgSrvContext.get("MSLAAlarm");
		if (msgSrvService != null) {
			msgSrvService.send("AlarmRuleMatchSystem AlarmInt Pid=" + pid + " JsonData=" + base64 + " " + sign);
		}
	}

	public static void alarm(String content, String alarmLevel) {
		// R:AlarmRuleMatchSystem AlarmInt Pid=pid
		// JsonData=base64(json(alarmType,sysname,content)) sign
		// pid: 为唯一值，表示包Id
		// alarmType:报警类型(待分配)
		// sysname 系统编号(附录2)
		// content 报警内容
		// sign签名字段，MD5(base64(json(......))+key)
		String pid = UUID.randomUUID().toString();

		Map<String, String> datas = new HashMap<String, String>();
		datas.put("alarmType", alarmType);
		datas.put("content", content);
		datas.put("alarmLevel", alarmLevel);

		String json = JsonUtil.buildJson(datas);
		String base64 = Base64Util.encode(json);
		String sign = MD5Util.getMD5String(base64 + SystemConfig.ALARM_KEY);

		com.nnk.msgsrv.client.core.MsgSrvService msgSrvService = MsgSrvContext.get("MSLAAlarm");
		if (msgSrvService != null) {
			msgSrvService.send("AlarmRuleMatchSystem AlarmInt Pid=" + pid + " JsonData=" + base64 + " " + sign);
		}
	}

}
