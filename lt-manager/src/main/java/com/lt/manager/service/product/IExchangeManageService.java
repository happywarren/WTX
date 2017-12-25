package com.lt.manager.service.product;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.model.product.ExchangeInfo;
import com.lt.util.utils.model.Response;

import java.util.List;

/**
 * 交易所管理service
 * @author jingwb
 *
 */
public interface IExchangeManageService {

	/**
	 * 添加交易所信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void addExchangeInfo(ExchangeParamVO param) throws Exception;

	/**
	 * 编辑交易所信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editExchangeInfo(ExchangeParamVO param) throws Exception;

	/**
	 * 删除交易所信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeExchangeInfo(ExchangeParamVO param) throws Exception;
	
	/**
	 * 查询交易所集--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<ExchangeInfo> queryExchangeInfoPage(ExchangeParamVO param) throws Exception;

	/**
	 * 查询交易所集
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ExchangeInfo> queryExchangeInfo(ExchangeParamVO param) throws Exception;

	/**
	 * 查询交易所数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer queryExchangeInfoCount(ExchangeParamVO param) throws Exception;
}
