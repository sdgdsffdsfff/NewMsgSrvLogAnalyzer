package com.msgsrv.log.analyzer.client.task;

import java.io.File;

public class FileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("F:/MsgSrvLog/6004.tar.gz");
		System.out.println("字节：" + file.length());
		System.out.println("大小：" + file.length() / 1024 / 1024 + "M");
	}

}
