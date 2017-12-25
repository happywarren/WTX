package com.lt.manager.dao.fund;

import java.util.List;

import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundTransferDetail;

/**
 * 提现流水dao
 * @author jingwb
 *
 */
public interface FundIoCashWithdrawalDao {

	/**
	 * 查询提现流水
	 * @param id
	 * @return
	 */
	public FundIoCashWithdrawal selectFundIoCashWithdrawalById(Long id);
	
	/**
	 * 批量修改提现流水
	 * @param fio
	 */
	public void updateFundIoCashWithds(List<FundIoCashWithdrawal> list);
	
	/**
	 * 修改提现流水
	 * @param fio
	 */
	public void updateFundIoCashWithd(FundIoCashWithdrawal fio);
	
	/**
	 * 批量生成提现明细
	 * @param list
	 */
	public void insertFundTransferDetails(List<FundTransferDetail> list);
	
	/**
	 * 批量生成提现明细
	 * @param list
	 */
	public void insertFundTransferDetail(FundTransferDetail fundTransferDetail);
	
	/**
	 * 查询提现明细(条件为提现流水id)
	 * @param ioId
	 * @return
	 */
	public FundTransferDetail selectTransferDetailByIoid(Long ioId);
	
	/**
	 * 查询提现明细(条件为外部订单号)
	 * @param ioId
	 * @return
	 */
	public FundTransferDetail selectTransferDetailByPayid(String payId);
	
	/**
	 * 修改转账明细
	 * @param ftd
	 */
	public void updateTransferDetail(FundTransferDetail ftd);
	
	/**
	 * 查询提现io集合
	 * @param fio
	 * @return
	 */
	public List<FundIoCashWithdrawal> selectFundIoCashWithds(FundIoCashWithdrawal fio);
	
	
	/**
	 * 查询提现id集合
	 * @param fio
	 * @return
	 */	
	public List<String> selectFundIoCashWithdalIds(FundIoCashWithdrawal fio);
	

	/**
	 * 查询出金明细(条件为外部订单号)
	 * @param payId
	 * @return
	 */	
	public FundIoCashWithdrawal selectFundIoCashWithdByPayIdForUpdate(String payId);
	
	/**
	 * 查询出金明细(条件为外部订单号)
	 * @param payId
	 * @return
	 */	
	public FundIoCashWithdrawal selectFundIoCashWithdByPayId(String payId);	
}
