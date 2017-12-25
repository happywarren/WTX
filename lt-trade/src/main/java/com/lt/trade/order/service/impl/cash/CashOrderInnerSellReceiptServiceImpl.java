/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderInnerSellReceiptServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.cash;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderCashSuccessInfo;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.OrderCashEntrustChildInfoDao;
import com.lt.trade.order.dao.OrderCashEntrustInfoDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.dao.OrderCashSuccessInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

/**
 * TODO 卖出订单数据持久化处理
 * @author XieZhibing
 * @date 2016年12月14日 下午4:02:59
 * @version <b>1.0.0</b>
 */
@Service
public class CashOrderInnerSellReceiptServiceImpl extends AbstractCashOrderSellReceiptService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(CashOrderInnerSellReceiptServiceImpl.class);
	
	/** 订单基本信息数据接口 */
	@Autowired
	private OrderCashInfoDao orderCashInfoDao;
	/** 委托单数据接口 */
	@Autowired
	private OrderCashEntrustInfoDao orderCashEntrustInfoDao;
	/** 委托单数据接口 */
	@Autowired
	private OrderCashEntrustChildInfoDao orderCashEntrustChildInfoDao;
	/** 成交结果数据接口 */
	@Autowired
	private OrderCashSuccessInfoDao orderCashSuccessInfoDao;
	/** 资金分布式接口: 资金扣款、结算、退款业务接口 */
	@Autowired
	private IFundTradeApiService fundTradeApiService;
	/** 券商费用配置接口 */
	@Autowired
	private IInvestorFeeCfgApiService investorFeeCfgApiService;
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	@Autowired
	private MessageQueueProducer promoteProducer;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;


	/**
	 *
	 * @param matchBean
	 * @param entrustRecord
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean,OrderCashEntrustInfo entrustRecord) {

		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2003]:[内盘]收到C++返回委托回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//订单持仓数量对象
		OrderCashInfo orderCashInfo = OrderCashInfoCache.get(orderId);
		if(orderCashInfo == null) {
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单持仓数量数据!", orderId);
			return;
		}
		//修改时间
		Date modifyDate = new Date();
		entrustRecord.setModifyDate(modifyDate);
		orderCashInfo.setModifyDate(modifyDate);
		
		//更新平仓委托数量
		if(matchBean.getMatchTotal() <= matchBean.getOrderTotal()) {
			//卖委托数量
			orderCashInfo.setSellEntrustCount(orderCashInfo.getSellEntrustCount()-matchBean.getMatchTotal());
			//更新持仓数量
			orderCashInfo.setHoldCount(orderCashInfo.getHoldCount()-matchBean.getMatchTotal());
		}
		
		//更新订单
		orderCashInfoDao.update(orderCashInfo);	
		logger.info("[2003]:[内盘]更新持仓数量, displayId:{}", orderId);
		//更新 交易所返回的委托单号
		orderCashEntrustInfoDao.update(entrustRecord);	
		logger.info("[2003]:[内盘]更新委托单, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
	}

	/**
	 *
	 * @param matchBean
	 * @param entrustRecord 买入(卖出)委托单
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2004]:[内盘]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//现金订单基本数据实例
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单基本数据!", orderId);
			return;
		}
		
		
		//产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(cashOrdersInfo.getProductCode());
	
		//修改时间
		Date now = new Date();
		Double jumpPrice = productVo.getJumpValue();

		//更新订单持仓数量对象的卖出成功数量、卖出均价、持仓数量等信息
		this.fillSellCashOrderCount(now, matchBean.getMatchPrice(),
				matchBean.getMatchVol(), cashOrdersInfo, jumpPrice,entrustRecord.getTradeType());
		
		try {
			Date date = DateTools.toDefaultDateTime(matchBean.getMatchDateTime());
			cashOrdersInfo.setLastSellDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("时间转换异常：订单完成时间 MatchDateTime:{},lastBuyDate:{}",matchBean.getMatchDateTime(),cashOrdersInfo.getLastBuyDate());
		}
		
		OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
				entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
				entrustRecord.getProductCode(), entrustRecord.getProductName(), 
				entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
				entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 				
				entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
				entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
				matchBean.getMatchVol(), cashOrdersInfo.getSellSuccessCount(), 
				matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), cashOrdersInfo.getLastSellDate(), now);
		//设置成交状态为完全成交
		orderCashSuccessInfo.setSuccessStatus(2);
		
		//更新订单基本信息数据
		cashOrdersInfo.setModifyDate(now);
		
		//买入均价
		Double sysBuyAvgPrice = cashOrdersInfo.getBuyAvgPrice();
		//卖出均价
		Double sysSellAvgPrice = cashOrdersInfo.getSellAvgPrice();
		
		//计算盈亏
		this.dealLossProfit(cashOrdersInfo, matchBean.getMatchTotal(),matchBean.getMatchVol(), sysBuyAvgPrice, sysSellAvgPrice);	
				
		//更新结算日期、结算盈亏、系统结算盈亏
		orderCashInfoDao.update(cashOrdersInfo);
		
		//新增成交记录
		orderCashSuccessInfoDao.add(orderCashSuccessInfo);
		logger.info("[2004]:[内盘]新增成交记录, displayId:{}", orderId);
		
		//资金结算
		this.fundBalance(cashOrdersInfo, matchBean.getMatchVol());
				
		//删除缓存中的委托单
		OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());

		//删除缓存中的订单基本信息数据
		OrderCashInfoCache.remove(orderId);

		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(),sendOrderMsgProducer);
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
	}

	/**
	 * 更新订单持仓数量对象的卖出成功数量、卖出均价、卖出时间、持仓数量等信息
	 * @param lastBuyDate
	 * @param matchPrice
	 * @param matchVol
	 * @param orderCashInfo
	 * @param jumpPrice
	 * @return
	 */
	private OrderCashInfo fillSellCashOrderCount(Date lastBuyDate, Double matchPrice,
			Integer matchVol, OrderCashInfo orderCashInfo ,Double jumpPrice,Integer tradeType) {

		//已卖出成功手数、卖出均价
		Integer sellSuccCount = orderCashInfo.getSellSuccessCount();

		//计算卖出均价、卖出成功数量
		AvgPriceVo avgPriceVo = new AvgPriceVo(orderCashInfo.getSellPriceTotal(), orderCashInfo.getSellMinPrice(), sellSuccCount, matchVol, matchPrice,
				orderCashInfo.getTradeDirection(), tradeType, jumpPrice);

		orderCashInfo.setSellMinPrice(avgPriceVo.getMinPrice());//最小价
		orderCashInfo.setSellPriceTotal(avgPriceVo.getPriceTotal());//总价
		//持仓数量
		orderCashInfo.setHoldCount(orderCashInfo.getHoldCount()-matchVol);
		//更新卖出时间

		orderCashInfo.setLastSellDate(lastBuyDate);
		//更新已卖出成功手数
		orderCashInfo.setSellSuccessCount(avgPriceVo.getCount());
		//更新委托卖数量
		orderCashInfo.setSellEntrustCount(orderCashInfo.getSellEntrustCount()-matchVol);

		//更新卖出均价
		orderCashInfo.setSellAvgPrice(avgPriceVo.getAvgPrice());
		logger.info("[内盘]更新订单持仓, sysSellAvgPrice:{}, sellSuccCount:{}", avgPriceVo.getAvgPrice(), sellSuccCount);
		return orderCashInfo;
	}

	
	/** 
	 * 获取 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @return fundTradeApiService 
	 */
	@Override
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
	 * 获取 券商费用配置接口 
	 * @return investorFeeCfgApiService 
	 */
	@Override
	public IInvestorFeeCfgApiService getInvestorFeeCfgApiService() {
		return investorFeeCfgApiService;
	}
	/** 
	 * 设置 券商费用配置接口 
	 * @param investorFeeCfgApiService 券商费用配置接口 
	 */
	public void setInvestorFeeCfgApiService(IInvestorFeeCfgApiService investorFeeCfgApiService) {
		this.investorFeeCfgApiService = investorFeeCfgApiService;
	}
	@Override
	public OrderCashInfoDao getOrderCashInfoDao() {
		return this.orderCashInfoDao;
	}
	@Override
	public OrderCashEntrustInfoDao getOrderCashEntrustInfoDao() {
		return this.orderCashEntrustInfoDao;
	}
	@Override
	public OrderCashEntrustChildInfoDao getOrderCashEntrustChildInfoDao() {
		return this.orderCashEntrustChildInfoDao;
	}
	@Override
	public void doPersist(FutureMatchBean matchBean,
			OrderCashEntrustInfo entrustRecord) {
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[外盘2004]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//现金订单基本数据实例
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("没有在缓存中查找到订单显示ID为:{}的外盘现金订单基本数据!", orderId);
			return;
		}

		//修改时间
		Date now = new Date();
		entrustRecord.setModifyDate(now);

		//产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());

		Double jumpPrice = productVo.getJumpValue();

		//更新订单持仓数量对象的卖出成功数量、卖出均价、持仓数量等信息
		this.fillSellCashOrderCount(now, matchBean.getMatchPrice(),
				matchBean.getMatchVol(), cashOrdersInfo, jumpPrice,entrustRecord.getTradeType());
		try {
			Date date = DateTools.toDefaultDateTime(matchBean.getMatchDateTime());
			cashOrdersInfo.setLastSellDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("时间转换异常：订单完成时间 MatchDateTime:{},lastBuyDate:{}",matchBean.getMatchDateTime(),cashOrdersInfo.getLastBuyDate());
		}

		//成交记录
		OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
				entrustRecord.getProductId(),entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
				entrustRecord.getProductCode(), entrustRecord.getProductName(),
				entrustRecord.getExchangeCode(), entrustRecord.getPlate(),
				entrustRecord.getSecurityCode(), entrustRecord.getTradeType(),
				entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(),
				entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(),
				matchBean.getMatchVol(), cashOrdersInfo.getSellSuccessCount(),
				matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), cashOrdersInfo.getLastSellDate(), now);
		//买入均价
		Double sysBuyAvgPrice = cashOrdersInfo.getBuyAvgPrice();
		//卖出均价
		Double sysSellAvgPrice = cashOrdersInfo.getSellAvgPrice();

		//计算盈亏
		this.dealLossProfit(cashOrdersInfo, matchBean.getMatchTotal(),matchBean.getMatchVol(), sysBuyAvgPrice, sysSellAvgPrice);
		boolean flag = false;
		if(cashOrdersInfo.getHoldCount() != 0){
			//设置成交状态为部分成交
			orderCashSuccessInfo.setSuccessStatus(1);
		}else{
			//设置成交状态为完全成交
			orderCashSuccessInfo.setSuccessStatus(2);
			flag = true;
		}

		//更新结算日期、结算盈亏、系统结算盈亏
		orderCashInfoDao.update(cashOrdersInfo);

		//新增成交记录
		orderCashSuccessInfoDao.add(orderCashSuccessInfo);
		logger.info("[外盘]新增成交记录, displayId:{}", orderId);

		//资金结算
		this.fundBalance(cashOrdersInfo, matchBean.getMatchVol());
		//平仓结束
		if(flag){
			//删除缓存中的委托单
			OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
			//删除缓存中的订单基本信息数据
			OrderCashInfoCache.remove(orderId);
			//设置卖出完成标记
			TradeUtils.setSuccess(redisTemplate, cashOrdersInfo.getUserId(), orderId);

			//用于统计推广数据
			JSONObject json = new JSONObject();
			json.put("userId", cashOrdersInfo.getUserId());
			json.put("orderId", cashOrdersInfo.getOrderId());
			json.put("tradeHandCount", cashOrdersInfo.getBuySuccessCount());
			json.put("tradeAmount", cashOrdersInfo.getActualCounterFee());
			json.put("dateTime", cashOrdersInfo.getEntrustSellDate());
			logger.info("===========通知推广模块统计===========");
			promoteProducer.sendMessage(json);
		}
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(),sendOrderMsgProducer);


	}
	
}
