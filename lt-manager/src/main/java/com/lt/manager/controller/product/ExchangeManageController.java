package com.lt.manager.controller.product;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.manager.service.product.IExchangeManageService;
import com.lt.model.product.ExchangeInfo;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 交易所管理控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/product")
public class ExchangeManageController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IExchangeManageService exchangeManageServiceImpl;
	
	/**
	 * 添加交易所信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/addExchangeInfo")
	@ResponseBody
	public String addExchangeInfo(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		logger.info("添加交易所信息param={}",param);
		try {
			if(StringTools.isEmpty(param.getCode()) || StringTools.isEmpty(param.getName())){				
				response = LTResponseCode.getCode(LTResponseCode.FU00001);	
				return response.toJsonString();
			}
			exchangeManageServiceImpl.addExchangeInfo(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("添加交易所信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑交易所信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/editExchangeInfo")
	@ResponseBody
	public String editExchangeInfo(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		logger.info("修改交易所信息param={}",param);
		try {
			if(param.getId() == null){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			exchangeManageServiceImpl.editExchangeInfo(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改交易所信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除交易所信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/removeExchangeInfo")
	@ResponseBody
	public String removeExchangeInfo(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		logger.info("删除交易所信息param={}",param);
		try {
			if(StringTools.isEmpty(param.getIds()) ){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			exchangeManageServiceImpl.removeExchangeInfo(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("删除交易所信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询交易所信息集--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/queryExchangeInfoPage")
	@ResponseBody
	public String queryExchangeInfoPage(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		logger.info("查询交易所信息param={}",param);
		try {
			Page<ExchangeInfo> page = exchangeManageServiceImpl.queryExchangeInfoPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询交易所信息异常，e={}",e);
		}
		return response.toJsonString();
	}

	/**
	 * 查询交易所信息集
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/exchange/queryExchangeInfo")
	@ResponseBody
	public String queryExchangeInfo(HttpServletRequest request,ExchangeParamVO param){
		Response response = null;
		logger.info("查询交易所信息param={}",param);
		try {

			return LTResponseCode.getCode(
					LTResponseCode.SUCCESS, exchangeManageServiceImpl.queryExchangeInfo(param)
			).toJsonString();
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询交易所信息异常，e={}",e);
		}
		return response.toJsonString();
	}
}
