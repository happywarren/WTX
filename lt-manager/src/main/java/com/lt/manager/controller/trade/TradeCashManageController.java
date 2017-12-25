package com.lt.manager.controller.trade;

import com.github.pagehelper.Page;
import com.lt.api.user.IUserApiLogService;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.manager.service.trade.ITradeCashManageService;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.util.LoggerTools;
import com.lt.util.StaffUtil;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 现金订单交易控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/trade/cash")
public class TradeCashManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private ITradeCashManageService tradeCashManageService;
	@Autowired
	private IUserApiLogService userLogServiceImpl ;
	
	/**
	 * 查询今日订单--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryTodayTradeOrderPage")
	@ResponseBody
	public String queryTodayTradeOrderPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			param.setFlag("today");//查询今日订单标志
			Page<OrderParamVO> page = tradeCashManageService.queryTradeOrderPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询今日订单异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询现金订单--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryTradeOrderPage")
	@ResponseBody
	public String queryTradeOrderPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			Page<OrderParamVO> page = tradeCashManageService.queryTradeOrderPage(param);		
			Map<String,String> map1 = tradeCashManageService.queryTradeOrderDateMap(param);
			return JqueryEasyUIData.init(page,map1);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询现金订单异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询现金订单--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryTradeOrderByUserIdPage")
	@ResponseBody
	public String queryTradeOrderByUserIdPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			Page<OrderParamVO> page = tradeCashManageService.queryTradeOrderPage(param);		
			Map<String,String> map1 = tradeCashManageService.queryTradeOrderDateMap(param);
			return JqueryEasyUIData.init(page,map1);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询现金订单异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询委托记录--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryEntrustTradeOrderPage")
	@ResponseBody
	public String queryEntrustTradeOrderPage(HttpServletRequest request,OrderParamVO param){
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
	 * 查询成交记录--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/querySuccessTradeOrderPage")
	@ResponseBody
	public String querySuccessTradeOrderPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			Page<OrderParamVO> page = tradeCashManageService.querySuccessTradeOrderPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询成交记录异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询订单详情
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryCashOrderInfo")
	@ResponseBody
	public String queryCashOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			Map<String,Object> rm = new HashMap<String,Object>();
			OrderParamVO order = tradeCashManageService.getCashOrderInfo(param.getId());
			List<Map<String, Object>> list = tradeCashManageService.getOrderBuyOrSaleList(param.getId());
			rm.put("orderInfo", order);
			rm.put("orderList", list);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,rm);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询订单详情异常,e={}",e);
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
	 * 获取成交单详情
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryCashSuccOrderInfo")
	@ResponseBody
	public String queryCashSuccOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			OrderParamVO order = tradeCashManageService.getCashSuccOrderInfo(Integer.valueOf(param.getId()));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,order);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询成交详情异常,e={}",e);
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
	
	/**
	 * 查询订单信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryOrderInfo")
	@ResponseBody
	public String queryOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getOrderId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			OrderCashInfo order = tradeCashManageService.queryOrderInfo(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,order);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询委托信息异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 强制成功
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/forceSuccess")
	@ResponseBody
	public String forceSuccess(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getOrderId()) && StringTools.isEmpty(param.getIds())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			Staff staff = StaffUtil.getStaff(request);
			param.setModifyUserId(staff.getName());
			if(StringTools.isNotBlank(param.getIds())){
				tradeCashManageService.forceTypeMulti(param, "Success");
			}
			else{
				tradeCashManageService.forceSuccess(param);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("强制成功异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 强制失败
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/forceFail")
	@ResponseBody
	public String forceFail(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getOrderId()) && StringTools.isEmpty(param.getIds())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			Staff staff = StaffUtil.getStaff(request);
			param.setModifyUserId(staff.getName());
			if(StringTools.isNotBlank(param.getIds())){
				tradeCashManageService.forceTypeMulti(param, "Fail");
			}
			else{
				tradeCashManageService.forceFail(param);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info(" 强制失败异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 强制平仓
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/forceSell")
	@ResponseBody
	public String forceSell(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getOrderId()) && StringTools.isEmpty(param.getIds())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			Staff staff = StaffUtil.getStaff(request);
			param.setModifyUserId(staff.getName());
			if(StringTools.isNotBlank(param.getIds())){
				tradeCashManageService.forceTypeMulti(param, "Sell");
			}
			else{
				tradeCashManageService.forceSell(param);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info(" 强制平仓异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	@RequestMapping(value="/getOrderOperateLog")
	@ResponseBody
	public String getOrderOperateLog(HttpServletRequest request){
		Response response = null;
		
		String orderId = StringTools.formatStr(request.getParameter("orderId"), "");
		String pages = StringTools.formatStr(request.getParameter("page"), "1");
		String rows = StringTools.formatStr(request.getParameter("rows"), "10");
		
		try {
			
			if(orderId.equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			Page<OrderLossProfitDefLog> page = userLogServiceImpl.qryUserOrderLossProfitDeferLog(orderId,Integer.parseInt(pages),Integer.parseInt(rows));
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询成交记录异常,e={}",e);
		}
		return response.toJsonString();
	}
}
