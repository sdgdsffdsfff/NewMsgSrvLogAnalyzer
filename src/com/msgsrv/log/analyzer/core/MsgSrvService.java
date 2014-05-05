/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.client.core<br/>
 * <b>文件名：</b>MsgSrvService.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-7-19-下午2:14:24<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.core;

import com.nnk.msgsrv.client.core.MsgSrvContext;

/**
 * 
 * <b>类名称：</b>MsgSrvService<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-7-19 下午2:14:24<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class MsgSrvService {

	public static void send(String message) {
		com.nnk.msgsrv.client.core.MsgSrvService msgSrvService = MsgSrvContext.get("MSLAServer");
		if (msgSrvService != null) {
			msgSrvService.send(message);
		}
	}
}
