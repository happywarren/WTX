/*
 * PROJECT NAME: lt-fund
 * PACKAGE NAME: com.lt.fund.dao
 * FILE    NAME: FundFlowScoreDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.fund.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.model.fund.FundFlow;
import com.lt.vo.fund.FundFlowVo;

/**
 * TODO 积分流水数据接口
 * @author XieZhibing
 * @date 2016年11月30日 下午5:13:52
 * @version <b>1.0.0</b>
 */
public interface FundFlowScoreDao extends Serializable {

	/**
	 * 
	 * TODO 添加积分流水
	 * @author XieZhibing
	 * @date 2016年12月6日 下午9:45:15
	 * @param fundFlow
	 */
	public void addFundFlowScore(FundFlow fundFlow);
	
	/**
	 * 
	 * TODO 批量添加积分流水
	 * @author XieZhibing
	 * @date 2016年12月6日 下午9:50:01
	 * @param flowList
	 */
	public void addFundFlowScoreList(@Param("flowList") List<FundFlow> flowList);	

	/**
	 * 查询指定业务编码的订单流水汇总
	 * @param orderId
	 * @param userId
	 * @param List<secondOptcode> 二级资金业务码集合
	 * @return
	 */
	public Double queryFundFlowScoreSum(@Param("orderId") String orderId, 
			@Param("userId") String userId, @Param("secondOptcodeList") List<String> secondOptcodeList);

	/**
	 * 查询用户资金明细
	 * @param map
	 * @return
	 */
	public List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map);

	/**
	 * 根据订单查询订单资金明细
	 * @param map
	 * @return
	 */
	public List<FundFlowVo> findFundFlowListByOrder(Map<String, Object> map);
}
