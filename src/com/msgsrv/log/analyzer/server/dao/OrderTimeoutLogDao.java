/**
 * <b>项目名：</b>对账系统<br/>
 * <b>包名：</b>com.nnk.log.analuzer.server.dao<br/>
 * <b>文件名：</b>OrderTimeoutLogDao.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2013-6-25-上午9:39:41<br/>
 * <b>Copyright (c)</b> 2013 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.server.dao;

import java.util.List;

import com.msgsrv.log.analyzer.server.entity.StepVo;

/**
 * 
 * <b>类名称：</b>OrderTimeoutLogDao<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2013-6-25 上午9:39:41<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public interface OrderTimeoutLogDao {

	void insert(List<StepVo> setpVos);

	void insert(StepVo setpVo);
}
