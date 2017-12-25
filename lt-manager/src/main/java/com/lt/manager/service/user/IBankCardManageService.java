package com.lt.manager.service.user;

import com.lt.util.utils.model.Response;

import java.util.List;

import com.lt.manager.bean.user.BankCard;
/**
 * 用户管理service
 * @author licy
 *
 */
public interface IBankCardManageService {

	/**
	 * 添加商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void addBankCard(BankCard param) throws Exception;
	
	/**
	 * 编辑商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editBankCard(BankCard param) throws Exception;

	/**
	 * 删除商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeBankCard(BankCard param) throws Exception;
	
	
	/**
	 *g根据用户信息查询银行卡
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public List<BankCard>  findBankCardByUserId(String userId);
	
	/**
	 * 银行信息查询
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public List<BankCard>  findBankInfo();
	
	/**
	 * 省份信息查询
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public List<BankCard>  findProInfo();
	
	public List<BankCard>  findCityInfo(String provinceId);
	
	public List<BankCard>  findBranchBank(String cityId,String bankCode);
}
