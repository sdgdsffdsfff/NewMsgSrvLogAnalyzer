package com.msgsrv.log.analyzer.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiThreadExecutors {

	private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

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
