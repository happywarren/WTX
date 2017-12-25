package com.lt.manager.service.product;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.model.product.ExchangeRate;
import com.lt.util.utils.model.Response;

import java.util.List;

/**
 * 货币汇率管理service
 * @author jingwb 
 *
 */
public interface IExchangeRateManageService {

	/**
	 * 添加货币信息
	 * @param param
	 * @throws Exception
	 */
	public void addExchangeRate(ExchangeParamVO param) throws Exception;

	/**
	 * 修改货币信息
	 * @param param
	 * @throws Exception
	 */
	public void editExchangeRate(ExchangeParamVO param) throws Exception;

	/**
	 * 删除货币信息
	 * @param param
	 * @throws Exception
	 */
	public void removeExchangeRate(ExchangeParamVO param) throws Exception;
	
	/**
	 * 查询货币集--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<ExchangeRate> queryExchangeRatePage(ExchangeParamVO param) throws Exception;
	/**
	 * 查询货币集
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ExchangeRate> queryExchangeRate(ExchangeParamVO param) throws Exception;
}
