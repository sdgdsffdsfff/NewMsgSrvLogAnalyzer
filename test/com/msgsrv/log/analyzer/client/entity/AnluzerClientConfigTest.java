package com.msgsrv.log.analyzer.client.entity;

public class AnluzerClientConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnluzerClientConfig cc = AnluzerClientConfig.load();
		System.out.println(cc);
	}

}
