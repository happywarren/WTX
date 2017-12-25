package com.lt.business.core.service.promote;

import java.util.List;

import com.lt.model.promote.CommisionMain;
import com.lt.model.statistic.StatisticPromoterDayLog;

/**
 * 佣金接口
 * @author jingwb
 *
 */
public interface ICommisionService {

	/**
	 * 批量更新佣金账户
	 * @param list
	 * @throws Exception
	 */
	public void modifyCommisionMains(List<CommisionMain> list) throws Exception;
	
	/**
	 * 佣金结算
	 * @param list
	 * @throws Exception
	 */
	public void balanceCommision(List<StatisticPromoterDayLog> list) throws Exception;
	
	/**
	 * 佣金提现申请
	 * @param userId
	 * @param amount
	 * @throws Exception
	 */
	public void commisionWidthdrawApply(String userId,Double amount) throws Exception;
	
	/**
	 * 佣金提现审核通过
	 * @param ioId
	 * @throws Exception
	 */
	public void commisionWidthdrawPass(String ioId) throws Exception;
	
	/**
	 * 佣金提现审核拒绝
	 * @param ioId
	 * @throws Exception
	 */
	public void commisionWidthdrawNoPass(String ioId) throws Exception;
}
