package com.lt.manager.dao.product;

import java.util.List;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.model.product.ProductTimeConfig;

/**
 * 商品交易时间配置dao
 * @author jingwb
 *
 */
public interface ProductTimeConfigManageDao {

	/**
	 * 批量插入交易时间配置信息
	 * @param list
	 */
	public void insertProTimeCfgs(List<ProductTimeConfig> list);
	
	
	/**
	 * 批量插入交易时间配置信息(冬令时)
	 * @param list
	 */
	public void insertProTimeCfgsWinter(List<ProductTimeConfig> list);
	
	
	/**
	 * 查询商品时间配置信息
	 * @param param
	 * @return
	 */
	public List<ProductTimeConfig> selectProTimeCfgs(ProductParamVO param);
	
	public List<ProductTimeConfig> selectProTimeCfgsWinter(ProductParamVO param);
	
	/**
	 * 批量删除交易时间配置信息
	 * @param param
	 */
	public void deleteProTimeCfgs(ProductParamVO param);
	
	/**
	 * 批量删除交易时间配置信息(冬令时)
	 * @param param
	 */
	public void deleteProTimeCfgsWinter(ProductParamVO param);
}
