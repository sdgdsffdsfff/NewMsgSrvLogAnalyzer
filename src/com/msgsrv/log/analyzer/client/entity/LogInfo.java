package com.msgsrv.log.analyzer.client.entity;

import java.text.ParseException;
import java.util.Date;

import com.msgsrv.log.analyzer.common.DateUtil;

/**
 * 
 * <b>类名称：</b>LogInfo<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-21 上午10:49:25<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class LogInfo {

	private String date;
	private String dir;
	private String src;
	private String fullSrc;
	private String dst;
	private String fullDst;
	private String cmd;
	private String content;
	private String[] contents;

	private String srcLogContent;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSrcLogContent() {
		return srcLogContent;
	}

	public void setSrcLogContent(String srcLogContent) {
		this.srcLogContent = srcLogContent;
	}

	public String getFullSrc() {
		return fullSrc;
	}

	public void setFullSrc(String fullSrc) {
		this.fullSrc = fullSrc;
	}

	public String getFullDst() {
		return fullDst;
	}

	public void setFullDst(String fullDst) {
		this.fullDst = fullDst;
	}

	public Date getLogDate() throws ParseException {
		String dateStr = date.substring(0, 14);
		return DateUtil.parse(dateStr);
	}

	public String[] getContents() {
		return contents;
	}

	public void setContents(String[] contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogInfo [date=");
		builder.append(date);
		builder.append(", dir=");
		builder.append(dir);
		builder.append(", src=");
		builder.append(src);
		builder.append(", fullSrc=");
		builder.append(fullSrc);
		builder.append(", dst=");
		builder.append(dst);
		builder.append(", fullDst=");
		builder.append(fullDst);
		builder.append(", cmd=");
		builder.append(cmd);
		builder.append(", content=");
		builder.append(content);
		builder.append(", srcLogContent=");
		builder.append(srcLogContent);
		builder.append("]");
		return builder.toString();
	}

}
