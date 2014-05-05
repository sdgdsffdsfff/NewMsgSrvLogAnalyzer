/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.entity<br/>
 * <b>文件名：</b>OrderTreeConfig.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-24-上午9:24:06<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * <b>类名称：</b>OrderTreeConfig<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-24 上午9:24:06<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class OrderTreeConfig {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String pid;
	@XmlAttribute
	private String content = "";
	@XmlAttribute
	private String orderIndex = "";
	
	private String[] orderIndexs;
	
	@XmlAttribute
	private String selfUseTime = "";
	private String[] selfUseTimes;
	@XmlAttribute
	private int alarm = Integer.MAX_VALUE;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOrderIndex() {
		return orderIndex;
	}
	
	public String[] getOrderIndexs() {
		if(orderIndexs==null){
			orderIndexs = this.orderIndex.split(",");
		}
		return orderIndexs;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public String getSelfUseTime() {
		return selfUseTime;
	}
	
	public String[] getSelfUseTimes() {
		if(selfUseTimes==null){
			selfUseTimes = selfUseTime.split(",");
		}
		return selfUseTimes;
	}

	public void setSelfUseTime(String selfUseTime) {
		this.selfUseTime = selfUseTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderTreeConfig [id=");
		builder.append(id);
		builder.append(", pid=");
		builder.append(pid);
		builder.append(", name=");
		builder.append(content);
		builder.append(", orderIndex=");
		builder.append(orderIndex);
		builder.append(", alarm=");
		builder.append(alarm);
		builder.append("]");
		return builder.toString();
	}
}
