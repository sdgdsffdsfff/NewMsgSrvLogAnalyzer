/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.client.core<br/>
 * <b>文件名：</b>Analyzer.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-7-16-下午4:19:17<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.msgsrv.log.analyzer.server.entity.StepVo;

/**
 * 
 * <b>类名称：</b>Analyzer<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-7-16 下午4:19:17<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class AnalyzerCoreMemory {

	public static volatile Date TIME_POINT = null;// 日志分析时间指针

	/**
	 * 存储KEY - 关键步骤
	 */
	public static final Map<String, List<StepVo>> KEY_RESULT_CACHE = new HashMap<String, List<StepVo>>();
	
	/**
	 * 存储MULTI-KEY - 关键步骤
	 */
	public static final Map<String, List<StepVo>> MULTI_KEY_RESULT_CACHE = new HashMap<String, List<StepVo>>();
	
	
	/**
	 * 存储 可选步骤 KEY - 关键步骤
	 */
	public static final List<StepVo> SELECTABLE_RESULT_CACHE = new ArrayList<StepVo>();
	

	/**
	 * 存储 KEY - KEY 的关联 此成员变量不再使用
	 */
	public static final Map<String, Set<String>> ASSOCIATE_KEYS_CACHE = new HashMap<String, Set<String>>();
	/**
	 * 存储 KEY - KEY 的关联
	 */
	public static final Cache<String, Set<String>> ASS_KEYS_CACHE = buildCache();// 命令对应的缓存信息

	private static Cache<String, Set<String>> buildCache() {
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
		builder.maximumSize(100000);
		builder.expireAfterWrite(10, TimeUnit.MINUTES);
		return builder.build();
	}

	public static Set<String> getAssKeys(String key) {
		Set<String> value = null;
		try {
			value = ASS_KEYS_CACHE.get(key, new Callable<Set<String>>() {
				@Override
				public Set<String> call() throws Exception {
					return new HashSet<String>();
				}
			});
		} catch (Exception e) {

		}
		if (value == null) {
			value = new HashSet<String>();
		}
		return value;
	}

	public static boolean containsKey(String key) {
		return ASS_KEYS_CACHE.asMap().containsKey(key);
	}

	/**
	 * 存储非KEY - 非关键步骤
	 */
	public static final Map<String, List<StepVo>> NOKEY_RESULT_CACHE = new HashMap<String, List<StepVo>>();

	/**
	 * 存储起始步骤 - 路径的起始步骤
	 */
	public static final List<StepVo> START_STEPS = new ArrayList<StepVo>();

	/**
	 * SQL 缓存
	 */
	public static final Queue<String> SQL_CACHE = new ConcurrentLinkedQueue<String>();
	
	/**
	 * load data 缓存
	 */
	public static final Queue<String> LOAD_DATA_CACHE = new ConcurrentLinkedQueue<String>();
}
