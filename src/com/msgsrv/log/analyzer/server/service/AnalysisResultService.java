/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.service<br/>
 * <b>文件名：</b>AnalysisResultService.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-25-上午10:12:08<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.service;

import java.util.Collection;
import java.util.List;

import com.msgsrv.log.analyzer.server.entity.OrderTimeLogVo;
import com.msgsrv.log.analyzer.server.entity.PathConfig;
import com.msgsrv.log.analyzer.server.entity.StepVo;

/**
 * 
 * <b>类名称：</b>AnalysisResultService<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-25 上午10:12:08<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public interface AnalysisResultService {

	void analysisResult(PathConfig pc, Collection<StepVo> stepVos);
	void analysisResult(PathConfig pc, Collection<StepVo> stepVos,List<OrderTimeLogVo> orderTimeLogVos);
	void analysisHandleResult(List<OrderTimeLogVo> orderTimeLogVos);
	void timeout(StepVo setpVo);
}
