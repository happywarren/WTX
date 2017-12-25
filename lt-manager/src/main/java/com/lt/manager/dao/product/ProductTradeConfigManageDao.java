package com.lt.manager.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductTradeConfig;

/**
 * 商品交易配置dao
 * @author jingwb
 *
 */
public interface ProductTradeConfigManageDao {

	/**
	 * 创建商品配置信息
	 * @param config
	 */
	public void insertProductTradeConfig(ProductTradeConfig config);
	
	/**
	 * 批量修改商品配置信息
	 * @param config
	 */
	public void updateProductTradeConfigs(ProductTradeConfig config);
	
	/**
	 * 批量删除交易配置信息
	 * @param param
	 */
	public void deleteProTradeCfgs(ProductParamVO param);
	
	/**
	 * 查询交易配置信息
	 * @param param
	 * @return
	 */
	public ProductTradeConfig selectProTradeCfg(ProductParamVO param);
	
	/**
	 * 查询商品交易配置信息
	 * @return
	 */
	public List<ProductTradeConfig> selectProTradeCfgs(ProductParamVO param);
	
}
