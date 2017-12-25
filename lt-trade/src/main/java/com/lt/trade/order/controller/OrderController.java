//package com.lt.trade.order.controller;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.lt.api.business.product.IProductApiService;
//import com.lt.api.trade.IOrderApiService;
//import com.lt.enums.fund.FundTypeEnum;
//import com.lt.enums.product.ProductMarketEnum;
//import com.lt.enums.trade.BuyTriggerTypeEnum;
//import com.lt.enums.trade.SellTriggerTypeEnum;
//import com.lt.enums.trade.TradeDirectionEnum;
//import com.lt.enums.trade.TradeTypeEnum;
//import com.lt.model.user.log.UserOperateLog;
//import com.lt.util.error.LTException;
//import com.lt.util.error.LTResponseCode;
//import com.lt.util.utils.IpUtils;
//import com.lt.util.utils.StringTools;
//import com.lt.util.utils.TokenTools;
//import com.lt.util.utils.model.Response;
//import com.lt.util.utils.model.Token;
//import com.lt.vo.product.ProductVo;
//import com.lt.vo.trade.EntrustVo;
//import com.lt.vo.trade.OrderVo;
//import com.lt.vo.trade.PositionOrderVo;
//import com.lt.vo.trade.SinglePositionOrderVo;
//
///**   
//* 项目名称：lt-controller   
//* 类名称：OrderController   
//* 类描述： 订单处理类 
//* 创建人：yuanxin   
//* 创建时间：2016年12月15日 下午3:29:15      
//*/
//
//@Controller
//@RequestMapping("/order")
//public class OrderController {
//	
//	Logger logger = LoggerFactory.getLogger(getClass());
//	
//	@Autowired
//	private IOrderApiService orderApiService;
//	@Autowired
//	private IProductApiService productApiService;
//
//	/**
//	 * 
//	 * TODO 开仓买入
//	 * @author XieZhibing
//	 * @date 2016年12月16日 上午10:19:27
//	 * @param token
//	 * @param productCode
//	 * @param productTypeId
//	 * @param investorId
//	 * @param count
//	 * @param tradeType
//	 * @param deferStatus
//	 * @param trailStopLoss
//	 * @param fundType
//	 * @param stopLoss
//	 * @param stopProfit
//	 * @param userBuyPrice
//	 * @param userBuyDate
//	 * @return
//	 */
//	@RequestMapping("/buy")
//	@ResponseBody
//	public Response buy(String token, String productCode, Integer productTypeId,
//			String investorId, Integer count, Integer tradeType,
//			Integer deferStatus, Integer trailStopLoss, Integer fundType,
//			Double stopLoss, Double stopProfit, Double userBuyPrice,
//			Date userBuyDate, HttpServletRequest request) {
//		//开始时间
//		long startTime = System.currentTimeMillis();
//		Integer tradeDirection = tradeType;
//		logger.info("开仓开始 productCode:{}", productCode);
//		//返回信息	
//		try {
//			//校验token
//			Token tokenObj = TokenTools.parseToken(token);
//			
//			//用户ID
//			String userId = tokenObj.getUserId();
//			if(StringTools.isEmpty(userId)){
//				Response _response = LTResponseCode.getCode(LTResponseCode.US01105);
//				logger.error("userId:{}, {}", userId, _response);
//				return _response;
//			}			
//			
//			//记录用户日志
//			UserOperateLog log = new UserOperateLog();
//			log.setUserId(userId);
//			log.setIp(IpUtils.getUserIP(request));
//
//			//校验券商账号
//			if(investorId == null) {
//				Response _response = LTResponseCode.getCode(LTResponseCode.IV00001);
//				logger.error("investorId:{}, {}", investorId, _response);
//				return _response;
//			}
//			
//			//商品类型
//			if(productTypeId == null || productTypeId <= 0){
//				//商品类型未找到
//				Response _response = LTResponseCode.getCode(LTResponseCode.PR00004);
//				logger.error("productTypeId:{}, {}", productTypeId, _response);
//				return _response;
//			}
//			
//			//产品
//			ProductVo productVo = productApiService.getProductInfo(productCode);
//			if(productVo == null){
//				//商品未找到
//				Response _response = LTResponseCode.getCode(LTResponseCode.PR00002);
//				logger.error("productVo:null, {}", _response);
//				return _response;
//			}
//			
//			//商品市场状态判断
//			Integer marketStatus = productVo.getMarketStatus();
//			if (ProductMarketEnum.ONLY_OPEN.getValue() != marketStatus
//					&& ProductMarketEnum.START_TRADING.getValue() != marketStatus) {
//				//商品市场状态判断
//				Response _response = LTResponseCode.getCode(LTResponseCode.PR00005);
//				logger.error("marketStatus:{}, {}", marketStatus, _response);
//				return _response;
//			}
//			
//			//商品上下架状态判断
//			Integer status = productVo.getStatus();
//			if (ProductMarketEnum.PRODUCT_STATUS_UP.getValue() != status) {				
//				Response _response = LTResponseCode.getCode(LTResponseCode.PR00008);
//				logger.error("status:{}, {}", status, _response);
//				return _response;
//			}		
//			
//			//买入手数
//			if(count <= 0){
//				Response _response = LTResponseCode.getCode(LTResponseCode.TD00001);
//				logger.error("count:{}, {}", count, _response);
//				return _response;
//			}
//			
//			//交易方向: 0 空; 1 多
//			logger.info("校验交易方向:{}", tradeDirection);
//			if(tradeDirection == null || (TradeDirectionEnum.DIRECTION_DOWN.getValue() != tradeDirection && 
//										TradeDirectionEnum.DIRECTION_UP.getValue() != tradeDirection)) {
//				throw new LTException(LTResponseCode.TD00002);
//			}
//
//			//资金类型: 0 现金; 1 积分 
//			if(FundTypeEnum.CASH.getValue() != fundType && FundTypeEnum.SCORE.getValue() != fundType) {
//				Response _response = LTResponseCode.getCode(LTResponseCode.TD00003);
//				logger.error("fundType:{}, {}", fundType, _response);
//				return _response;
//			}
//			
//			//订单买入参数
//			OrderVo orderVo = new OrderVo(productCode, productVo.getProductName(), 
//					productTypeId, userId, investorId, count, 
//					tradeDirection, deferStatus, trailStopLoss, fundType, stopLoss, 
//					stopProfit, userBuyPrice, new Date(startTime),BuyTriggerTypeEnum.CUSTOMER.getValue());
//			orderVo.setProductVo(productVo);
//			
//			//调用订单服务接口买入
//			orderApiService.buy(orderVo);
//			logger.info("开仓用时:{}ms", (System.currentTimeMillis() - startTime));
//			
//			return LTResponseCode.getCode(LTResponseCode.SUCCESS);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			return LTResponseCode.getCode(e.getMessage());
//		}
//	}
//	
//	/**
//	 * 
//	 * TODO 平仓卖出
//	 * @author XieZhibing
//	 * @date 2016年12月16日 下午12:05:57
//	 * @return
//	 */
//	@RequestMapping("/sell")
//	@ResponseBody
//	public Response sell(String token, String displayId, Integer fundType,
//			Date userSaleDate, Double userSalePrice, HttpServletRequest request) {
//		//开始时间
//		long startTime = System.currentTimeMillis();
//		logger.info("平仓开始 displayId:{}", displayId);
//		try {
//			//校验token
//			Token tokenObj = TokenTools.parseToken(token);
//			
//			//用户ID
//			String userId = tokenObj.getUserId();
//			if(StringTools.isEmpty(userId)){
//				return LTResponseCode.getCode(LTResponseCode.US01105);
//			}			
//			
//			//记录用户日志
//			UserOperateLog log = new UserOperateLog();
//			log.setUserId(userId);
//			log.setIp(IpUtils.getUserIP(request));
//
//			//订单不存在 
//			if(displayId == null) {
//				throw new LTException(LTResponseCode.TD00004);
//			}
//			
//			//资金类型: 0 现金; 1 积分 
//			if(FundTypeEnum.CASH.getValue() != fundType && FundTypeEnum.SCORE.getValue() != fundType) {
//				return LTResponseCode.getCode(LTResponseCode.TD00003);
//			}
//			
//			// 订单卖出参数
//			OrderVo orderVo = new OrderVo(displayId, fundType, SellTriggerTypeEnum.CUSTOMER.getValue(), 
//					userId, userSalePrice, new Date(startTime));
//			
//			// 调用订单服务接口卖出			
//			orderApiService.sell(orderVo);
//			logger.info("平仓用时:{}ms", (System.currentTimeMillis() - startTime));
//			return LTResponseCode.getCode(LTResponseCode.SUCCESS);
//		} catch (Exception e) {
//			return LTResponseCode.getCode(e.getMessage());
//		}
//	}
//	/**
//	 * 
//	 * TODO 批量平仓
//	 * @author XieZhibing
//	 * @date 2016年12月16日 下午12:05:57
//	 * @return
//	 */
//	@RequestMapping("/sellAll")
//	@ResponseBody
//	public Response sell(String token, Integer fundType, String productCode, HttpServletRequest request) {
//		//开始时间
//		long startTime = System.currentTimeMillis();
//		logger.info("批量平仓开始 productCode:{}", productCode);
//		try {
//			//校验token
//			Token tokenObj = TokenTools.parseToken(token);
//			
//			//用户ID
//			String userId = tokenObj.getUserId();
//			if(StringTools.isEmpty(userId)){
//				return LTResponseCode.getCode(LTResponseCode.US01105);
//			}			
//			
//			//记录用户日志
//			UserOperateLog log = new UserOperateLog();
//			log.setUserId(userId);
//			log.setIp(IpUtils.getUserIP(request));
//
//			//资金类型: 0 现金; 1 积分 
//			if(FundTypeEnum.CASH.getValue() != fundType && FundTypeEnum.SCORE.getValue() != fundType) {
//				return LTResponseCode.getCode(LTResponseCode.TD00003);
//			}
//			
//			// 调用订单服务接口卖出			
//			orderApiService.sellAll(fundType, userId, productCode);
//			logger.info("批量平仓用时:{}ms", (System.currentTimeMillis() - startTime));
//			return LTResponseCode.getCode(LTResponseCode.SUCCESS);
//		} catch (Exception e) {
//			return LTResponseCode.getCode(e.getMessage());
//		}
//	}
//	
//	
//	/**
//	 * 用户持仓订单
//	 * @param token
//	 * @param fundType
//	 * @return
//	 */
//	@RequestMapping("findPositionOrder")
//	@ResponseBody
//	public Response findPositionOrder(String token,Integer fundType){
//		List<PositionOrderVo> data = null;
//		try {
//			Token  tokens = TokenTools.parseToken(token);
//			if(tokens == null || tokens.getUserId().isEmpty()){
//				throw new LTException(LTResponseCode.OR10004);
//			}
//			if(fundType == null ){
//				throw new LTException(LTResponseCode.OR10005);
//			}
//			String userId = tokens.getUserId();
//			data = orderApiService.findPositionOrderByUserId(userId, fundType);
//			
//		} catch (LTException e) {
//			return LTResponseCode.getCode(e.getMessage(),data);
//		}
//		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
//	}
//	
//	/**
//	 * 单品持仓列表
//	 * @param token
//	 * @param productCode
//	 * @param fundType
//	 * @return
//	 */
//	@RequestMapping("findSinglePositionList")
//	@ResponseBody
//	public Response findSinglePositionList(String token,String productCode,Integer fundType){
//		List<SinglePositionOrderVo> data = null;
//		try {
//			Token  tokens = TokenTools.parseToken(token);
//			if(tokens == null || tokens.getUserId().isEmpty()){
//				throw new LTException(LTResponseCode.OR10004);
//			}
//			if(StringTools.isEmpty(productCode)){
//				throw new LTException(LTResponseCode.OR10005);
//			}
//			if(fundType == null ){
//				throw new LTException(LTResponseCode.OR10005);
//			}
//			String userId = tokens.getUserId();
//			data = orderApiService.findSinglePositionList(userId,productCode, fundType);
//			
//		} catch (LTException e) {
//			return LTResponseCode.getCode(e.getMessage(),data);
//		}
//		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
//	}
//	
//	/**
//	 * 查询用户委托中订单个数
//	 * @param token
//	 * @return
//	 */
//	@RequestMapping("findEntrustTheOrdersCount")
//	@ResponseBody
//	public Response findEntrustTheOrdersCount(String token){
//		int data = 0;
//		try {
//			Token  tokens = TokenTools.parseToken(token);
//			if(tokens == null || tokens.getUserId().isEmpty()){
//				throw new LTException(LTResponseCode.OR10004);
//			}
//			String userId = tokens.getUserId();
//			data = orderApiService.findEntrustTheOrdersCount(userId);
//			
//		} catch (LTException e) {
//			return LTResponseCode.getCode(e.getMessage(),data);
//		}
//		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
//	}
//	
//	/**
//	 * 查询用户委托中订单
//	 * @param token
//	 * @return
//	 */
//	@RequestMapping("findEntrustTheOrdersList")
//	@ResponseBody
//	public Response findEntrustTheOrdersList(String token){
//		List<EntrustVo> data = null;
//		try {
//			Token  tokens = TokenTools.parseToken(token);
//			if(tokens == null || tokens.getUserId().isEmpty()){
//				throw new LTException(LTResponseCode.OR10004);
//			}
//			String userId = tokens.getUserId();
//			data = orderApiService.findEntrustTheOrdersList(userId);
//			
//		} catch (LTException e) {
//			return LTResponseCode.getCode(e.getMessage(),data);
//		}
//		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
//	}
//	
//	/**
//	 * 查询用户持仓订单数
//	 * @param token
//	 * @return
//	 */
//	@RequestMapping("findPosiOrderCount")
//	@ResponseBody
//	public Response findPosiOrderCount(String token,Integer fundType){
//		Map<String,Map<String,Object>> data = null;
//		try {
//			Token  tokens = TokenTools.parseToken(token);
//			if(tokens == null || tokens.getUserId().isEmpty()||Integer.valueOf(tokens.getUserId()) <= 0){
//				throw new LTException(LTResponseCode.OR10001);
//			}
//			if(fundType == null ){
//				throw new LTException(LTResponseCode.OR10002);
//			}
//			String userId = tokens.getUserId();
//			data = orderApiService.findPosiOrderCount(userId, fundType);
//			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(data.values());
//			return LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
//		} catch (LTException e) {
//			return LTResponseCode.getCode(e.getMessage(),data);
//		}
//		
//	}
//	
//	
//	@RequestMapping("test")
//	@ResponseBody
//	public Response test(String token,Integer fundType){
//		Map<String, Double> data = null;
//		try {
//			data = orderApiService.findFloatAmtByUserId("10025");
//			return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
//		} catch (LTException e) {
//			return LTResponseCode.getCode(e.getMessage(),data);
//		}
//		
//	}
//}
