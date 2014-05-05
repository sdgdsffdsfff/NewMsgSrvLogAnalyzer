/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.client.entity<br/>
 * <b>文件名：</b>AnluzerConfig.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-20-上午8:37:45<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.msgsrv.log.analyzer.common.XmlUtil;

/**
 * 
 * <b>类名称：</b>AnluzerConfig<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-20 上午8:37:45<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
@XmlRootElement(name = "rules")
@XmlAccessorType(XmlAccessType.NONE)
public class AnluzerServerConfig {

	@XmlElement(name = "path")
	private List<PathConfig> paths = new ArrayList<PathConfig>();

	private static AnluzerServerConfig anluzerServerConfig;

	private static final List<String> startSteps = new ArrayList<String>();

	private static final Map<String, PathConfig> startStepPaths = new HashMap<String, PathConfig>();

	public List<PathConfig> getPaths() {
		return paths;
	}

	public void setPaths(List<PathConfig> paths) {
		this.paths = paths;
	}

	public List<String> getStartSteps() {
		if (AnluzerServerConfig.getStartsteps().isEmpty()) {
			for (PathConfig pc : paths) {
				String[] steps = pc.getKeyStepSets();
				if (steps != null && steps.length > 0) {
					AnluzerServerConfig.getStartsteps().add(steps[0]);
				}
			}
		}
		return AnluzerServerConfig.getStartsteps();
	}

	public PathConfig getStartStepPath(String step) {
		if (startStepPaths.isEmpty()) {
			for (PathConfig pc : paths) {
				String[] steps = pc.getKeyStepSets();
				if (steps != null && steps.length > 0) {
					startStepPaths.put(steps[0], pc);
				}
			}
		}
		return startStepPaths.get(step);
	}

	/**
	 * 得到规则配置实例，只会加载一次config/rules目录下的配置文件
	 * 
	 * @return
	 */
	public synchronized static AnluzerServerConfig load() {
		if (anluzerServerConfig == null) {
			try {
				File configFile = new File("config/server.xml");
				anluzerServerConfig = XmlUtil.loadXmlFromFile(AnluzerServerConfig.class, configFile);
			} catch (FileNotFoundException e) {
			} catch (JAXBException e) {
			}
		}
		return anluzerServerConfig;
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

	public static List<String> getStartsteps() {
		return startSteps;
	}

}
