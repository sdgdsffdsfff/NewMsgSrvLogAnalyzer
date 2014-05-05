package com.msgsrv.log.analyzer.common;

import com.nnk.dbsrv.DatabaseManager;

public final class DBSrvUtils {

	private static DatabaseManager dbManager;

	private DBSrvUtils() {

	}

	public static DatabaseManager getDatabaseManager() {
		if (dbManager == null) {
			synchronized (DBSrvUtils.class) {
				if (dbManager == null) {
					String path = "config/dbsrv.properties";
					dbManager = new DatabaseManager(path);
				}
			}
		}
		return dbManager;
	}
}
