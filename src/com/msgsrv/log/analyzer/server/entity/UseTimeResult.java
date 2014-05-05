package com.msgsrv.log.analyzer.server.entity;

public class UseTimeResult {
	private int useType = 0;
	private String identifyValue = "";
	private long useTime = 0;
	private String time = "0000-00-00 00:00:00";

	public UseTimeResult(int useType, String identifyValue, long useTime, String time) {
		super();
		this.useType = useType;
		this.identifyValue = identifyValue;
		this.useTime = useTime;
		this.time = time;
	}

	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}

	public String getIdentifyValue() {
		return identifyValue;
	}

	public void setIdentifyValue(String identifyValue) {
		this.identifyValue = identifyValue;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}