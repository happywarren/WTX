/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderBuyServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.constant.LTConstant;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.trade.OrderScoreSuccessInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.OrderScoreEntrustInfoDao;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.dao.OrderScoreSuccessInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * TODO 外盘订单买入委托数据、回执数据处理
 * @author XieZhibing
 * @date 2016年12月12日 下午1:52:10
 * @version <b>1.0.0</b>
 */
@Service
public class ScoreOrderOuterBuyReceiptServiceImpl extends AbstractScoreOrderBuyReceiptService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1050229096357327075L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ScoreOrderOuterBuyReceiptServiceImpl.class);
	
	/** 订单基本信息数据接口 */
	@Autowired
	private OrderScoreInfoDao orderScoreInfoDao;
	@Autowired
	private OrderScoreEntrustInfoDao orderScoreEntrustInfoDao;
	/** 成交结果数据接口 */
	@Autowired
	private OrderScoreSuccessInfoDao orderScoreSuccessInfoDao;
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
	public void doPersist2003(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {

		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2003]:[外盘]收到C++返回委托回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//订单基本信息
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		
		
		//从redis中获取产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
		//产品价格小数位数
		//Integer scale = productVo.getDecimalDigits();
		
		Double jumpPrice = productVo.getJumpValue();
		//当前时间
		Date now = new Date();
		//更新订单持仓数量对象的买入成功数量、买入均价、持仓数量等信息
		ScoreOrdersInfo = this.fillBuyScoreOrderCount(now,matchBean.getMatchPrice(), 
					matchBean.getOrderTotal(), matchBean.getMatchVol(),matchBean.getMatchTotal(), ScoreOrdersInfo, jumpPrice,entrustRecord.getTradeType());
			
		//设置期货公司委托id
		String sysOrderId = matchBean.getSysOrderId();
		if(!StringTools.isNotEmpty(sysOrderId)){
			sysOrderId = sysOrderId.trim();
		}
		
		ScoreOrdersInfo.setModifyDate(now);
		//成交记录实体组装
		OrderScoreSuccessInfo OrderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), ScoreOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), ScoreOrdersInfo.getLastBuyDate(), now);
		
		//更新订单信息
		orderScoreInfoDao.update(ScoreOrdersInfo);	
		logger.info("[2003]:[外盘]更新持仓数量信息, displayId:{}", orderId);
		
		//新增成交记录
		orderScoreSuccessInfoDao.add(OrderScoreSuccessInfo);
		logger.info("[2003]:[外盘]新增成交记录, displayId:{}", orderId);
		
		
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(ScoreOrdersInfo.getUserId(),sendOrderMsgProducer);

	}
	
	/**
	 * 更新持仓数量、买入成功数量、买入均价、买入时间、添加成交记录<br/>
	 * 买入失败时，记录买入失败数量、并退款
	 * @author XieZhibing
	 * @date 2016年12月13日 下午4:00:02
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2004(String, Integer, Double, Integer, String, com.lt.model.trade.ScoreOrdersEntrustRecord)
	 * @param matchDateTime 成交时间
	 * @param matchVol 本次成交数量
	 * @param matchPrice 成交价
	 * @param matchTotal 已成交数量
	 * @param orderTotal 委托数量
	 * @param sysMatchId 期货公司成交ID
	 * @param entrustRecord 买入委托单
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {

		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2004]:[外盘]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//订单基本信息
		OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);		
		if(scoreOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		
		//从redis中获取产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
		//产品价格小数位数
		//Integer scale = productVo.getDecimalDigits();
		Double jumpPrice = productVo.getJumpValue();
		
		//成交记录
		Date now = new Date();
				
		//更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
		this.fillBuyScoreOrderCount(now,matchBean.getMatchPrice(), 
				matchBean.getOrderTotal(), matchBean.getMatchVol(),matchBean.getMatchTotal(), scoreOrdersInfo, jumpPrice,entrustRecord.getTradeType());
	
				
		//重新计算止盈价、止损价
		this.calculateStopLossProfitPrice(scoreOrdersInfo, productVo.getDecimalDigits());
		
		scoreOrdersInfo.setModifyDate(now);
		
		//成交记录实体组装
		OrderScoreSuccessInfo orderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), scoreOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), scoreOrdersInfo.getLastBuyDate(), now);
		//设置成交状态为完全成交
		orderScoreSuccessInfo.setSuccessStatus(2);
		//更新订单信息
		orderScoreInfoDao.update(scoreOrdersInfo);
		logger.info("[2004]:[外盘]更新订单信息, orderId:{}", orderId);		

		//新增成交记录
		orderScoreSuccessInfoDao.add(orderScoreSuccessInfo);
		logger.info("[2004]:[外盘]新增成交记录, orderId:{}", orderId);
		
		//退款
		if(scoreOrdersInfo.getBuyFailCount() > 0){
			//退款
			this.refund(scoreOrdersInfo, scoreOrdersInfo.getBuyFailCount());
		}

		//删除缓存中的买入委托单
		OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
		//订单加入到风控监控队列
		this.fillRiskControlQueue(scoreOrdersInfo);
		logger.info("[2004]:[外盘]订单加入到风控监控队列, displayId:{}", orderId);
		
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient( scoreOrdersInfo.getUserId(),sendOrderMsgProducer);
		
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
	 * @param ScoreOrdersCount
	 * @param scale 产品价格小数位数	
	 */
	public OrderScoreInfo fillBuyScoreOrderCount(Date lastBuyDate, Double matchPrice,
			Integer orderTotal, Integer matchVol,Integer matchTotal, OrderScoreInfo orderScoreInfo, Double jumpPrice,Integer tradeType) {
		//已买入成功手数、买入均价
		Integer buySuccCount = orderScoreInfo.getBuySuccessCount();
		
		//计算买入均价、买入成功数量
		AvgPriceVo avgPriceVo = new AvgPriceVo(orderScoreInfo.getBuyPriceTotal(), orderScoreInfo.getBuyMinPrice(), buySuccCount, matchVol, matchPrice, 
					orderScoreInfo.getTradeDirection(), tradeType, jumpPrice);
		
		logger.info("=============买入均价，买入成功数量==avgPriceVo={}===========",JSONObject.toJSONString(avgPriceVo));
		
		orderScoreInfo.setBuyMinPrice(avgPriceVo.getMinPrice());//最小价
		orderScoreInfo.setBuyPriceTotal(avgPriceVo.getPriceTotal());//总价
		
		//更新未成功数量 = 买入委托数量 - 买入成功数量
		Integer buyFailCount = orderTotal - avgPriceVo.getCount();
		orderScoreInfo.setBuyFailCount(buyFailCount);
		
		//更新买入时间
		orderScoreInfo.setLastBuyDate(lastBuyDate);

		//更新已买入成功手数
		orderScoreInfo.setBuySuccessCount(avgPriceVo.getCount());
		//更新买入均价		
		orderScoreInfo.setBuyAvgPrice(avgPriceVo.getAvgPrice());
		//更新持仓数量
		orderScoreInfo.setHoldCount(orderScoreInfo.getBuySuccessCount());
		
		//处理实扣字段
		this.dealActualParams(orderScoreInfo, matchTotal);

		logger.info("[外盘]更新订单持仓, sysBuyAvgPrice:{}, buySuccCount:{}", avgPriceVo.getAvgPrice(), orderScoreInfo.getBuySuccessCount());
		return orderScoreInfo;
	}
	
	/**
	 * 处理实扣订单字段值
	 * @param orderScoreInfo
	 * @param matchTotal
	 */
	public void dealActualParams(OrderScoreInfo orderScoreInfo,Integer matchTotal){
		//汇率
		double rate = orderScoreInfo.getRate();
		
		//每手止损金额(原币种)
		double perStopLoss = orderScoreInfo.getPerStopLoss();		
		//单手保证金附加费
		double perSurcharge = orderScoreInfo.getPerSurcharge();
		
		//每手止损保证金
		double perHoldFund = DoubleTools.scaleFormat(DoubleTools.add(perSurcharge, perStopLoss)) ;
		//本次实扣保证金
		double actualHoldFund = DoubleTools.mul(DoubleTools.mul(perHoldFund, rate),matchTotal);
		
		//实扣递延保证金
		double actualDeferFund=0.0;
		if(orderScoreInfo.getDeferStatus() == DeferStatusEnum.DEFER.getValue()){//如果开启递延，更新实扣递延保证金
			//每手递延保证金
			double perDeferFund = DoubleTools.scaleFormat(orderScoreInfo.getPerDeferFund());

			actualDeferFund = DoubleTools.mul(DoubleTools.mul(perDeferFund ,rate  ), matchTotal);
			
		}
		
		//每手手续费
		double perCounterFee = DoubleTools.scaleFormat(orderScoreInfo.getPerCounterFee());		
		//实扣手续费
		double actualCounterFee =  DoubleTools.mul(DoubleTools.mul(perCounterFee , rate ), matchTotal); 
		
		orderScoreInfo.setActualHoldFund(actualHoldFund);
		orderScoreInfo.setActualDeferFund(actualDeferFund);
		orderScoreInfo.setActualCounterFee(actualCounterFee);
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
	public OrderScoreInfoDao getOrderScoreInfoDao() {
		return this.orderScoreInfoDao;
	}

	@Override
	public OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao() {
		return this.orderScoreEntrustInfoDao;
	}

	@Override
	public void doPersist(FutureMatchBean matchBean,
				OrderScoreEntrustInfo entrustRecord) {
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2004]:[外盘]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//订单基本信息
		OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);		
		if(scoreOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		
		//从redis中获取产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
		//产品价格小数位数
		//Integer scale = productVo.getDecimalDigits();
		Double jumpPrice = productVo.getJumpValue();
		
		//成交记录
		Date now = new Date();
				
		//更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
		this.fillBuyScoreOrderCount(now,matchBean.getMatchPrice(), 
				matchBean.getOrderTotal(), matchBean.getMatchVol(),matchBean.getMatchTotal(), scoreOrdersInfo, jumpPrice,entrustRecord.getTradeType());
	
				
		//重新计算止盈价、止损价
		this.calculateStopLossProfitPrice(scoreOrdersInfo, productVo.getDecimalDigits());
		
		scoreOrdersInfo.setModifyDate(now);
		
		//成交记录实体组装
		OrderScoreSuccessInfo orderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), scoreOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), scoreOrdersInfo.getLastBuyDate(), now);
		boolean flag = false;
		if(scoreOrdersInfo.getBuyEntrustCount() == scoreOrdersInfo.getHoldCount()){
			//设置成交状态为完全成交
			orderScoreSuccessInfo.setSuccessStatus(2);
			flag = true;
		}else{
			//设置成交状态为部分成交
			orderScoreSuccessInfo.setSuccessStatus(1);
		}	
		
		//更新订单信息
		orderScoreInfoDao.update(scoreOrdersInfo);
		logger.info("[2004]:[外盘]更新订单信息, orderId:{}", orderId);	
		
		//新增成交记录
		orderScoreSuccessInfoDao.add(orderScoreSuccessInfo);
		logger.info("[2004]:[外盘]新增成交记录, orderId:{}", orderId);
		
		if(flag){
			//删除缓存中的买入委托单
			OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
			//订单加入到风控监控队列
			this.fillRiskControlQueue(scoreOrdersInfo);
			logger.info("[2004]:[外盘]订单加入到风控监控队列, displayId:{}", orderId);
		}
		
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(scoreOrdersInfo.getUserId(),sendOrderMsgProducer);
	}
	
	
}
