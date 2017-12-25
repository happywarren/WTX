package com.lt.manager.controller.trade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.manager.service.trade.ITradeScoreManageService;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.util.LoggerTools;
import com.lt.util.StaffUtil;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 积分订单交易控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/trade/score")
public class TradeScoreManageController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private ITradeScoreManageService tradeScoreManageService;

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
			Page<OrderParamVO> page = tradeScoreManageService.queryTradeOrderPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询今日订单异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询积分订单--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryTradeOrderByUserIdPage")
	@ResponseBody
	public String queryTradeOrderByUserIdPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			Page<OrderParamVO> page = tradeScoreManageService.queryTradeOrderPage(param);		
			Map<String,String> map1 = tradeScoreManageService.queryTradeOrderDateMap(param);
			return JqueryEasyUIData.init(page,map1);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询现金订单异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询积分订单--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryTradeOrderPage")
	@ResponseBody
	public String queryTradeOrderPage(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try {
			Page<OrderParamVO> page = tradeScoreManageService.queryTradeOrderPage(param);		
			Map<String,String> map1 = tradeScoreManageService.queryTradeOrderDateMap(param);
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
			Page<OrderParamVO> page = tradeScoreManageService.queryEntrustTradeOrderPage(param);
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
			Page<OrderParamVO> page = tradeScoreManageService.querySuccessTradeOrderPage(param);
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
	@RequestMapping(value="/queryScoreOrderInfo")
	@ResponseBody
	public String queryCashOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			Map<String,Object> rm = new HashMap<String,Object>();
			OrderParamVO order = tradeScoreManageService.getScoreOrderInfo(param.getId());
			List<Map<String, Object>> list = tradeScoreManageService.getOrderBuyOrSaleList(param.getId());
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
	@RequestMapping(value="/queryScoreEntrustOrderInfo")
	@ResponseBody
	public String queryCashEntrustOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}

			OrderParamVO order = tradeScoreManageService.getScoreEntrustOrderInfo(Integer.valueOf(param.getId()));
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
	@RequestMapping(value="/queryScoreSuccOrderInfo")
	@ResponseBody
	public String queryCashSuccOrderInfo(HttpServletRequest request,OrderParamVO param){
		Response response = null;
		try{
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.TRJ0000).toJsonString();
			}
			OrderParamVO order = tradeScoreManageService.getScoreSuccOrderInfo(Integer.valueOf(param.getId()));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,order);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询成交详情异常,e={}",e);
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
			if(StringTools.isNotBlank(param.getIds())){
				tradeScoreManageService.forceTypeMulti(param, "Sell");
			}
			else{
				tradeScoreManageService.forceSell(param);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info(" 强制平仓异常,e={}",e);
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
			param.setModifyUserId(staff.getId().toString());
			if(StringTools.isNotBlank(param.getIds())){
				tradeScoreManageService.forceTypeMulti(param, "Fail");
			}
			else{
				tradeScoreManageService.forceFail(param);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info(" 强制失败异常,e={}",e);
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
			param.setModifyUserId(staff.getId().toString());
			if(StringTools.isNotBlank(param.getIds())){
				tradeScoreManageService.forceTypeMulti(param, "Success");
			}
			else{
				tradeScoreManageService.forceSuccess(param);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("强制成功异常,e={}",e);
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
			OrderScoreEntrustInfo order = tradeScoreManageService.queryEntrustInfo(param);
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
			OrderScoreInfo order = tradeScoreManageService.queryOrderInfo(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,order);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询委托信息异常,e={}",e);
		}
		return response.toJsonString();
	}
}
