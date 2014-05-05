/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.client.core<br/>
 * <b>文件名：</b>LogFileComparator.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-7-16-下午7:26:38<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.client.core;

import java.io.File;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import com.msgsrv.log.analyzer.common.DateUtil;

/**
 * 
 * <b>类名称：</b>LogFileComparator<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-7-16 下午7:26:38<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class LogFileComparator implements Comparator<File> {

	@Override
	public int compare(File f1, File f2) {
		long result = 0;
		String fileDate1 = f1.getName().substring(f1.getName().lastIndexOf("-") + 1, f1.getName().lastIndexOf("."));
		String fileDate2 = f2.getName().substring(f2.getName().lastIndexOf("-") + 1, f2.getName().lastIndexOf("."));
		try {
			Date date1 = DateUtil.parse(fileDate1, "yyyyMMdd");
			Date date2 = DateUtil.parse(fileDate2, "yyyyMMdd");
			result = date1.getTime() - date2.getTime();
			if (result == 0) {
				String suffix1 = f1.getName().substring(f1.getName().lastIndexOf(".") + 1);
				String suffix2 = f2.getName().substring(f2.getName().lastIndexOf(".") + 1);
				int num1 = Integer.MAX_VALUE;
				int num2 = Integer.MAX_VALUE;
				try {
					num1 = Integer.parseInt(suffix1);
				} catch (Exception e) {
				}
				try {
					num2 = Integer.parseInt(suffix2);
				} catch (Exception e) {
				}
				result = num1 - num2;
			}
		} catch (ParseException e) {
		}
		return (int) result;
	}

}
