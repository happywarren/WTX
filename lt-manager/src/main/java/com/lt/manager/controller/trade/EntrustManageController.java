package com.lt.manager.controller.trade;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.api.user.IUserApiLogService;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.manager.service.trade.IEntrustManageService;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 现金订单交易控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/entrust/child")
public class EntrustManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IEntrustManageService tradeCashManageService;
	@Autowired
	private IUserApiLogService userLogServiceImpl ;
	
	
	
	/**
	 * 查询委托子记录--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryEntrustChildTradeOrderPage")
	@ResponseBody
	public String queryEntrustChildTradeOrderPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			Page<OrderParamVO> page = tradeCashManageService.queryEntrustTradeOrderPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询委托订单异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询委托详情
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryCashEntrustOrderInfo")
	@ResponseBody
	public String queryCashEntrustOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			
			OrderParamVO order = tradeCashManageService.getCashEntrustOrderInfo(Integer.valueOf(param.getId()));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,order);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询委托详情异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询委托信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryEntrustInfo")
	@ResponseBody
	public String queryEntrustInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getOrderId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			OrderCashEntrustInfo order = tradeCashManageService.queryEntrustInfo(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,order);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询委托信息异常,e={}",e);
		}
		return response.toJsonString();
	}
	
}
