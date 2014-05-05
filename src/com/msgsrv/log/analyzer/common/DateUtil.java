/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.universal.account.common<br/>
 * <b>文件名：</b>DateUtil.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-6-上午9:41:00<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * <b>类名称：</b>DateUtil<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-6 上午9:41:00<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class DateUtil {
	private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			return format;
		}
	};

	public static String format(Date date, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static String format(Date date) {
		return DATE_FORMAT.get().format(date);
	}

	public static Date parse(String source) throws ParseException {
		return DATE_FORMAT.get().parse(source);
	}

	public static Date parse(String source, String pattern) throws ParseException {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.parse(source);
	}
}
