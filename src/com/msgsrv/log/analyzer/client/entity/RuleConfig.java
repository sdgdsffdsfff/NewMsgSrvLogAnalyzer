package com.msgsrv.log.analyzer.client.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;

import com.msgsrv.log.analyzer.common.StringUtil;

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
public class RuleConfig {

	private static final Logger LOGGER = Logger.getLogger(RuleConfig.class);

	@XmlAttribute
	private String step = "";
	@XmlAttribute
	private String dir = "";
	@XmlAttribute
	private String src = "";
	@XmlAttribute
	private String dst = "";
	@XmlAttribute
	private String cmd = "";
	@XmlAttribute
	private long timeout = 0;
	@XmlElement
	private ContentConfig content;

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public ContentConfig getContent() {
		return content;
	}

	public void setContent(ContentConfig content) {
		this.content = content;
	}

	public boolean baseMatch(LogInfo log) {
		if (log == null) {
			return false;
		}

		if (this.dir.equals(log.getDir()) && this.cmd.equals(log.getCmd())) {
			if ("*".equals(this.src) && this.dst.equals(log.getDst())) {
				return true;
			} else if ("*".equals(this.dst) && this.src.equals(log.getSrc())) {
				return true;
			} else if (this.src.equals(log.getSrc()) && this.dst.equals(log.getDst())) {
				return true;
			}
		}
		return false;
	}

	public String getKey(LogInfo log) {
		String[] content = log.getContents();
		if(content==null){
			content = log.getContent().split(" +");
			log.setContents(content);
		}
		List<KeyConfig> keys = this.getContent().getKeys();
		List<String> identifyValues = new ArrayList<String>(keys.size());
		for (int j = 0; j < keys.size(); j++) {
			String identifyValue = "";
			KeyConfig kc = keys.get(j);
			String[] indexs = kc.getIndexs();
			Map<Integer, int[]> subs = new HashMap<Integer, int[]>();// 存储查分规则
			if (!StringUtil.isEmpty(kc.getSub())) {
				String[] subStrings = kc.getSub().split(";");
				for (int i = 0; i < subStrings.length; i++) {
					String s = subStrings[i];
					String index = s.substring(0, s.indexOf(":"));

					String args = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
					String[] s2 = args.split(",");
					if (s2.length == 1) {
						subs.put(Integer.parseInt(index), new int[] { Integer.parseInt(s2[0]) });
					} else {
						subs.put(Integer.parseInt(index), new int[] { Integer.parseInt(s2[0]), Integer.parseInt(s2[1]) });
					}
				}
			}

			for (int i = 0; i < indexs.length; i++) {
				try {
					// 如果key中存在的是s
					if ("s".equals(indexs[i])) {
						if (i == (indexs.length - 1)) {
							identifyValue = identifyValue + log.getFullSrc();
						} else {
							identifyValue = identifyValue + log.getFullSrc() + "_";
						}
						continue;
					} else if ("d".equals(indexs[i])) {
						if (i == (indexs.length - 1)) {
							identifyValue = identifyValue + log.getFullDst();
						} else {
							identifyValue = identifyValue + log.getFullDst() + "_";
						}
						continue;
					}
					int keyIndex = Integer.parseInt(indexs[i]);
					if (content.length >= keyIndex) {
						// 判断KEY是否需要截取
						int[] ks = subs.get(keyIndex);
						if (ks != null) {
							if (ks.length == 1) {
								content[keyIndex] = content[keyIndex].substring(ks[0]);
							} else {
								content[keyIndex] = content[keyIndex].substring(ks[0], ks[1]);
							}
						}
						if (i == (indexs.length - 1)) {
							identifyValue = identifyValue + content[keyIndex];
						} else {
							identifyValue = identifyValue + content[keyIndex] + "_";
						}
					}
				} catch (Exception ignore) {
				}
			}
			// 判断是否需要添加前缀
			if("0".equals(identifyValue)){
				continue;
			}
			if (!StringUtil.isEmpty(kc.getPrefixs())) {
				identifyValue = kc.getPrefixs() + "@" + identifyValue;
			}
			identifyValues.add(identifyValue);
		}
		String identifyValue = "";
		for (int i = 0; i < identifyValues.size(); i++) {
			String value = identifyValues.get(i);
			identifyValue = identifyValue + value + ",";
		}

		if (identifyValue.lastIndexOf(",") > 0) {
			return identifyValue.substring(0, identifyValue.lastIndexOf(","));
		}
		return identifyValue;
	}

	public boolean getExactFlag(String[] content) {
		boolean exactFlag = true;
		if (this.getContent().getEigenvalue() != null) {
			// 匹配其它特征值
			EigenValueConfig eigenvalue = this.getContent().getEigenvalue();
			if (eigenvalue.getLength() != 0) {
				if (content.length != eigenvalue.getLength()) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("[content=" + Arrays.asList(content) + "]长度不符合[content.length = " + content.length + "][特征值长度：" + eigenvalue.getLength() + "]");
					}
					exactFlag = false;
				}
			}
			if (eigenvalue.getIndexs() != null && !"".equals(eigenvalue.getIndexs())) {
				String indexsStr = eigenvalue.getIndexs();
				if (eigenvalue.getValues() != null && !"".equals(eigenvalue.getValues())) {
					String valuesStr = eigenvalue.getValues();
					String[] indexs = indexsStr.split(",");
					String[] values = valuesStr.split(",");

					if (indexs.length == values.length) {
						for (int i = 0; i < indexs.length; i++) {
							try {
								int keyIndex = Integer.parseInt(indexs[i]);
								if (content.length >= keyIndex) {
									if (!content[keyIndex].equals(values[i])) {
										if (LOGGER.isDebugEnabled()) {
											LOGGER.debug("[content=" + Arrays.asList(content) + "][index=" + keyIndex + "]值[values=" + Arrays.asList(values) + "][index=" + i + "]不相等");
										}
										exactFlag = false;
									}
								}
							} catch (Exception e) {
							}
						}

					}
				}
			}
		}
		return exactFlag;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RuleConfig [step=");
		builder.append(step);
		builder.append(", dir=");
		builder.append(dir);
		builder.append(", src=");
		builder.append(src);
		builder.append(", dst=");
		builder.append(dst);
		builder.append(", cmd=");
		builder.append(cmd);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}

	public long getTimeout() {
		return timeout;
	}
}
