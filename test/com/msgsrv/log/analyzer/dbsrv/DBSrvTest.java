package com.msgsrv.log.analyzer.dbsrv;

import com.msgsrv.log.analyzer.core.MsgSrvService;
import com.nnk.msgsrv.client.core.MsgSrvManager;

public class DBSrvTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String message = "DBServerRecv NewNNKQuery 7af07500-df01-4195-99b7-26715ac45feb \"INSERT INTO OrderTimeLog(identifyValue,path,orderIndex,beginTime,endTime,selfUseTime,useTime,parentId,exactFlag,content,insert_time) VALUES('1000000212_01010140219000002','OrderReceiver','37','20140219000017.299407','20140219000017.299407','0','6873','0','0','R NewOrderReceiver to NewMyCartDBSrv;S NewMyCartDBSrv to NewOrderReceiver',now());\" 0 6";
		MsgSrvManager.start("config/msgsrv.xml");
		Thread.sleep(1000);
		MsgSrvService.send(message);
	}

}
