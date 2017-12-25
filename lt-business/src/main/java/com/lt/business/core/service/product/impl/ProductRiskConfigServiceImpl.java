package com.lt.business.core.service.product.impl;

import com.lt.business.core.dao.product.IProductRiskConfigDao;
import com.lt.constant.LTConstant;
import com.lt.api.business.product.IProductRiskConfigService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.product.ProductRiskConfig;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.utils.StringTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述: 商品风险杠杆配置接口实现类
 *
 * @author lvx
 * @created 2017/7/18
 */
@Service
public class ProductRiskConfigServiceImpl implements IProductRiskConfigService {
	@Resource
	private IProductRiskConfigDao productRiskConfigDao;

	@Autowired
	private IUserApiBussinessService userService;

	@Override
	public List<ProductRiskConfig> queryProductRiskConfig(Integer productId, Integer riskLevel) {
		return productRiskConfigDao.queryProductRiskConfig(productId, riskLevel);
	}

	@Override
	public double queryProductRiskConfig(Integer productId, String userId) {
		UserBaseInfo userBaseInfo = this.userService.queryUserBuyId(userId);
		String riskLevel = userBaseInfo.getRiskLevel();
		if (StringTools.isEmpty(riskLevel)) {
			riskLevel = LTConstant.DEFAULT_RISK_LEVEL;
		}
		List<ProductRiskConfig> productRiskConfigList = this.productRiskConfigDao.queryProductRiskConfig(productId,
				Integer.parseInt(riskLevel));
		return productRiskConfigList.get(0).getDeferFee();
	}
}
