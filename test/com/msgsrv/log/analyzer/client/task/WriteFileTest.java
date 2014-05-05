package com.msgsrv.log.analyzer.client.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.msgsrv.log.analyzer.common.DateUtil;

public class WriteFileTest {

	@Test
	public void testFileOutputStream() throws Exception {
		String sqlFileName = "sql/" + DateUtil.format(new Date()) + ".sql";
		File sqlFile = new File(sqlFileName);
		sqlFileName = sqlFile.getAbsolutePath().replaceAll("\\\\", "/");
		FileOutputStream outStream = new FileOutputStream(sqlFile, true);
		for (int i = 0; i < 1000000; i++) {
			String sql = "'"
					+ UUID.randomUUID()
					+ "','query','5','20130704000002.703155','20130704000002.712536','0','9381','0','1','(R) * to Distributor|(S) Distributor to *','2014-02-13 10:38:54'\n";
			outStream.write(sql.getBytes());
		}
		outStream.flush();
		outStream.close();
	}

	@Test
	public void testBufferedWriter() throws Exception {
		String sqlFileName = "sql/" + DateUtil.format(new Date()) + ".sql";
		File sqlFile = new File(sqlFileName);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sqlFile));
		for (int i = 0; i < 1000000; i++) {
			String sql = "'"
					+ UUID.randomUUID()
					+ "','query','5','20130704000002.703155','20130704000002.712536','0','9381','0','1','(R) * to Distributor|(S) Distributor to *','2014-02-13 10:38:54'\n";
			bufferedWriter.write(sql);
		}
		bufferedWriter.flush();
		bufferedWriter.close();
	}
}
