package com.lt.manager.dao.product;

import java.util.List;



import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.model.product.ExchangeHoliday;

/**
 * 交易所假期dao
 * @author jingwb
 *
 */
public interface ExchangeHolidayManageDao {

	/**
	 * 批量新增交易所假日信息
	 * @param param
	 */
	public void insertExchangeHolidays(List<ExchangeParamVO> list);
	
	/**
	 * 编辑交易所假日信息
	 * @param param
	 */
	public void updateExchangeHoliday(ExchangeParamVO param);
	
	/**
	 * 删除交易所假日信息
	 * @param param
	 */
	public void deleteExchangeHoliday(ExchangeParamVO param);
	
	/**
	 * 查询交易所假日信息集--分页
	 * @param param
	 * @return
	 */
	public List<ExchangeHoliday> selectExchangeHolidayPage(ExchangeParamVO param);
	
	/**
	 * 查询交易所假日信息数量
	 * @param param
	 * @return
	 */
	public Integer selectExchangeHolidayCount(ExchangeParamVO param);
}
