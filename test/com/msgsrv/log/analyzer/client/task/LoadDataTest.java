package com.msgsrv.log.analyzer.client.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.msgsrv.log.analyzer.common.DateUtil;
import com.msgsrv.log.analyzer.server.dao.DBManage;

public class LoadDataTest {

	String sqlFileName = null;

	@Before
	public void testBefore() throws Exception {
		sqlFileName = "sql/" + DateUtil.format(new Date()) + ".sql";
		File sqlFile = new File(sqlFileName);
		sqlFileName = sqlFile.getAbsolutePath().replaceAll("\\\\", "/");
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sqlFile));
		for (int i = 0; i < 10000; i++) {
			String sql = "'"
					+ UUID.randomUUID()
					+ "','query','5','20130704000002.703155','20130704000002.712536','0','9381','0','1','(R) * to Distributor|(S) Distributor to *','2014-02-13 10:38:54'\n";
			bufferedWriter.write(sql);
		}
		bufferedWriter.flush();
		bufferedWriter.close();
		DBManage.getConnection();
	}

	@Test
	public void test() throws Exception {
		Statement statement = null;
		try {
			statement = DBManage.getConnection().createStatement();
			String sql = "load data infile '"
					+ sqlFileName
					+ "' replace into table OrderTimeLog character set GBK fields terminated by ',' enclosed by '\\'' LINES TERMINATED BY '\\n' (identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time)";
			System.out.println(sql);
			boolean result = statement.execute(sql);
			System.out.println(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
