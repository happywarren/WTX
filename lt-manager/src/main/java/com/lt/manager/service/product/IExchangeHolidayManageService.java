package com.lt.manager.service.product;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.model.product.ExchangeHoliday;
import com.lt.util.utils.model.Response;

/**
 * 交易所假期service
 * @author jingwb
 *
 */
public interface IExchangeHolidayManageService {

	/**
	 * 添加交易所假期信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void addExchangeHoliday(ExchangeParamVO param) throws Exception;
	
	/**
	 * 修改交易所假期信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editExchangeHoliday(ExchangeParamVO param) throws Exception;
	
	/**
	 * remove交易所假期信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeExchangeHoliday(ExchangeParamVO param) throws Exception;
	
	/**
	 * 查询交易所假期信息集--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<ExchangeHoliday> queryExchangeHolidayPage(ExchangeParamVO param) throws Exception;
}
