package com.lt.manager.dao.product;

import java.util.List;

import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.model.product.ExchangeRate;

/**
 * 货币管理dao
 * @author jingwb
 *
 */
public interface ExchangeRateManageDao {

	/**
	 * 货币新增
	 * @param param
	 */
	public void insertExchangeRate(ExchangeParamVO param);
	
	/**
	 * 货币编辑
	 * @param param
	 */
	public void updateExchangeRate(ExchangeParamVO param);
	
	/**
	 * 货币删除
	 * @param param
	 */
	public void deleteExchangeRate(ExchangeParamVO param);
	
	/**
	 * 查询货币--分页
	 * @param param
	 * @return
	 */
	public List<ExchangeRate> selectExchangeRatePage(ExchangeParamVO param);
	
	/**
	 * 查询货币数量
	 * @param param
	 * @return
	 */
	public Integer selectExchangeRateCount(ExchangeParamVO param);
}
