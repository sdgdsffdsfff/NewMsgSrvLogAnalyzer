package com.msgsrv.log.analyzer.client.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * <b>类名称：</b>KeyConfig<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-20 上午8:44:08<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class KeyConfig {

	@XmlAttribute
	private String index;
	private String[] indexs;
	@XmlAttribute
	private String prefixs;
	@XmlAttribute
	private String sub;

	public String[] getIndexs() {
		if(indexs==null){
			indexs = index.split(",");
		}
		return indexs;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getPrefixs() {
		return prefixs;
	}

	public void setPrefixs(String prefixs) {
		this.prefixs = prefixs;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}
}
