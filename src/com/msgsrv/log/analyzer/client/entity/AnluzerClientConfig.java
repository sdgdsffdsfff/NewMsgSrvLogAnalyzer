package com.msgsrv.log.analyzer.client.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.msgsrv.log.analyzer.common.XmlUtil;

@XmlRootElement(name = "rules")
@XmlAccessorType(XmlAccessType.NONE)
public class AnluzerClientConfig {

	@XmlElement(name = "rule")
	private List<RuleConfig> rules = new ArrayList<RuleConfig>();

	private static AnluzerClientConfig anluzerClientConfig;

	/**
	 * 得到规则配置实例，只会加载一次config/rules目录下的配置文件
	 * 
	 * @return
	 */
	public synchronized static AnluzerClientConfig load() {
		if (anluzerClientConfig == null) {
			try {
				File configFile = new File("config/client.xml");
				anluzerClientConfig = XmlUtil.loadXmlFromFile(AnluzerClientConfig.class, configFile);
			} catch (FileNotFoundException e) {
			} catch (JAXBException e) {
			}
		}
		return anluzerClientConfig;
	}

	public List<RuleConfig> getRules() {
		return rules;
	}

	public void setRules(List<RuleConfig> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		String xml = "";
		try {
			xml = XmlUtil.toXml(this);
		} catch (JAXBException e) {
		} catch (IOException e) {
		}
		return xml;
	}

}
