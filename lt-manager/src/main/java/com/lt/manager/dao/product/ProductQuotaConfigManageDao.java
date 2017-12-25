package com.lt.manager.dao.product;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductQuotaConfig;

/**
 * 商品行情配置dao
 * @author jingwb
 *
 */
public interface ProductQuotaConfigManageDao {

	/**
	 * 创建商品行情配置
	 * @param config
	 */
	public void insertProQuotaConfig(ProductQuotaConfig config);
	
	/**
	 * 批量编辑商品行情配置信息
	 * @param config
	 */
	public void updateProQuotaConfigs(ProductQuotaConfig config);
	
	/**
	 * 批量删除行情配置信息
	 * @param param
	 */
	public void deleteProQuotaCfgs(ProductParamVO param);
	
	/**
	 * 查询行情配置信息
	 * @param param
	 * @return
	 */
	public ProductQuotaConfig selectProQuotaCfg(ProductParamVO param);
}
