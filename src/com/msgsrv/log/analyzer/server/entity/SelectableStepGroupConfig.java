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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

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
public class SelectableStepGroupConfig {

	@XmlAttribute
	private String steps = "";
	private List<String> _steps = new ArrayList<String>();
	@XmlAttribute
	private String before = "";
	@XmlAttribute
	private String last = "";

	@XmlElement(name = "orderTree")
	private List<OrderTreeConfig> orderTrees = new ArrayList<OrderTreeConfig>();

	public List<String> getSteps() {
		if (_steps.isEmpty()) {
			String[] strings = steps.split(",");
			for (String step : strings) {
				_steps.add(step);
			}
		}
		return _steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public List<OrderTreeConfig> getOrderTrees() {
		return orderTrees;
	}

	public void setOrderTrees(List<OrderTreeConfig> orderTrees) {
		this.orderTrees = orderTrees;
	}

}
