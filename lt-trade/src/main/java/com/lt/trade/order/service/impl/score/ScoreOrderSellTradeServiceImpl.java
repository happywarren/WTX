/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: scoreOrderSellTradeServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.EntrustPriceTypeEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.SellTriggerTypeEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.OrderScoreEntrustInfoDao;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.service.IOrderSellTradeService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.OrderVo;
import com.lt.vo.trade.TradeDirectVo;

/**
 * TODO 现金订单平仓业务实现
 * @author XieZhibing
 * @date 2016年12月12日 下午8:44:03
 * @version <b>1.0.0</b>
 */
@Service
public class ScoreOrderSellTradeServiceImpl extends AbstractScoreOrderTradeSerivce implements IOrderSellTradeService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -1111246308799240761L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ScoreOrderSellTradeServiceImpl.class);
	
	/** 资金分布式接口: 资金扣款、结算、退款业务接口 */
	@Autowired
	private IFundTradeApiService fundTradeApiService;
	/** 订单基本信息接口 */
	@Autowired
	private OrderScoreInfoDao orderScoreInfoDao;

	/** 产品信息接口 */
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	/** 交易服务 */
	@Autowired
	private TradeServer tradeServer;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	/** 
	 * 平仓
	 * @author XieZhibing
	 * @date 2016年12月12日 下午8:44:03
	 * @see com.lt.trade.order.service.IOrderSellTradeService#doSell(com.lt.vo.trade.OrderVo)
	 * @param orderVo
	 * @throws LTException
	 */
	@Override
	public void doSell(OrderVo orderVo) throws LTException {
		//开始时间
		long startTime = System.currentTimeMillis();
		//订单显示ID
		String orderId = orderVo.getOrderId();		
		logger.info("======平仓开始orderId:{}======", orderId);
		//是否卖完
		if(TradeUtils.isSuccess(redisTemplate,"", orderId)){
			OrderCashInfoCache.remove(orderId);
			logger.error("订单已卖出  orderVo:{} ",JSONObject.toJSON(orderVo));
			return;
		}
		
		//订单正在重复卖出
		if(TradeUtils.isDuplicateSell(redisTemplate,"", orderId)){
			logger.error("订单正在卖出中  orderVo:{} ",JSONObject.toJSON(orderVo));
			throw new LTException(LTResponseCode.TDJ0002);
		}
				
				
		//现金订单基本信息
		OrderScoreInfo scoreOrder = this.queryScoreOrdersInfo(orderVo);
		//理订单的移动止损金额、移动止损价
		this.dealMoveStopLoss(scoreOrder,orderVo);
		//初始化卖出委托单
		OrderScoreEntrustInfo entrustRecord = this.initSellEntrustRecord(scoreOrder);
		logger.info("已初始化平仓委托单, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//设置委托类型，委托价
		scoreOrder.setSellEntrustType(entrustRecord.getEntrustType());
		scoreOrder.setEntrustSellPrice(entrustRecord.getEntrustPrice());
		
		//订单主数据保存到缓存
		OrderScoreInfoCache.put(scoreOrder);
		//委托单保存到缓存
		OrderScoreEntrustInfoCache.put(entrustRecord);
		
		//发送委托消息到C++
		this.sendSellEntrustMessage(scoreOrder, entrustRecord);	
		logger.info("======订单orderId:{}平仓用时time:{}ms======", orderId, (System.currentTimeMillis() - startTime));
	}

	/**
	 * 
	 * TODO 批量平仓
	 * @author XieZhibing
	 * @date 2017年1月4日 上午9:56:14
	 * @param userId
	 * @param productCode
	 * @throws LTException
	 */
	public void doSell(String userId, String productCode) throws LTException {
		//执行时间
		long startTime = System.currentTimeMillis();
		logger.info("======批量平仓开始userId:{}, productCode:{}======", userId, productCode);
		
		//查询持仓中的现金订单信息
		List<OrderScoreInfo> orderScoreInfoList = orderScoreInfoDao.queryUserVendibleScoreOrderList(userId, productCode);
		//判断现金订单是否存在
		if(orderScoreInfoList == null || orderScoreInfoList.isEmpty()) {
			logger.info("未查询到用户:{}, 产品:{}的订单", userId, productCode);
			return;
		}
		
		for(OrderScoreInfo orderScoreInfo : orderScoreInfoList){
			
			//是否卖完
			if(TradeUtils.isSuccess(redisTemplate,"", orderScoreInfo.getOrderId())){
				OrderCashInfoCache.remove(orderScoreInfo.getOrderId());
				logger.error("订单已卖出  orderVo:{} ",JSONObject.toJSON(orderScoreInfo));
				continue;
			}
			
			if(TradeUtils.isDuplicateSell(redisTemplate,orderScoreInfo.getUserId().toString(), orderScoreInfo.getOrderId())){
//				throw new LTException(LTResponseCode.TDJ0002);
				//订单正在卖出中
				logger.error("订单正在卖出中  orderCashInfo:{} ",JSONObject.toJSON(orderScoreInfo));
				continue;
			}
			
			OrderVo orderVo = new OrderVo(orderScoreInfo.getOrderId(), FundTypeEnum.SCORE.getValue(), SellTriggerTypeEnum.CUSTOMER.getValue(), 
					userId, 0, new Date(startTime));
			
			//现金订单基本信息
			OrderScoreInfo scoreOrder = this.queryScoreOrdersInfo(orderVo);
			//理订单的移动止损金额、移动止损价
			this.dealMoveStopLoss(scoreOrder,orderVo);
			//初始化卖出委托单
			OrderScoreEntrustInfo entrustRecord = this.initSellEntrustRecord(scoreOrder);
			logger.info("已初始化平仓委托单, orderId:{}, entrustCode:{}", orderScoreInfo.getOrderId(), entrustRecord.getEntrustId());
			
			//设置委托类型，委托价
			scoreOrder.setBuyEntrustType(entrustRecord.getEntrustType());
			scoreOrder.setEntrustBuyPrice(entrustRecord.getEntrustPrice());
			
			//订单主数据保存到缓存
			OrderScoreInfoCache.put(scoreOrder);
			//委托单保存到缓存
			OrderScoreEntrustInfoCache.put(entrustRecord);
			
			//发送委托消息到C++
			this.sendSellEntrustMessage(scoreOrder, entrustRecord);	
		
		}
		logger.info("======批量平仓完成 userId:{}, productCode:{}, 执行时间time:{}ms======", userId, productCode, (System.currentTimeMillis() - startTime));
	}

	
	/**
	 * 
	 * TODO 初始化平仓委托单
	 * @author XieZhibing
	 * @date 2017年1月3日 下午8:48:34
	 * @param displayId
	 * @param orderId
	 * @param sellEntrustCount
	 * @param scoreOrder
	 * @return
	 */
	private OrderScoreEntrustInfo initSellEntrustRecord(OrderScoreInfo scoreOrder) {
		
		//订单买入时的交易方向
		Integer tradeDirection = scoreOrder.getTradeDirection();

		//计算委托单的委托方向
		TradeDirectVo tradeDirectVo = new TradeDirectVo(tradeDirection,TradeTypeEnum.SELL.getValue());
				
		//委托价
		double entrustPrice = scoreOrder.getUserCommitSellPrice();
		//限价浮动值: 限价加减点位 
		double limitedPriceValue = -999;
		//委托价类型
		Integer entrustType = EntrustPriceTypeEnum.LIMIT_PRICE.getValue();
		//获取产品波动点位、波动价格
		ProductVo productVo = productInfoServiceImpl.queryProduct(scoreOrder.getProductCode());
		if(1 == productVo.getIsMarketPrice()){
			entrustType = EntrustPriceTypeEnum.MARKET_PRICE.getValue();
			entrustPrice = 0;
		} else {
			//限价加减点位
			limitedPriceValue = productVo.getLimitedPriceValue();
		}
		
		//时间
		Date now = new Date();
		//生成报给交易所的委托单号
		int entrustCode = TradeUtils.makeEntrustCode();
		String entrustId = String.valueOf(entrustCode);
		
		//初始化卖出委托单		
		OrderScoreEntrustInfo orderScoreEntrustInfo = new OrderScoreEntrustInfo(scoreOrder.getOrderId(), entrustId,scoreOrder.getProductId(), 
				scoreOrder.getProductCode(), scoreOrder.getProductName(), scoreOrder.getExchangeCode(), 
				scoreOrder.getPlate(), scoreOrder.getSecurityCode(),scoreOrder.getInvestorId(),
				scoreOrder.getAccountId(),TradeTypeEnum.SELL.getValue(), tradeDirection,
				tradeDirectVo.getTradeOffset(), tradeDirectVo.getTradeDirect(), scoreOrder.getSellEntrustCount(), 
				entrustPrice, entrustType, limitedPriceValue, now, now);
		//触发方式
		orderScoreEntrustInfo.setTriggerType(scoreOrder.getSellTriggerType());
				
		return orderScoreEntrustInfo;
	}


	/**
	 * TODO 查询订单主数据信息
	 * @author XieZhibing
	 * @date 2016年12月13日 上午9:27:50
	 * @param orderVo
	 * @return
	 */
	private OrderScoreInfo queryScoreOrdersInfo(OrderVo orderVo) throws LTException {
		//订单显示ID
		String orderId = orderVo.getOrderId();
		//查询缓存数据
		OrderScoreInfo scoreOrder = OrderScoreInfoCache.get(orderId);
		if(scoreOrder == null || scoreOrder.getBuySuccessCount() == 0 || scoreOrder.getHoldCount() == 0) {//如果买成功数为0，查下数据库
			//查询数据库中订单基本数据
			scoreOrder = orderScoreInfoDao.queryByOrderId(orderId);
		}
		if(scoreOrder == null) {
			throw new LTException("查不到订单, orderId:{"+ orderId +"}");
		}
			
		logger.info("=====执行queryCashOrdersInfo=cashOrder={}====",JSONObject.toJSONString(scoreOrder));
		
		//不是持仓中订单，打回
		if(!(scoreOrder.getHoldCount() > 0 && (scoreOrder.getSellEntrustCount() == null || scoreOrder.getSellEntrustCount() == 0))){
			throw new LTException(LTResponseCode.TDJ0003);
		}
		
		//产品编码
		String productCode = scoreOrder.getProductCode();
		//产品
		ProductVo productVo = productInfoServiceImpl.queryCheckProduct(productCode);
		orderVo.setProductVo(productVo);
		//产品价格小数位数
		Integer scale = productVo.getDecimalDigits();
		
		scoreOrder.setUserCommitSellDate(orderVo.getUserSaleDate());	

		
		//价格
		double quotePrice = orderVo.getUserSalePrice();			
		//系统提交卖出价
		if(scoreOrder.getPlate() == PlateEnum.INNER_PLATE.getValue()){
			quotePrice = tradeServer.getInnerFutureTrade().getQuotePrice(scoreOrder.getProductCode()).getLastPrice();
		} else if(scoreOrder.getPlate() == PlateEnum.OUTER_PLATE.getValue()){
			quotePrice = tradeServer.getOuterFutureTrade().getQuotePrice(scoreOrder.getProductCode()).getLastPrice();
		}
		quotePrice = DoubleTools.scaleFormat(quotePrice, scale);
		
		//用户提交卖出价格
		scoreOrder.setUserCommitSellPrice(quotePrice);
		scoreOrder.setUserCommitSellDate(orderVo.getUserSaleDate());
		//卖出触发方式
		scoreOrder.setSellTriggerType(orderVo.getSellTriggerType());
		//设置卖出委托数量
		scoreOrder.setSellEntrustCount(scoreOrder.getHoldCount());
		//平仓成功数量
		scoreOrder.setSellSuccessCount(0);
		scoreOrder.setModifyUserId(orderVo.getModifyUserId());
		return scoreOrder;
	}

	/** 
	 * 获取 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @return fundTradeApiService 
	 */
	public IFundTradeApiService getFundTradeApiService() {
		return fundTradeApiService;
	}

	/** 
	 * 设置 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @param fundTradeApiService 资金分布式接口: 资金扣款、结算、退款业务接口 
	 */
	public void setFundTradeApiService(IFundTradeApiService fundTradeApiService) {
		this.fundTradeApiService = fundTradeApiService;
	}

	/** 
	 * 获取 交易服务 
	 * @return tradeServer 
	 */
	public TradeServer getTradeServer() {
		return tradeServer;
	}

	/** 
	 * 设置 交易服务 
	 * @param tradeServer 交易服务 
	 */
	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	/*

	@Override
	public void updateScoreOrder(OrderScoreInfo scoreOrdersInfo) throws LTException {
	}*/

	@Override
	public OrderScoreInfoDao getOrderScoreInfoDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
