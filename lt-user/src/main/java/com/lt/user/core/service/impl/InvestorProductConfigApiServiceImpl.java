package com.lt.user.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.business.product.IProductApiService;
import com.lt.api.user.IInvestorProductConfigApiService;
import com.lt.model.user.InvestorProductConfig;
import com.lt.user.core.dao.sqldb.IInvestorProductConfigDao;
import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.ProductVo;

@Service
public class InvestorProductConfigApiServiceImpl implements IInvestorProductConfigApiService{

	Logger logger =LoggerFactory.getLogger(getClass());
	@Autowired
	private IInvestorProductConfigDao investorProductConfigDao;
	@Autowired
	private IProductApiService productApiService;
	@Override
	public InvestorProductConfig findInvestorProductConfig(String accountId,
			String productCode) throws LTException {
		if(StringTools.isNotEmpty(accountId)&&StringTools.isNotEmpty(productCode)){
			ProductVo vo = productApiService.getProductInfo(productCode);
			return investorProductConfigDao.findInvestorProductConfig(accountId, vo.getShortCode());
		}
		return null;
	}

}
