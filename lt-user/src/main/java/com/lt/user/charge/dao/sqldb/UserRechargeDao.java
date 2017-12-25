package com.lt.user.charge.dao.sqldb;

import java.util.List;

import com.lt.model.fund.FundOptCode;

public interface UserRechargeDao {
	
	/**
	 * 根据渠道id查询渠道组id
	 * @param channelId
	 * @return
	 */
	public String selectGroupIdByChannelId(String channelId);
	
	
	/**
	 * 获取充值code
	 * @param thirdOptCode
	 * @return
	 */
	public FundOptCode selectFundOptCodebyThirdOptCode(String thirdOptCode);
	
	
	/**
	 * 查询支付渠道id
	 * @param groupId
	 * @return
	 */
	public List<String> selectChannelIdByGroupId(String groupId);
}
