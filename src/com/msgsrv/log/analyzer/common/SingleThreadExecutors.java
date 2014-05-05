/**
 * <b>��Ŀ��</b>����ϵͳ<br/>
 * <b>����</b>com.nnk.account.common<br/>
 * <b>�ļ���</b>ExecutorsUtil.java<br/>
 * <b>�汾��Ϣ��</b>1.0<br/>
 * <b>���ڣ�</b>2013-4-8-����10:22:45<br/>
 * <b>Copyright (c)</b> 2013 ���������꿨����Ƽ����޹�˾-��Ȩ����<br/>
 * 
 */
package com.msgsrv.log.analyzer.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleThreadExecutors {

	private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	public static void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		service.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public static void scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit) {
		service.scheduleWithFixedDelay(command, initialDelay, period, unit);
	}

	public static void schedule(Runnable command, long delay, TimeUnit unit) {
		service.schedule(command, delay, unit);

	}

	public static void execute(Runnable command) {
		service.execute(command);
	}
}
