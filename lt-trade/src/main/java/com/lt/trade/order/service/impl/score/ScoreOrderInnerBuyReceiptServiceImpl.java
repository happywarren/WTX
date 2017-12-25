/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderInnerBuyReceiptServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.fund.IFundTradeApiService;
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
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;

/**
 * TODO 内盘订单买入委托数据、回执数据处理
 * @author XieZhibing
 * @date 2016年12月14日 上午11:46:26
 * @version <b>1.0.0</b>
 */
@Service
public class ScoreOrderInnerBuyReceiptServiceImpl extends AbstractScoreOrderBuyReceiptService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 4085081336414240028L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ScoreOrderInnerBuyReceiptServiceImpl.class);

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
	
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;
	/**
	 * 获取委托数量、成交数量，可以判断是否已完全成交
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 下午2:41:43
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2003(java.lang.String, java.lang.Integer, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String, com.lt.model.trade.ScoreOrdersEntrustRecord)
	 * @param matchDateTime 成交时间
	 * @param matchVol 本次成交数量
	 * @param matchPrice 成交价
	 * @param matchTotal 已成交数量
	 * @param orderTotal 委托数量
	 * @param sysMatchId 期货公司成交ID
	 * @param entrustRecord 买入委托单
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2003]:[内盘]收到C++返回委托回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//订单对象
		OrderScoreInfo ScoreOrdersCount = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersCount == null) {
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单数据!", orderId);
			return;
		}
		//修改时间
		Date modifyDate = new Date();
		
		//更新未成功数量 = 委托数量 - 已成交数量 
		if(matchBean.getMatchTotal() <= matchBean.getOrderTotal()) {//如果已成交数量小于等于委托数量，更新未成功数量
			//未成功数量
			Integer buyFailCount = matchBean.getOrderTotal() - matchBean.getMatchTotal();
			//还未成功的数量
			ScoreOrdersCount.setBuyFailCount(buyFailCount);
		}
		ScoreOrdersCount.setModifyDate(modifyDate);
				
		//更新订单信息
		orderScoreInfoDao.update(ScoreOrdersCount);
		
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
	}

	/** 
	 * 
	 * 返回当前成交数量、成交价，可以计算成交均价
	 * @author XieZhibing
	 * @date 2016年12月14日 上午11:46:26
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2004(java.lang.String, java.lang.Integer, java.lang.Double, java.lang.Integer, java.lang.String, com.lt.model.trade.ScoreOrdersEntrustRecord)
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
		logger.info("[2004]:[内盘]收到C++返回成交回执, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//订单基本信息
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单数据!", orderId);
			return;
		}
		
		//产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(ScoreOrdersInfo.getProductCode());
		
		int jumpValue = productVo.getDecimalDigits();			
		//成交记录
		Date now = new Date();
				
		//更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息				
		this.fillBuyScoreOrderCount(now, matchBean.getMatchPrice(), matchBean.getMatchVol(),matchBean.getMatchTotal(), ScoreOrdersInfo, jumpValue);

		//重新计算止盈价、止损价
		this.calculateStopLossProfitPrice(ScoreOrdersInfo, productVo.getDecimalDigits());
		
		
		ScoreOrdersInfo.setModifyDate(now);
				
		//成交记录实体组装
		OrderScoreSuccessInfo orderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
					entrustRecord.getProductId(),entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
					entrustRecord.getProductCode(), entrustRecord.getProductName(), 
					entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
					entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 
					entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
					entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
					matchBean.getMatchVol(), ScoreOrdersInfo.getBuySuccessCount(), 
					matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), ScoreOrdersInfo.getLastBuyDate(), now);
		//设置成交状态为完全成交
		orderScoreSuccessInfo.setSuccessStatus(2);
		//更新订单信息
		orderScoreInfoDao.update(ScoreOrdersInfo);
		logger.info("[2004]:[内盘]更新订单的止损价止盈价, displayId:{}", orderId);

		//新增成交记录
		orderScoreSuccessInfoDao.add(orderScoreSuccessInfo);
		logger.info("[2004]:[内盘]新增成交记录, displayId:{}", orderId);
		
//		//退款
//		if(ScoreOrdersCount.getBuyFailCount() > 0) {
//			//更新时间
//			ScoreOrdersInfo.setModifyDate(createDate);
//			//退款
//			this.refund(ScoreOrdersInfo, ScoreOrdersCount.getBuyFailCount());
//			logger.info("[内盘]退款, buyFailCount:{}", ScoreOrdersCount.getBuyFailCount());
//		} else 
		

		//删除缓存中的买入委托单
		OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
		
		//完全成交
		if(ScoreOrdersInfo.getBuyFailCount() == 0){
			//订单加入到风控监控队列
			this.fillRiskControlQueue(ScoreOrdersInfo);
			logger.info("[2004]:[内盘]订单加入到风控监控队列, dsplayId:{}", orderId);
		}
		
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(ScoreOrdersInfo.getUserId(),sendOrderMsgProducer);
	}

	/**
	 * TODO 更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:06:16
	 * @param flag 完全成交标识：2003部分成交、2004全部成交
	 * @param matchDateTime 成交时间
	 * @param matchPrice 成交价
	 * @param matchVol 本次成交数量
	 * @param ScoreOrdersCount 持仓数量信息
	 * @param scale 小数位数
	 */
	public OrderScoreInfo fillBuyScoreOrderCount(Date lastBuyDate, Double matchPrice, 
			Integer matchVol,Integer matchTotal, OrderScoreInfo ScoreOrdersCount, int jumpValue) {
		//已买入成功手数、买入均价
		Integer buySuccCount = ScoreOrdersCount.getBuySuccessCount();
		Double sysBuyAvgPrice = ScoreOrdersCount.getBuyAvgPrice();
		
		//计算买入均价、买入成功数量
		AvgPriceVo avgPriceVo = new AvgPriceVo(buySuccCount, sysBuyAvgPrice, matchVol, matchPrice,ScoreOrdersCount.getTradeDirection(),jumpValue);
		/*//买入均价
		Double avgPrice = DoubleUtils.scaleFormat(avgPriceVo.getAvgPrice(), scale);*/
		
		//更新买入时间
		ScoreOrdersCount.setLastBuyDate(lastBuyDate);

		//更新已买入成功手数
		ScoreOrdersCount.setBuySuccessCount(avgPriceVo.getCount());
		//更新买入均价		
		ScoreOrdersCount.setBuyAvgPrice(avgPriceVo.getAvgPrice());
		//更新持仓数量
		ScoreOrdersCount.setHoldCount(ScoreOrdersCount.getBuySuccessCount());
		//处理实扣字段
		dealActualParams(ScoreOrdersCount, matchTotal);
		
		logger.info("[内盘]更新订单持仓, sysBuyAvgPrice:{}, buySuccCount:{}", avgPriceVo.getAvgPrice(), ScoreOrdersCount.getBuySuccessCount());
		return ScoreOrdersCount;
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
		double perStopLoss = DoubleTools.scaleFormat(orderScoreInfo.getPerStopLoss());		
		//单手保证金附加费
		double perSurcharge = DoubleTools.scaleFormat(orderScoreInfo.getPerSurcharge());
		
		//止损金额
		double stopLoss = DoubleTools.mul(DoubleTools.mul(perStopLoss , rate ),matchTotal );	
		//保证金参数
		double surcharge = DoubleTools.mul(DoubleTools.mul(perSurcharge ,rate  ), matchTotal);	
		//本次实扣保证金
		double actualHoldFund = DoubleTools.add(stopLoss, surcharge);	
		
		//实扣递延保证金
		double actualDeferFund=0.0;
		if(orderScoreInfo.getDeferStatus() == DeferStatusEnum.DEFER.getValue()){//如果开启递延，更新实扣递延保证金
			//每手递延保证金
			double perDeferFund = DoubleTools.scaleFormat(orderScoreInfo.getPerDeferFund());

			actualDeferFund = DoubleTools.mul(DoubleTools.mul(perDeferFund ,rate  ), matchTotal);	
			
		}
		
		//每手手续费
		double perCounterFee =DoubleTools.scaleFormat( orderScoreInfo.getPerCounterFee());		
		//实扣手续费
		double actualCounterFee = DoubleTools.mul(DoubleTools.mul(perCounterFee , rate ), matchTotal);	
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao() {
		return this.orderScoreEntrustInfoDao;
	}

	@Override
	public void doPersist(FutureMatchBean matchBean,
			OrderScoreEntrustInfo scoreEntrustRecord) {
		
	}

}
