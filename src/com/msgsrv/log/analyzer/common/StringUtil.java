package com.msgsrv.log.analyzer.common;

public class StringUtil {

	// 工具类，不需要实例化
	private StringUtil() {
		throw new AssertionError();
	}

	public static boolean isAnyEmpty(String... args) {
		if (args == null) {
			return true;
		}
		for (String string : args) {
			if (string == null && "".equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static boolean in(String src, String... args) {
		if (args == null) {
			return false;
		}
		if (src == null) {
			for (String str : args) {
				if (str == null) {
					return true;
				}
			}
		} else {
			for (String str : args) {
				if (src.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static String toString(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
}
