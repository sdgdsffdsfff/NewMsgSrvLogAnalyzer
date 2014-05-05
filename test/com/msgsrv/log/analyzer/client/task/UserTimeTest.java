package com.msgsrv.log.analyzer.client.task;

import org.junit.Test;

public class UserTimeTest {

	public static void main(String[] args) throws InterruptedException {
		long startTime = System.nanoTime();
		Thread.sleep(1000);
		long endTime = System.nanoTime();
		System.out.println((endTime - startTime)/1000000);
	}
	
	@Test
	public void test() throws Exception {
		for (int i = 0; i < 10000; i++) {
			long startTime = System.nanoTime();
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000);
		}
	}
}
