package com.msgsrv.log.analyzer.server.entity;

public class AnluzerServerConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnluzerServerConfig load = AnluzerServerConfig.load();
		System.out.println(load);
	}

}
