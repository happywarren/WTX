package com.lt.manager.dao.product;

import java.util.List;

import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.model.product.ExchangeInfo;


/**
 * 交易所管理dao
 * @author jingwb
 *
 */
public interface ExchangeManageDao {
	
	/**
	 * 插入交易所信息
	 * @param param
	 */
	public void insertExchangeInfo(ExchangeParamVO param);
	
	/**
	 * 修改交易所信息
	 * @param param
	 */
	public void updateExchangeInfo(ExchangeParamVO param);
	
	/**
	 * 刪除交易所信息
	 * @param param
	 */
	public void deleteExchangeInfo(ExchangeParamVO param);
	
	/**
	 * 查詢交易所集--用于分頁
	 * @param param
	 * @return
	 */
	public List<ExchangeInfo> selectExchangeInfoPage(ExchangeParamVO param);
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public Integer selectExchangeInfoCount(ExchangeParamVO param);

}
