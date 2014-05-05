/**
 * <b>包名：</b>com.msgsrv.log.analyzer.client.task<br/>
 * <b>文件名：</b>AnalyzerLogFileTaskTest.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-24-下午2:50:50<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.client.task;

import org.junit.Assert;
import org.junit.Test;

import com.msgsrv.log.analyzer.client.entity.LogInfo;
import com.msgsrv.log.analyzer.server.entity.StepVo;

/**
 * 
 * <b>类名称：</b>AnalyzerLogFileTaskTest<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-24 下午2:50:50<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class AnalyzerLogFileTaskTest {

	@Test
	public void test100() throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316192041.976947:(R)OrderKeep.2b.a91e.22MsgSrv6004->...22MsgSrv6004|Broker2 NewTask 66013570|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "100");
	}
	@Test
	public void test101()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316192041.977267:(S)...22MsgSrv6004->Broker2.73.7f4d.22MsgSrv6004|OrderKeep.2b.a91e.22MsgSrv6004 NewTask 66013570|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "101");
	}
	
	@Test
	public void test102()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092656.217725:(R)Broker2.9e.f109.20MsgSrv6004->...20MsgSrv6004|IFTran TranSlowInt 0 20139320521593241 2000000005 200000000000005 OEM20139320517354316 1 5000 110 江苏 22 NA NA 18861379001 20140224092656 NA 连云港|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "102");
	}
	
	@Test
	public void test103()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092656.218047:(S)...20MsgSrv6004->IFTran.ab.abf.20MsgSrv6004|Broker2.9e.f109.20MsgSrv6004 TranSlowInt 0 20139320521593241 2000000005 200000000000005 OEM20139320517354316 1 5000 110 江苏 22 NA NA 18861379001 20140224092656 NA 连云港|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "103");
	}
	
	
	@Test
	public void test104()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092655.999575:(R)IFTran.ab.abf.20MsgSrv6004->...20MsgSrv6004|Broker2 SlowInt 1 20139320521527733 2000000017 200000000000017 RT041342732756920140224092600566 1 5000 110 广东 22 NA NA 13923691767 20140224092655 NA 河源 NA 5000 20140224092601 20140224092655 NA |");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "104");
	}
	
	@Test
	public void test105()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092655.999905:(S)...20MsgSrv6004->Broker2.9e.f109.20MsgSrv6004|IFTran.ab.abf.20MsgSrv6004 SlowInt 1 20139320521527733 2000000017 200000000000017 RT041342732756920140224092600566 1 5000 110 广东 22 NA NA 13923691767 20140224092655 NA 河源 NA 5000 20140224092601 20140224092655 NA |");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "105");
	}
	

	@Test
	public void test106()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316194654.114898:(R)Broker2.73.7f4d.22MsgSrv6004->...22MsgSrv6004|SchMaster NewTask 30977027 22139497040362669 22139497041304884|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "106");
	}
	
	@Test
	public void test107()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316194654.115227:(S)...22MsgSrv6004->SchMaster.65.430e.22MsgSrv6004|Broker2.73.7f4d.22MsgSrv6004 NewTask 30977027 22139497040362669 22139497041304884|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		System.out.println(vo.getSn());
		//Assert.assertEquals(vo.getStep(), "107");
	}
	
	@Test
	public void test108()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092656.514836:(R)IFTran.ab.abf.20MsgSrv6004->...20MsgSrv6004|CallBack SlowInt 1 20139320520412901 NA NA NA NA 10000 NA NA 33 NA NA 13978051762 20140224092656 NA NA NA 10000 20140224092640 20140224092656 NA NA 充值成功 |");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "108");
	}
	
	@Test
	public void test109()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092656.515164:(S)...20MsgSrv6004->CallBack.13.9740.20MsgSrv6004|IFTran.ab.abf.20MsgSrv6004 SlowInt 1 20139320520412901 NA NA NA NA 10000 NA NA 33 NA NA 13978051762 20140224092656 NA NA NA 10000 20140224092640 20140224092656 NA NA 充值成功 |");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "109");
	}
	
	@Test
	public void test110()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316194757.859374:(R)SchMaster.65.430e.22MsgSrv6004->...22MsgSrv6004|MsgDispatcher TaskFin 30977027 66017039|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "110");
	}
	@Test
	public void test111()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316194757.859705:(S)...22MsgSrv6004->MsgDispatcher.5b.4eb1.22MsgSrv6004|SchMaster.65.430e.22MsgSrv6004 TaskFin 30977027 66017039|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "111");
	}
	@Test
	public void test112()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316194757.860332:(R)MsgDispatcher.5b.4eb1.22MsgSrv6004->...22MsgSrv6004|RcgRst TaskFin 30977027 66017039|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "112");
	}
	@Test
	public void test113()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140316194757.860659:(S)...22MsgSrv6004->RcgRst.22.4ab0.22MsgSrv6004|MsgDispatcher.5b.4eb1.22MsgSrv6004 TaskFin 30977027 66017039|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "113");
	}
	
	@Test
	public void test114()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092656.403125:(R)RcgRst.b9.10b2.20MsgSrv6004->...20MsgSrv6004|CallBack RcgRstFin 117159589|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "114");
	}
	
	@Test
	public void test115()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092656.403465:(S)...20MsgSrv6004->CallBack.13.9740.20MsgSrv6004|RcgRst.b9.10b2.20MsgSrv6004 RcgRstFin 117159589|");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "115");
	}
	
	@Test
	public void test116()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092712.827191:(R)ReCallBack.9d.5c2.20MsgSrv6004->...20MsgSrv6004|HttpCaller SlowInt 1 20139320519484036 1000000156 100000000000156 64007584A010023557504797995005 1 10000 3 内蒙古 12 NA NA 13694781751 20140224092712 http://interfaces.cdzlxt.com/callback/c007ka.do NA 9 10000 20140224092712 20140224092712 1 20140224 \"Success\" |");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "116");
	}
	
	@Test
	public void test117()throws Exception {
		AnalyzerLogFileTask task = new AnalyzerLogFileTask(null, null, null);
		LogInfo log = task.isValidLog("20140224092712.827506:(S)...20MsgSrv6004->HttpCaller.7e.562.20MsgSrv6004|ReCallBack.9d.5c2.20MsgSrv6004 SlowInt 1 20139320519484036 1000000156 100000000000156 64007584A010023557504797995005 1 10000 3 内蒙古 12 NA NA 13694781751 20140224092712 http://interfaces.cdzlxt.com/callback/c007ka.do NA 9 10000 20140224092712 20140224092712 1 20140224 \"Success\" |");
		StepVo vo = task.analysisLog(log);
		Assert.assertNotNull(vo);
		Assert.assertEquals(vo.getStep(), "117");
	}
	
}
