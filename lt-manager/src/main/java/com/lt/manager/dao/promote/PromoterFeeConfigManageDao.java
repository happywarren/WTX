package com.lt.manager.dao.promote;

import java.util.List;

import com.lt.manager.bean.promote.PromoteParamVo;
import com.lt.model.product.Product;
import com.lt.model.promote.PromoterFeeConfig;

/**
 * 推广员佣金设置dao
 * @author jingwb
 *
 */
public interface PromoterFeeConfigManageDao {

	/**
	 * 批量插入佣金设置
	 * @param list
	 */
	public void insertPromoterFeeConfigs(List<PromoterFeeConfig> list);
	
	/**
	 * 修改推广配置信息
	 * @param promoterFeeConfig
	 */
	public void updatePromoterFeeConfig(PromoterFeeConfig promoterFeeConfig);
	
	/**
	 * 删除推广配置
	 * @param promoterFeeConfig
	 */
	public void deletePromoterFeeConfig(PromoterFeeConfig promoterFeeConfig);
	
	/**
	 * 查询配置信息
	 * @param levelId
	 * @return
	 */
	public List<PromoterFeeConfig> selectPromoterFeeConfigByLevelId(String levelId);
	
	public List<PromoterFeeConfig> selectPromoterFeeConfigPage(PromoteParamVo param);
	
	public Integer selectPromoterFeeConfigCount(PromoteParamVo param);
	
	public List<Product> selectProShortCode(String level);
	
	public Integer selectProShortCodeCount(String level);
 }
