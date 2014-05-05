package com.msgsrv.log.analyzer.client.task;

import java.util.UUID;


public class Test {

	@org.junit.Test
	public void test() throws Exception {
		String string = UUID.randomUUID().toString();
		for (int i = 0; i < 100000; i++) {
			if(string.startsWith("[")){
				
			}
		}
		
	}
}
