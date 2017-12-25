package com.lt.fund.dao;

import com.lt.model.fund.FundIoScoreWithdrawal;

public interface FundIoScoreWithdrawalDao {

	/**
	 * 保存提现信息
	 * @param fundIoScoreWithdrawal
	 */
	public void addFundIoScoreWithdrawal(FundIoScoreWithdrawal fundIoScoreWithdrawal);
	
	/**
	 * 修改提现信息	 
	 * status 
	 * userId 
	 * remark 
	 * modifyUserId 
	 * doneDate 
	 * auditDate
	 * @param fundIoScoreWithdrawal
	 */
	public void updateFundIoScoreWithdrawal(FundIoScoreWithdrawal fundIoScoreWithdrawal);
	
	/**
	 * 查询提现信息
	 * @param id
	 * @return
	 */
	public FundIoScoreWithdrawal queryFundIoScoreWithdrawalById(Long id);
}
