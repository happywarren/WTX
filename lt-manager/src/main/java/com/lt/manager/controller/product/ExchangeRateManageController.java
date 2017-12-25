package com.lt.manager.controller.product;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.manager.service.product.IExchangeRateManageService;
import com.lt.manager.service.product.IProductManageService;
import com.lt.model.product.ExchangeRate;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 货币汇率管理控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/product")
public class ExchangeRateManageController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IExchangeRateManageService exchangeRateManageServiceImpl;
	@Autowired
	private IProductManageService productManageServiceImpl;
	
	/**
	 * 添加货币
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rate/addExchangeRate")
	@ResponseBody
	public String addExchangeRate(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getName()) || StringTools.isEmpty(param.getCurrency()) ||
								param.getRate() == null){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			exchangeRateManageServiceImpl.addExchangeRate(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("添加货币信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑货币
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rate/editExchangeRate")
	@ResponseBody
	public String editExchangeRate(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			if(param.getId() == null){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			exchangeRateManageServiceImpl.editExchangeRate(param);
			productManageServiceImpl.loadProductToRedis();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("编辑货币信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除货币
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rate/removeExchangeRate")
	@ResponseBody
	public String removeExchangeRate(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getIds())){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			exchangeRateManageServiceImpl.removeExchangeRate(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("删除货币信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	
	/**
	 * 查询货币--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rate/queryExchangeRatePage")
	@ResponseBody
	public String queryExchangeRatePage(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			Page<ExchangeRate> page = exchangeRateManageServiceImpl.queryExchangeRatePage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			logger.info("查询货币信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	/**
	 * 查询货币
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rate/queryExchangeRate")
	@ResponseBody
	public String queryExchangeRate(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			return LTResponseCode.getCode(
					LTResponseCode.SUCCESS, exchangeRateManageServiceImpl.queryExchangeRate(param)
			).toJsonString();
		} catch (Exception e) {
			logger.info("查询货币信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
