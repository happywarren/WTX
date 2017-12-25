package com.lt.manager.controller.product;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.manager.service.product.IExchangeHolidayManageService;
import com.lt.manager.service.product.IProductManageService;
import com.lt.model.product.ExchangeHoliday;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.model.Response;

/**
 * 交易所假期controller
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/product")
public class ExchangeHolidayManageController {
	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IExchangeHolidayManageService exchangeHolidayManageServiceImpl;
	@Autowired
	private IProductManageService productManageServiceImpl;
	
	/**
	 * 新增交易所假期信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/addExchangeHoliday")
	@ResponseBody
	public String addExchangeHoliday(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			exchangeHolidayManageServiceImpl.addExchangeHoliday(param);
			productManageServiceImpl.loadProAndTimeToRedis();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("新增假期信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑交易所假期信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/editExchangeHoliday")
	@ResponseBody
	public String editExchangeHoliday(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			exchangeHolidayManageServiceImpl.editExchangeHoliday(param);
			productManageServiceImpl.loadProAndTimeToRedis();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("编辑假期信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除交易所假期信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/removeExchangeHoliday")
	@ResponseBody
	public String removeExchangeHoliday(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			exchangeHolidayManageServiceImpl.removeExchangeHoliday(param);
			productManageServiceImpl.loadProAndTimeToRedis();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("删除假期信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询交易所假期信息--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/queryExchangeHolidayPage")
	@ResponseBody
	public String queryExchangeHolidayPage(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		try {
			Page<ExchangeHoliday>  page = exchangeHolidayManageServiceImpl.queryExchangeHolidayPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			logger.info("查询假期信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
