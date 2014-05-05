/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.entity<br/>
 * <b>文件名：</b>OrderTimeLogVo.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-25-上午10:19:55<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.entity;

/**
 * 
 * <b>类名称：</b>OrderTimeLogVo<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-25 上午10:19:55<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class OrderTimeLogVo {
	private long id;
	private String identifyValue;
	private String orderIndex;
	private String beginTime;
	private String endTime;
	private long useTime;
	private long selfUseTime;
	private long parentId;
	private int exactFlag =1;
	private String content="";
	private String pid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdentifyValue() {
		return identifyValue;
	}

	public void setIdentifyValue(String identifyValue) {
		this.identifyValue = identifyValue;
	}

	public String getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getExactFlag() {
		return exactFlag;
	}

	public void setExactFlag(int exactFlag) {
		this.exactFlag = exactFlag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getSelfUseTime() {
		return selfUseTime;
	}

	public void setSelfUseTime(long selfUseTime) {
		this.selfUseTime = selfUseTime;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderTimeLogVo [id=");
		builder.append(id);
		builder.append(", identifyValue=");
		builder.append(identifyValue);
		builder.append(", orderIndex=");
		builder.append(orderIndex);
		builder.append(", beginTime=");
		builder.append(beginTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", useTime=");
		builder.append(useTime);
		builder.append(", selfUseTime=");
		builder.append(selfUseTime);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", exactFlag=");
		builder.append(exactFlag);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}
}
