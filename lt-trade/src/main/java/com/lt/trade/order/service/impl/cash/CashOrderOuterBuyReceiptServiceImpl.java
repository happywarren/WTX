/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderBuyServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.cash;

import java.text.ParseException;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.constant.LTConstant;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderCashSuccessInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.OrderCashEntrustInfoDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.dao.OrderCashSuccessInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;

/**
 * TODO 外盘订单买入委托数据、回执数据处理
 * @author XieZhibing
 * @date 2016年12月12日 下午1:52:10
 * @version <b>1.0.0</b>
 */
@Service
public class CashOrderOuterBuyReceiptServiceImpl extends AbstractCashOrderBuyReceiptService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1050229096357327075L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(CashOrderOuterBuyReceiptServiceImpl.class);
	
	/** 订单基本信息数据接口 */
	@Autowired
	private OrderCashInfoDao orderCashInfoDao;
	@Autowired
	private OrderCashEntrustInfoDao orderCashEntrustInfoDao;
	/** 成交结果数据接口 */
	@Autowired
	private OrderCashSuccessInfoDao orderCashSuccessInfoDao;
	/** 资金分布式接口: 资金扣款、结算、退款业务接口 */
	@Autowired
	private IFundTradeApiService fundTradeApiService;
	/** 交易服务 */
	@Autowired
	private TradeServer tradeServer;
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	@Autowired
	private IInvestorFeeCfgApiService investorFeeCfgApiService;
	
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;
	
	/**
	 * 外盘处理数据
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {

		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2003]:[外盘]收到C++返回委托回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//订单基本信息
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		
		
		//从redis中获取产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
	
		Double jumpPrice = productVo.getJumpValue();
		
		//当前时间
		Date now = new Date();
		//更新订单持仓数量对象的买入成功数量、买入均价、持仓数量等信息
		cashOrdersInfo = this.fillBuyCashOrderCount(now,matchBean.getMatchPrice(), 
					matchBean.getOrderTotal(), matchBean.getMatchVol(),matchBean.getMatchTotal(), cashOrdersInfo, jumpPrice,entrustRecord.getTradeType());
		
		//设置期货公司委托id
		String sysOrderId = matchBean.getSysOrderId();
		if(!StringTools.isNotEmpty(sysOrderId)){
			sysOrderId = sysOrderId.trim();
		}
		try {
			Date date = DateTools.toDefaultDateTime(matchBean.getMatchDateTime());
			cashOrdersInfo.setLastBuyDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("时间转换异常：订单完成时间 MatchDateTime:{},lastBuyDate:{}",matchBean.getMatchDateTime(),cashOrdersInfo.getLastBuyDate());
		}
		cashOrdersInfo.setModifyDate(now);
		//成交记录实体组装
		OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), cashOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), cashOrdersInfo.getLastBuyDate(), now);
		//设置成交状态为部分成交
		orderCashSuccessInfo.setSuccessStatus(1);
		//更新订单信息
		orderCashInfoDao.update(cashOrdersInfo);	
		logger.info("[2003]:[外盘]更新持仓数量信息, displayId:{}", orderId);
		
		//新增成交记录
		orderCashSuccessInfoDao.add(orderCashSuccessInfo);
		logger.info("[2003]:[外盘]新增成交记录, displayId:{}", orderId);
	
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient( cashOrdersInfo.getUserId(),sendOrderMsgProducer);
				
	}
	
	/**
	 * 更新持仓数量、买入成功数量、买入均价、买入时间、添加成交记录<br/>
	 * 买入失败时，记录买入失败数量、并退款
	 * @author XieZhibing
	 * @date 2016年12月13日 下午4:00:02
	 * @see com.lt.trade.order.service.ICashOrderBaseService#doPersist2004(java.lang.String, java.lang.Integer, java.lang.Double, java.lang.Integer, java.lang.String, com.lt.model.trade.CashOrdersEntrustRecord)
	 * @param matchDateTime 成交时间
	 * @param matchVol 本次成交数量
	 * @param matchPrice 成交价
	 * @param matchTotal 已成交数量
	 * @param orderTotal 委托数量
	 * @param sysMatchId 期货公司成交ID
	 * @param entrustRecord 买入委托单
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {
		
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2004]:[外盘]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//订单基本信息
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);		
		if(cashOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		//从redis中获取产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
		
		Double jumpPrice = productVo.getJumpValue();
		
		Date now = new Date();
		
		//更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
		cashOrdersInfo = this.fillBuyCashOrderCount(now,matchBean.getMatchPrice(), 
				matchBean.getOrderTotal(), matchBean.getMatchVol(),matchBean.getMatchTotal(), cashOrdersInfo, jumpPrice,entrustRecord.getTradeType());
	
		//重新计算止盈价、止损价
		this.calculateStopLossProfitPrice(cashOrdersInfo, productVo.getDecimalDigits());

		cashOrdersInfo.setModifyDate(now);
		try {
			Date date = DateTools.toDefaultDateTime(matchBean.getMatchDateTime());
			cashOrdersInfo.setLastBuyDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("时间转换异常：订单完成时间 MatchDateTime:{},lastBuyDate:{}",matchBean.getMatchDateTime(),cashOrdersInfo.getLastBuyDate());
		}
		//成交记录实体组装
		OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), cashOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), 
					matchBean.getMatchPrice(), cashOrdersInfo.getLastBuyDate(), now);
		//设置成交状态为完全成交
		orderCashSuccessInfo.setSuccessStatus(2);
		//更新订单信息
		orderCashInfoDao.update(cashOrdersInfo);
		logger.info("[2004]:[外盘]更新订单信息, orderId:{}", orderId);		

		//新增成交记录
		orderCashSuccessInfoDao.add(orderCashSuccessInfo);
		logger.info("[2004]:[外盘]新增成交记录, orderId:{}", orderId);
		
		//退款
		if(cashOrdersInfo.getBuyFailCount() > 0){
			//退款
			this.refund(cashOrdersInfo, cashOrdersInfo.getBuyFailCount());
		}

		//删除缓存中的买入委托单
		OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
		
		//订单加入到风控监控队列
		this.fillRiskControlQueue(cashOrdersInfo);
		logger.info("[2004]:[外盘]订单加入到风控监控队列, displayId:{}", orderId);
		
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(),sendOrderMsgProducer);
		
	}

	@Override
	public void doPersist(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {
		
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[------------]:[外盘]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//订单基本信息
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);		
		if(cashOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		Date now = new Date();
//		try {
//			//保存订单主数据
//			cashOrdersInfo.setModifyDate(now);
//			getOrderCashInfoDao().add(cashOrdersInfo);
//			//绑定订单ID,修改时间
//			entrustRecord.setOrderId(orderId);
//			entrustRecord.setModifyDate(now);
//			//委托实体入库
//			addEntrustInfo(entrustRecord);
//		} catch (Exception e) {
//			logger.error("已经处理2012回执不需要再处理");
//		}
		//从redis中获取产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
		
		Double jumpPrice = productVo.getJumpValue();
		
		
		//更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
		cashOrdersInfo = this.fillBuyCashOrderCount(now,matchBean.getMatchPrice(), 
				matchBean.getOrderTotal(), matchBean.getMatchVol(),matchBean.getMatchTotal(), cashOrdersInfo, jumpPrice,entrustRecord.getTradeType());
			
		//重新计算止盈价、止损价
		this.calculateStopLossProfitPrice(cashOrdersInfo, productVo.getDecimalDigits());
		
		cashOrdersInfo.setModifyDate(now);
		try {
			Date date = DateTools.toDefaultDateTime(matchBean.getMatchDateTime());
			cashOrdersInfo.setLastBuyDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("时间转换异常：订单完成时间 MatchDateTime:{},lastBuyDate:{}",matchBean.getMatchDateTime(),cashOrdersInfo.getLastBuyDate());
		}
		//成交记录实体组装
		OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), cashOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), 
					matchBean.getMatchPrice(), cashOrdersInfo.getLastBuyDate(), now);
		boolean flag = false;
		if(cashOrdersInfo.getBuyEntrustCount() == cashOrdersInfo.getHoldCount()){
			//设置成交状态为完全成交
			orderCashSuccessInfo.setSuccessStatus(2);
			flag = true;
		}else{
			//设置成交状态为部分成交
			orderCashSuccessInfo.setSuccessStatus(1);
		}	
		
		//更新订单信息
		orderCashInfoDao.update(cashOrdersInfo);
		logger.info("[2004]:[外盘]更新订单信息, orderId:{}", orderId);	
		
		//新增成交记录
		orderCashSuccessInfoDao.add(orderCashSuccessInfo);
		logger.info("[2004]:[外盘]新增成交记录, orderId:{}", orderId);
		
		if(flag){
			//删除缓存中的买入委托单
			OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
			//订单加入到风控监控队列
			this.fillRiskControlQueue(cashOrdersInfo);
			logger.info("[2004]:[外盘]订单加入到风控监控队列, displayId:{}", orderId);
		}
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient( cashOrdersInfo.getUserId(),sendOrderMsgProducer);
		
	}
	

	/**
	 * TODO 更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:06:16
	 * @param flag 完全成交标识：2003部分成交、2004全部成交
	 * @param matchDateTime 成交时间
	 * @param matchPrice 成交价
	 * @param orderTotal 委托数量
	 * @param matchVol 本次成交数量
	 * @param cashOrdersCount
	 * @param scale 产品价格小数位数	
	 */
	public OrderCashInfo fillBuyCashOrderCount(Date lastBuyDate, Double matchPrice,
			Integer orderTotal, Integer matchVol,Integer matchTotal, OrderCashInfo orderCashInfo, Double jumpPrice,Integer tradeType) {
		//已买入成功手数、买入均价
		Integer buySuccCount = orderCashInfo.getBuySuccessCount();
		
		//计算买入均价、买入成功数量
		AvgPriceVo avgPriceVo = new AvgPriceVo(orderCashInfo.getBuyPriceTotal(), orderCashInfo.getBuyMinPrice(), buySuccCount, matchVol, matchPrice, 
															orderCashInfo.getTradeDirection(), tradeType, jumpPrice);

		logger.info("=============买入均价，买入成功数量==avgPriceVo={}===========",JSONObject.toJSONString(avgPriceVo));
		
		orderCashInfo.setBuyMinPrice(avgPriceVo.getMinPrice());//最小价
		orderCashInfo.setBuyPriceTotal(avgPriceVo.getPriceTotal());//总价
		//更新买入时间
		orderCashInfo.setLastBuyDate(lastBuyDate);

		//更新已买入成功手数
		orderCashInfo.setBuySuccessCount(avgPriceVo.getCount());
		//更新买入均价		
		orderCashInfo.setBuyAvgPrice(avgPriceVo.getAvgPrice());
		//更新持仓数量
		orderCashInfo.setHoldCount(orderCashInfo.getBuySuccessCount());
		
		//处理实扣字段
		this.dealActualParams(orderCashInfo, matchVol);

		logger.info("[外盘]更新订单持仓, sysBuyAvgPrice:{}, buySuccCount:{}", avgPriceVo.getAvgPrice(), orderCashInfo.getBuySuccessCount());
		return orderCashInfo;
	}
	
	/**
	 * 处理实扣订单字段值
	 * @param orderCashInfo
	 * @param matchTotal
	 */
	public void dealActualParams(OrderCashInfo orderCashInfo,Integer matchVol){
		//汇率
		double rate = orderCashInfo.getRate();
		
		//每手止损金额(原币种)
		double perStopLoss = DoubleTools.scaleFormat(orderCashInfo.getPerStopLoss());		
		//单手保证金附加费
		double perSurcharge = DoubleTools.scaleFormat(orderCashInfo.getPerSurcharge());
		
		//每手止损保证金
		double perHoldFund = DoubleTools.scaleFormat(DoubleTools.add(perSurcharge, perStopLoss)) ;
		//本次实扣保证金
		double actualHoldFund = DoubleTools.mul(DoubleTools.mul(perHoldFund, rate),matchVol);

		//实扣递延保证金
		double actualDeferFund=0.0;
		if(orderCashInfo.getDeferStatus() == DeferStatusEnum.DEFER.getValue()){//如果开启递延，更新实扣递延保证金
			//每手递延保证金
			double perDeferFund = DoubleTools.scaleFormat(orderCashInfo.getPerDeferFund());

			actualDeferFund = DoubleTools.mul(DoubleTools.mul( perDeferFund , rate ), matchVol);
			
		}
		
		//每手手续费
		double perCounterFee = DoubleTools.scaleFormat(orderCashInfo.getPerCounterFee());		
		//实扣手续费
		double actualCounterFee = DoubleTools.mul(DoubleTools.mul( perCounterFee ,rate  ), matchVol); 
		
		orderCashInfo.setActualHoldFund(orderCashInfo.getActualHoldFund()+actualHoldFund);
		orderCashInfo.setActualDeferFund(orderCashInfo.getActualDeferFund()+actualDeferFund);
		orderCashInfo.setActualCounterFee(orderCashInfo.getActualCounterFee()+actualCounterFee);
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

	
	@Override
	public OrderCashInfoDao getOrderCashInfoDao() {
		return this.orderCashInfoDao;
	}

	@Override
	public OrderCashEntrustInfoDao getOrderCashEntrustInfoDao() {
		return this.orderCashEntrustInfoDao;
	}
	
	
}
