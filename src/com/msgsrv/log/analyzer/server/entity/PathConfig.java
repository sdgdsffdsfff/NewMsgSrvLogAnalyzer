/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.client.entity<br/>
 * <b>文件名：</b>RuleConfig.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-20-下午4:47:58<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * <b>类名称：</b>RuleConfig<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-20 下午4:47:58<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class PathConfig {

	@XmlAttribute
	private String name;// 业务路径的名称
	@XmlAttribute(name = "KeyStepSet")
	private String keyStepSet;// 路径包含的Key步骤集合
	private Set<String> keySteps = new HashSet<String>();// 路径包含的Key步骤集合
	private String[] keyStepSets;// 路径包含的Key步骤集合

	@XmlAttribute(name = "NoKeyStepSet")
	private String noKeyStepSet;// 路径包含的非Key步骤集合
	private String[] noKeyStepSets;//
	private Set<String> noKeySteps = new HashSet<String>();;//
	@XmlAttribute
	private String identifyStep;// identifyStep采用第几个步骤的key作为交易的标识

	@XmlElement(name = "orderTree")
	private List<OrderTreeConfig> orderTrees = new ArrayList<OrderTreeConfig>();
	@XmlElement(name = "SelectableStepGroup")
	private List<SelectableStepGroupConfig> SelectableStepGroups = new ArrayList<SelectableStepGroupConfig>();

	private Set<String> essentialSteps = new HashSet<String>();
	
	private List<String> allSteps = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getKeyStepSets() {
		if (keyStepSets == null) {
			keyStepSets = this.keyStepSet.split(",");
		}
		return keyStepSets;
	}

	public Set<String> getKeySteps() {
		if (keySteps.isEmpty()) {
			String[] _keyStepSets = getKeyStepSets();
			for (String step : _keyStepSets) {
				keySteps.add(step);
			}
		}
		return keySteps;
	}

	public void setKeyStepSet(String keyStepSet) {
		this.keyStepSet = keyStepSet;
	}

	public String getIdentifyStep() {
		return identifyStep;
	}

	public void setIdentifyStep(String identifyStep) {
		this.identifyStep = identifyStep;
	}

	public List<OrderTreeConfig> getOrderTrees() {
		return orderTrees;
	}

	public void setOrderTrees(List<OrderTreeConfig> orderTrees) {
		this.orderTrees = orderTrees;
	}

	public String getNoKeyStepSet() {
		return noKeyStepSet;
	}

	public String[] getNoKeyStepSets() {
		if (noKeyStepSets == null) {
			if(noKeyStepSet==null){
				noKeyStepSets = new String[]{};
			}else{
				noKeyStepSets = noKeyStepSet.split(",");
			}
			
		}
		return noKeyStepSets;
	}

	public Set<String> getNoKeySteps() {
		if (noKeySteps.isEmpty()) {
			String[] _noKeyStepSets = getNoKeyStepSets();
			for (String step : _noKeyStepSets) {
				noKeySteps.add(step);
			}
		}
		return noKeySteps;
	}
	
	public List<String> getAllKeySteps() {
		if (allSteps.isEmpty()) {
			Set<String> _keySets = getKeySteps();
			Set<String> _noKeySteps = getNoKeySteps();
			
			allSteps.addAll(_keySets);
			allSteps.addAll(_noKeySteps);
			// 将步骤进行一次排序操作
			Collections.sort(allSteps, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return Integer.parseInt(o1) - Integer.parseInt(o2);
				}
			});
		}
		return allSteps;
	}

	
	
	public Set<String> getEssentialSteps() {
		if(essentialSteps.isEmpty()){
			essentialSteps.addAll(getKeySteps());
			essentialSteps.addAll(getNoKeySteps());
		}
		return essentialSteps;
	}

	public void setNoKeyStepSet(String noKeyStepSet) {
		this.noKeyStepSet = noKeyStepSet;
	}

	public List<SelectableStepGroupConfig> getSelectableStepGroups() {
		return SelectableStepGroups;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PathConfig [name=");
		builder.append(name);
		builder.append(", keyStepSet=");
		builder.append(keyStepSet);
		builder.append(", noKeyStepSet=");
		builder.append(noKeyStepSet);
		builder.append(", identifyStep=");
		builder.append(identifyStep);
		builder.append(", orderTrees=");
		builder.append(orderTrees);
		builder.append("]");
		return builder.toString();
	}

}
