package com.lt.api.user;

import com.lt.model.user.InvestorProductConfig;
import com.lt.util.error.LTException;

public interface IInvestorProductConfigApiService {
	
	
	/**
	 * 根据商品Code 与所属券商信息 查询用户所属券商配置信息
	 * @param accountId
	 * @param productCode
	 * @return com.lt.model.user.InvestorProductConfig 券商商品配置model
	 * @throws LTException
	 */
	public InvestorProductConfig findInvestorProductConfig(String accountId,String productCode)throws LTException;
	
	
}
