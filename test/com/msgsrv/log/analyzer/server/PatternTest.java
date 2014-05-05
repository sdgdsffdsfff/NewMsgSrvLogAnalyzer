package com.msgsrv.log.analyzer.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pattern p = Pattern.compile("insert into oemcdkey_order[(]id,merid,meraccount,orderid,command,rettype,chargenum,amount,ordtime,recvtime,attach,checkdate,merurl,inorderid,timeout,endtime,cardtype,province,cardsn,cardkey,meruserid,isReq,retcode,dnote[)] values[(]null,'([0-9]{10})','[0-9]{15}','([^,]+)',.*");
		Matcher matcher = p.matcher("insert into oemcdkey_order(id,merid,meraccount,orderid,command,rettype,chargenum,amount,ordtime,recvtime,attach,checkdate,merurl,inorderid,timeout,endtime,cardtype,province,cardsn,cardkey,meruserid,isReq,retcode,dnote) values(null,'1100000006','100000100000006','15318210518908628826',9,2,'15318210518',50,'20140224090315',now(),'7.1393203830','','','20139320383067588',120,'2014-02-24 09:05:50',0,'','','','62270022600256****3',0,0,'')");
		if(matcher.matches() && matcher.groupCount() >= 2){
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
		}
	}

}
