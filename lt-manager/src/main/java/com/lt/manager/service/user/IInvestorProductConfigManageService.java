package com.lt.manager.service.user;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.InvestorProductConfig;
import com.lt.manager.bean.user.ProductAccountMapper;

import java.util.List;

/**
 * 券商商品配置管理service
 * @author licy
 *
 */
public interface IInvestorProductConfigManageService {

	public boolean batchAddInvestorProductConfig(String creater, List<InvestorProductConfig> investorProductConfigList);


	/**
	 * 券商商品配置
	 * @param investorProductConfig
	 */
	public void editInvestorProductConfig(InvestorProductConfig investorProductConfig);

	/**
	 * 删除券商商品配置
	 *
	 * @param id
	 */
	public void deleteInvestorProductConfig(String ids);

	public List<InvestorProductConfig> selectNoCheckProductList(InvestorProductConfig investorProductConfig);

	/***
	 * 券商商品配置列表
	 * @param investorProductConfig
	 */
	public List<InvestorProductConfig> findInvestorProductConfigList(InvestorProductConfig investorProductConfig);


	public Page<InvestorProductConfig> findInvestorProductConfig(InvestorProductConfig investorProductConfig);

	
}
