package com.msgsrv.log.analyzer.common;

import java.util.Calendar;
import java.util.Date;

public class TimeUitl {

	public static final long DAYS = 1000 * 60 * 60 * 24;
	public static final long HOURS = 1000 * 60 * 60;
	public static final long MINUTES = 1000 * 60;
	public static final long SECONDS = 1000;

	public static long nextHourTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monday = calendar.get(Calendar.MONDAY);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONDAY, monday);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour + 1);
		return calendar.getTimeInMillis();
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monday = calendar.get(Calendar.MONDAY);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		System.out.println(year);
		System.out.println(monday);
		System.out.println(day);
		System.out.println(hour);
		System.out.println(calendar);
		System.err.println(System.currentTimeMillis());
		System.err.println(nextHourTime());
	}

	public static boolean isWeeHours(Date date) {
		if (date == null) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		if (hour == 0 && minute == 0 && second == 0) {
			return true;
		}
		return false;
	}
}
