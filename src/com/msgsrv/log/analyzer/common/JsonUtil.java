package com.msgsrv.log.analyzer.common;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	public static Map<String, String> buildMap(String json) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, String> map = g.fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());
		return map;
	}

	public static String buildJson(Object object) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		return g.toJson(object);
	}

	public static <T> T buildObject(String json, Class<T> clazz) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		return g.fromJson(json, clazz);
	}

}
