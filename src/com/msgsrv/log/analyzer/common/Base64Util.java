package com.msgsrv.log.analyzer.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	private static final Base64 base64 = new Base64();

	private static final String DEFAULT_CHARSET = "GBK";

	public static String encode(String str) {
		return encode(str, DEFAULT_CHARSET);
	}

	public static String encode(String str, String charsetName) {
		String encodeSql = null;
		try {
			byte[] enbytes = base64.encode(str.getBytes(charsetName));
			encodeSql = new String(enbytes);
		} catch (UnsupportedEncodingException e) {
		}
		return encodeSql;
	}

	public static String decode(String str) {
		return decode(str, DEFAULT_CHARSET);
	}

	public static String decode(String str, String charsetName) {
		byte[] debytes = base64.decode(str.getBytes());
		String decodeSql = null;
		try {
			decodeSql = new String(debytes, charsetName);
		} catch (UnsupportedEncodingException e) {
		}
		return decodeSql;
	}

	public static byte[] decodeBytes(String str) {
		byte[] debytes = base64.decode(str.getBytes());
		return debytes;
	}

	public static String encodeToString(byte[] binaryData) {
		return base64.encodeToString(binaryData);
	}

}
