package com.msgsrv.log.analyzer.client.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.NONE)
public class ContentConfig {

	@XmlAttribute
	private String contentType = "fix-field";
	@XmlElement
	private EigenValueConfig eigenvalue;
	@XmlElement(name = "key")
	@XmlElementWrapper(name = "keys")
	private List<KeyConfig> keys = new ArrayList<KeyConfig>();

	@XmlElement(name = "multi-key")
	@XmlElementWrapper(name = "multi-keys")
	private List<MultiKeyConfig> multiKeys = new ArrayList<MultiKeyConfig>();

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public EigenValueConfig getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(EigenValueConfig eigenvalue) {
		this.eigenvalue = eigenvalue;
	}

	public List<KeyConfig> getKeys() {
		return keys;
	}

	public void setKeys(List<KeyConfig> keys) {
		this.keys = keys;
	}

	public List<MultiKeyConfig> getMultiKeys() {
		return multiKeys;
	}

	public void setMultiKeys(List<MultiKeyConfig> multiKeys) {
		this.multiKeys = multiKeys;
	}

	@Override
	public String toString() {
		return "ContentConfig [contentType=" + contentType + ", eigenvalue=" + eigenvalue + ", keys=" + keys + ", multiKeys=" + multiKeys + "]";
	}
}
