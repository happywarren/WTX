package com.lt.api.business.product;

import com.lt.model.product.ProductRiskConfig;

import java.util.List;

/**
 * 业务模块 商品风险杠杆配置API接口
 * @author guodw
 *
 */
public interface IProductRiskConfigService {

	/**
	 * 查询商品时间配置
	 * @param productId
	 * @return
	 */
	public List<ProductRiskConfig> queryProductRiskConfig(Integer productId, Integer riskLevel);
	
	
	public double queryProductRiskConfig(Integer productId, String userId);
}
