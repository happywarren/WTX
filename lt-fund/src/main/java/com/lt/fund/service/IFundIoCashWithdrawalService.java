package com.lt.fund.service;

import java.util.Map;

import com.lt.enums.fund.IFundOptCode;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.util.error.LTException;

public interface IFundIoCashWithdrawalService {
	/**
	 * 申请提现
	 * @param userId 用户id
	 * @param outAmount  提现金额
	 * @param flag 是否需要扣手续费 
	 */
	FundIoCashWithdrawal doRequestWithdraw(String userId, Double outAmount,Double rate,
											String withdrawCode,String faxCode, boolean flag, boolean backgroundFlag) throws Exception;


	/**
	 * 审核操作
	 * @param id
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 */
	void doAudit(Long id,String modifyUserId, Integer status,
			IFundOptCode thirdOptCode,Double fact_tax, String remark);
	

	/**
	 * 提现申请
	 * @param userId
	 * @param outAmount
	 * @param sign 操作标记
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> doWithdrawApply(String userId,Double outAmount,Double rate,
										String withdrawCode,String faxCode,boolean flag,String sign) throws LTException;
	
	/**
	 * 提现成功
	 * @param fio
	 * @throws Exception
	 */
	public void withdrawSuccess(FundIoCashWithdrawal fio,String thirdOptCode) throws Exception ;
	
	/**
	 * 提现失败
	 * @param fio
	 * @throws Exception
	 */
	public void withdrawFail(FundIoCashWithdrawal fio) throws Exception ;
	
	/**
	 * 提现撤销
	 * @param userId
	 * @param ioId
	 * @throws LTException
	 */
	public void WithdrawCancel(String userId,Long ioId) throws LTException;


}
