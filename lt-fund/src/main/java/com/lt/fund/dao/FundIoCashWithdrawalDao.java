package com.lt.fund.dao;

import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.product.ExchangeHoliday;

public interface FundIoCashWithdrawalDao {

	/**
	 * 保存提现信息
	 * @param fundIoCashWithdrawal
	 */
	public Integer addFundIoCashWithdrawal(FundIoCashWithdrawal fundIoCashWithdrawal);
	
	/**
	 * 修改提现信息	 
	 * status 
	 * userId 
	 * remark 
	 * modifyUserId 
	 * doneDate 
	 * auditDate
	 * @param fundIoCashWithdrawal
	 */
	public void updateFundIoCashWithdrawal(FundIoCashWithdrawal fundIoCashWithdrawal);
	
	/**
	 * 查询提现信息
	 * @param id
	 * @return
	 */
	public FundIoCashWithdrawal queryFundIoCashWithdrawalById(Long id);
	
	/**
	 * 查询当日提现次数
	 * @param userId
	 * @return
	 */
	public Integer getTotayWithdrawCount(String userId);
	
	/**
	 * 查询提现时间在上海交易所假期时间范围内的最大假期结束时间
	 * @param widthdrawTime
	 * @return
	 */
	public String selectHolidays(String widthdrawTime);
	
	/**
	 * 查询出金记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectWithdrawPage(Map<String,Object> map);
	
	/**
	 * 查询出金明细
	 * @param ioId
	 * @return
	 */
	public Map<String,Object> selectFioAndDetail(Long ioId);
	
	/**
	 *  查询出金明细
	 * @param ioId
	 * @return
	 */
	public Map<String,Object> selectFioAndBank(Long ioId);
}
