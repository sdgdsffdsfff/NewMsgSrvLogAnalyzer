package com.msgsrv.log.analyzer.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class YhbfShell {

	private static final Logger log = Logger.getLogger(YhbfShell.class);

	private static String commandPath = "/usr/local/007ka/bin/yhbf";

	// ///////////////////////
	// private public yhbf_shell(String command_type,String cardkey
	// 调用卡密加密解密shell
	// Input:
	// command_type:命令的类型，E：加密，D：解密
	// cardkey:传入密码的明文或密文，当command_type.equals("E"),cardkey则为明文，当command_type.equals("D"),cardkey则为密文
	// Output:
	//
	// return:
	// cardkey:经过加密或解密的密码的明文或密文，当command_type.equals("E"),cardkey则为密文，当command_type.equals("D"),cardkey则为明文
	// last change by los 2009-03-18
	// ////////////////////////
	public static String yhbf_shell(String command_type, String cardkey) {
		String command = commandPath + " " + command_type + " " + cardkey;
		String line = null;
		try {
			Process pro = Runtime.getRuntime().exec(command);
			// Process类是一个抽象类，方法都是抽象的，它封装了一个进程，也就是一个可执行的程序
			// 类提供进程的输入、执行输出到进程、等待进程的完成和检查进程的退出状态及销毁进程的方法
			// ProcessBuilder.start()和Runtime.exec方法创建一个本机进程并返回Process子类的一个实例，该实例可以控制进程并获取相关的信息
			BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			cardkey = "";
			while ((line = buf.readLine()) != null)
				cardkey = cardkey + line;
			buf.close();
			pro.destroy();
		} catch (Exception ex) {
			log.warn(ex);
		}
		return cardkey;
	}

	public static String decoder(String encode) {
		String command = commandPath + "  D " + encode;
		Process p = null;
		InputStreamReader in = null;
		BufferedReader bf = null;
		try {
			p = Runtime.getRuntime().exec(command);
			// Process类是一个抽象类，方法都是抽象的，它封装了一个进程，也就是一个可执行的程序
			// 类提供进程的输入、执行输出到进程、等待进程的完成和检查进程的退出状态及销毁进程的方法
			// ProcessBuilder.start()和Runtime.exec方法创建一个本机进程并返回Process子类的一个实例，该实例可以控制进程并获取相关的信息
			in = new InputStreamReader(p.getInputStream());
			bf = new BufferedReader(in);
			String ciphertext = "";
			String line = null;
			while ((line = bf.readLine()) != null)
				ciphertext = ciphertext + line;
			encode = ciphertext;
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (p != null) {
				p.destroy();
			}

		}
		return encode;
	}

	public static String encode(String decode) {
		String command = commandPath + " E " + decode;
		Process p = null;
		InputStreamReader in = null;
		BufferedReader bf = null;
		try {
			p = Runtime.getRuntime().exec(command);
			// Process类是一个抽象类，方法都是抽象的，它封装了一个进程，也就是一个可执行的程序
			// 类提供进程的输入、执行输出到进程、等待进程的完成和检查进程的退出状态及销毁进程的方法
			// ProcessBuilder.start()和Runtime.exec方法创建一个本机进程并返回Process子类的一个实例，该实例可以控制进程并获取相关的信息
			in = new InputStreamReader(p.getInputStream());
			bf = new BufferedReader(in);
			String ciphertext = "";
			String line = null;
			while ((line = bf.readLine()) != null)
				ciphertext = ciphertext + line;
			decode = ciphertext;
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (p != null) {
				p.destroy();
			}

		}
		return decode;
	}
}