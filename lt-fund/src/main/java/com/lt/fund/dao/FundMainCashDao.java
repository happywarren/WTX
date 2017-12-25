/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.fund.dao
 * FILE    NAME: FundMainCashDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.fund.dao;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;

import com.lt.model.fund.FundMainCash;

/**
 * TODO 现金主账户数据接口
 * @author XieZhibing
 * @date 2016年11月30日 下午3:32:24
 * @version <b>1.0.0</b>
 */
public interface FundMainCashDao extends Serializable {
	
	/**
	 * 
	 * TODO 初始化现金主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:46:19
	 * @param userId
	 * @return
	 */
	public void initFundMainCash(String userId);
	/**
	 * 
	 * TODO 删除现金主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:46:10
	 * @param userId
	 * @return
	 */
	public int deleteFundMainCash(String userId);	
	/**
	 * 
	 * TODO 更新资金主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:46:01
	 * @param fundMainCash
	 * @return
	 */
	public int updateFundMainCash(FundMainCash fundMainCash);
	
	/**
	 * 
	 * TODO 查询现金主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:45:33
	 * @param userId
	 * @return
	 */
	public FundMainCash queryFundMainCash(String userId);
	/**
	 * 
	 * TODO 查询现金主账户(行锁)
	 * @author XieZhibing
	 * @date 2016年12月6日 下午6:47:34
	 * @param userId
	 * @return
	 */
	public FundMainCash queryFundMainCashForUpdate(String userId);
	
	/**
	 * 
	 * TODO 查询积分主账户余额是否可供开仓
	 * @author XieZhibing
	 * @date 2016年12月9日 下午3:38:27
	 * @param userId
	 * @param amount
	 * @return
	 */
	public Integer queryFundMainCashBalance(@Param("userId")String userId, @Param("amount")double amount);
}
