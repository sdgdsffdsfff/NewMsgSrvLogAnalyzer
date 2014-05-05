/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.entity<br/>
 * <b>文件名：</b>StepVo.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-24-上午10:52:04<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.entity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.msgsrv.log.analyzer.common.DateUtil;

/**
 * 
 * <b>类名称：</b>StepVo<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-24 上午10:52:04<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class StepVo implements Comparable<StepVo> {

	private String identifyValue;// 已知KEY值
	private String step;// 步骤标识
	private String occTime;// 处理时间
	private boolean exactFlag;// 标志此步骤是否是精确匹配
	private String sn;// 步骤对应的SN号
	private String eigenValue;// 步骤的特征值

	private List<String> multiKeys = Collections.emptyList();// MultiKey 集合

	private long occDateMicrosecond = 0;
	private long timeout = 0;
	private long analyzeTimeout = 0;

	public StepVo(String identifyValue, String step, String occTime, boolean exactFlag, List<String> multiKeys, String sn, String eigenValue) {
		super();
		this.identifyValue = identifyValue;
		this.step = step;
		this.occTime = occTime;
		this.exactFlag = exactFlag;
		this.multiKeys = multiKeys;
		this.sn = sn;
		this.eigenValue = eigenValue;
	}

	public StepVo(String identifyValue, String step, String occTime, boolean exactFlag) {
		super();
		this.identifyValue = identifyValue;
		this.step = step;
		this.occTime = occTime;
		this.exactFlag = exactFlag;
	}

	public String getIdentifyValue() {
		return identifyValue;
	}

	public void setIdentifyValue(String identifyValue) {
		this.identifyValue = identifyValue;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public boolean isExactFlag() {
		return exactFlag;
	}

	public void setExactFlag(boolean exactFlag) {
		this.exactFlag = exactFlag;
	}

	public String getOccTime() {
		return occTime;
	}

	public long getOccDateTime() {
		long time = 0;
		String[] s1 = occTime.split("\\.");
		try {
			Date endOccDate = DateUtil.parse(s1[0]);
			int endOcc = Integer.parseInt(s1[1]);
			time = endOccDate.getTime() + (endOcc / 1000);
		} catch (Exception e) {
		}
		return time;
	}

	public String getEigenValue() {
		return eigenValue;
	}

	public void setEigenValue(String eigenValue) {
		this.eigenValue = eigenValue;
	}

	public long getOccDateMicrosecond() {
		if (occDateMicrosecond > 0) {
			return occDateMicrosecond;
		}
		String[] s1 = occTime.split("\\.");
		try {
			Date endOccDate = DateUtil.parse(s1[0]);
			int endOcc = Integer.parseInt(s1[1]);
			occDateMicrosecond = endOccDate.getTime() * 1000 + endOcc;
		} catch (Exception e) {
		}
		return occDateMicrosecond;
	}

	public void setOccTime(String occTime) {
		this.occTime = occTime;
	}

	public List<String> getMultiKeys() {
		return multiKeys;
	}

	public void setMultiKeys(List<String> multiKeys) {
		this.multiKeys = multiKeys;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(step);
		builder.append(" ");
		builder.append(identifyValue);
		builder.append(" ");
		builder.append(occTime);
		builder.append(" ");
		builder.append(exactFlag);
		builder.append(" ");
		return builder.toString();
	}

	@Override
	public int compareTo(StepVo o) {
		return this.occTime.compareTo(o.getOccTime());
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public long getAnalyzeTimeout() {
		return analyzeTimeout;
	}

	public void setAnalyzeTimeout(long analyzeTimeout) {
		this.analyzeTimeout = analyzeTimeout;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
