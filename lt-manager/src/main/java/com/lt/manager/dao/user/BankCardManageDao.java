package com.lt.manager.dao.user;

import java.util.List;

import com.lt.manager.bean.user.BankCard;

/**
 * 商品类型管理dao
 * @author licy
 *
 */
public interface BankCardManageDao {

	
	/**
	 * 添加银行卡
	 * @param param
	 */
	public void insertBankCard(BankCard param);
	
	/**
	 * 修改银行卡
	 * @param param
	 */
	public void updateBankCard(BankCard param);
	
	/**
	 * 修改银行卡默认值
	 */
	public void updateBankCardDefaultByUserId(String userId);
	/***
	 * 删除银行卡
	 * @param param
	 */
	public void deleteBankCard(BankCard param);
	
	
	public List<BankCard>  selectBankCardByUserId(String userId);
	
	
	public List<BankCard> selectBankInfo();
	
	public List<BankCard> selectProv();
	
	public List<BankCard> selectCity(String provinceId);
	
	public List<BankCard> selectBranchBank(String cityId,String bankCode);
	
}
