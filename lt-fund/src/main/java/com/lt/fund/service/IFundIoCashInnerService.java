package com.lt.fund.service;

import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundIoCashInner;

public interface IFundIoCashInnerService {

	/**
	 * 现金内部存入
	 * @param userId
	 * @param freezeAmount
	 * @param thirdOptCode
	 */
	void doManualInByCash(String orderId,String userId, Double freezeAmount, String thirdOptCode,String remark,Double rmbAmount,Integer modifyUserId);
	/**
	 * 积分内部存入
	 * @param userId
	 * @param freezeAmount
	 * @param thirdOptCode
	 */
	void doManualInByScore(String orderId,String userId, Double freezeAmount, String thirdOptCode,String remark,Integer modifyUserId);

	/**
	 * 现金内部取出
	 * @param userId
	 * @param freezeAmount
	 * @param thirdOptCode
	 */
	void doManualOutCash(String orderId,String userId, Double freezeAmount, String thirdOptCode,String remark,Double rmbAmount,Integer modifyUserId);
	
	/**
	 * 积分内部取出
	 * @param userId
	 * @param freezeAmount
	 * @param thirdOptCode
	 */
	void doManualOutScore(String orderId,String userId, double freezeAmount, String thirdOptCode,String remark,Integer modifyUserId);

	/**
	 * 审核
	 * @param id
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 */
	void doCashAudit(Long id, Integer status, String remark,Integer modifyUserId);
	
	/**
	 * 审核
	 * @param id
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 */
	void doScoreAudit(Long id, Integer status, String remark,Integer modifyUserId);


}
