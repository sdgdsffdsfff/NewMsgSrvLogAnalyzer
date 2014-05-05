package com.msgsrv.log.analyzer.client.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.msgsrv.log.analyzer.common.StringUtil;

@XmlAccessorType(XmlAccessType.NONE)
public class EigenValueConfig {

	@XmlAttribute
	private int length;
	@XmlAttribute
	private String indexs;
	@XmlAttribute
	private String values;
	@XmlAttribute(name = "EigenValueIndex")
	private String eigenValueIndex;
	private int[] eigenValueIndexs = null;;

	@XmlAttribute
	private String match = "";

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getIndexs() {
		return indexs;
	}

	public void setIndexs(String indexs) {
		this.indexs = indexs;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public int[] getEigenValueIndex() {
		if (eigenValueIndexs == null) {
			if (StringUtil.isNotEmpty(eigenValueIndex)) {
				String[] indexs = eigenValueIndex.split(",");
				eigenValueIndexs = new int[indexs.length];
				for (int i = 0; i < indexs.length; i++) {
					eigenValueIndexs[i] = Integer.parseInt(indexs[i]);
				}
				return eigenValueIndexs;
			}
		}
		return eigenValueIndexs;
	}

	public void setEigenValueIndex(String eigenValueIndex) {
		this.eigenValueIndex = eigenValueIndex;
	}
}
