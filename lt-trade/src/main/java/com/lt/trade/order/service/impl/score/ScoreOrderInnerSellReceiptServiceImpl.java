/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderInnerSellReceiptServiceImpl.java
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
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.trade.OrderScoreSuccessInfo;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.OrderScoreEntrustInfoDao;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.dao.OrderScoreSuccessInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;

/**
 * TODO 卖出订单数据持久化处理
 * @author XieZhibing
 * @date 2016年12月14日 下午4:02:59
 * @version <b>1.0.0</b>
 */
@Service
public class ScoreOrderInnerSellReceiptServiceImpl extends AbstractScoreOrderSellReceiptService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ScoreOrderInnerSellReceiptServiceImpl.class);
	
	/** 订单基本信息数据接口 */
	@Autowired
	private OrderScoreInfoDao orderScoreInfoDao;
	/** 委托单数据接口 */
	@Autowired
	private OrderScoreEntrustInfoDao orderScoreEntrustInfoDao;
	/** 成交结果数据接口 */
	@Autowired
	private OrderScoreSuccessInfoDao orderScoreSuccessInfoDao;
	/** 资金分布式接口: 资金扣款、结算、退款业务接口 */
	@Autowired
	private IFundTradeApiService fundTradeApiService;
	/** 券商费用配置接口 */
	@Autowired
	private IInvestorFeeCfgApiService investorFeeCfgApiService;
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 下午4:04:22
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2003(java.lang.String, java.lang.Integer, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String, com.lt.model.trade.ScoreOrdersEntrustRecord)
	 * @param matchDateTime
	 * @param matchVol
	 * @param matchPrice
	 * @param matchTotal
	 * @param orderTotal
	 * @param sysMatchId
	 * @param entrustRecord
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean,OrderScoreEntrustInfo entrustRecord) {

		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2003]:[内盘]收到C++返回委托回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//订单持仓数量对象
		OrderScoreInfo orderScoreInfo = OrderScoreInfoCache.get(orderId);
		if(orderScoreInfo == null) {
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单持仓数量数据!", orderId);
			return;
		}
		//修改时间
		Date modifyDate = new Date();
		entrustRecord.setModifyDate(modifyDate);
		orderScoreInfo.setModifyDate(modifyDate);
		
		//更新平仓委托数量
		if(matchBean.getMatchTotal() <= matchBean.getOrderTotal()) {
			//卖委托数量
			orderScoreInfo.setSellEntrustCount(orderScoreInfo.getSellEntrustCount()-matchBean.getMatchTotal());
			//更新持仓数量
			orderScoreInfo.setHoldCount(orderScoreInfo.getHoldCount()-matchBean.getMatchTotal());
		}
		
		//更新订单
		orderScoreInfoDao.update(orderScoreInfo);	
		logger.info("[2003]:[内盘]更新持仓数量, displayId:{}", orderId);
		//更新 交易所返回的委托单号
		orderScoreEntrustInfoDao.update(entrustRecord);	
		logger.info("[2003]:[内盘]更新委托单, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
	}
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 下午4:04:22
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2004(java.lang.String, java.lang.Integer, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String, com.lt.model.trade.ScoreOrdersEntrustRecord)
	 * @param matchDateTime
	 * @param matchVol
	 * @param matchPrice
	 * @param matchTotal
	 * @param orderTotal
	 * @param sysMatchId
	 * @param entrustRecord
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2004]:[内盘]收到C++返回成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//现金订单基本数据实例
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单基本数据!", orderId);
			return;
		}
		
		
		//产品
		ProductVo productVo = productInfoServiceImpl.queryProduct(ScoreOrdersInfo.getProductCode());
		//产品价格小数位数
		//Integer scale = productVo.getDecimalDigits();
		
		int jumpValue = productVo.getDecimalDigits();
		
		//修改时间
		Date now = new Date();
				
		//更新订单持仓数量对象的卖出成功数量、卖出均价、持仓数量等信息
		this.fillSellScoreOrderCount(now, matchBean.getMatchPrice(), matchBean.getMatchVol(), ScoreOrdersInfo, jumpValue);
		
		
		
		OrderScoreSuccessInfo orderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
				entrustRecord.getProductId(), entrustRecord.getInvestorId(),entrustRecord.getAccountId(),
				entrustRecord.getProductCode(), entrustRecord.getProductName(), 
				entrustRecord.getExchangeCode(), entrustRecord.getPlate(), 
				entrustRecord.getSecurityCode(), entrustRecord.getTradeType(), 				
				entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(), 
				entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(), 
				matchBean.getMatchVol(), ScoreOrdersInfo.getSellSuccessCount(), 
				matchBean.getSysMatchId(),matchBean.getSysOrderId(), matchBean.getMatchPrice(), ScoreOrdersInfo.getLastSellDate(), now);
		
		//设置成交状态为完全成交
		orderScoreSuccessInfo.setSuccessStatus(2);
		//更新订单基本信息数据
		ScoreOrdersInfo.setModifyDate(now);
		
		//买入均价
		Double sysBuyAvgPrice = ScoreOrdersInfo.getBuyAvgPrice();
		//卖出均价
		Double sysSellAvgPrice = ScoreOrdersInfo.getSellAvgPrice();
		
		this.dealLossProfit(ScoreOrdersInfo, matchBean.getMatchTotal(),matchBean.getMatchVol(), sysBuyAvgPrice, sysSellAvgPrice);
		//更新结算日期、结算盈亏、系统结算盈亏
		orderScoreInfoDao.update(ScoreOrdersInfo);
		
		//新增成交记录
		orderScoreSuccessInfoDao.add(orderScoreSuccessInfo);
		logger.info("[2004]:[内盘]新增成交记录, displayId:{}", orderId);
		//资金结算
		this.fundBalance(ScoreOrdersInfo, matchBean.getMatchVol());
		//删除缓存中的委托单
		OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());

		//删除缓存中的订单基本信息数据
		OrderScoreInfoCache.remove(orderId);
		//向APP客户端推送交易结果
		TradeUtils.notifyAPPClient(ScoreOrdersInfo.getUserId(),sendOrderMsgProducer);
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//移除预警处理
		TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
	}
	
	/**
	 * TODO 更新订单持仓数量对象的卖出成功数量、卖出均价、卖出时间、持仓数量等信息
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:06:16
	 * @param matchDateTime 成交时间
	 * @param matchPrice 成交价
	 * @param matchVol 本次成交数量
	 * @param ScoreOrdersCount 订单持仓数量信息
	 * @param scale	产品价格小数位数
	 */
	private OrderScoreInfo fillSellScoreOrderCount(Date lastBuyDate, Double matchPrice,
			Integer matchVol, OrderScoreInfo orderScoreInfo, int jumpValue) {
		//已卖出成功手数、卖出均价
		Integer sellSuccCount = orderScoreInfo.getSellSuccessCount();
		Double sysSellAvgPrice = orderScoreInfo.getSellAvgPrice();
		
		//计算卖出均价、卖出成功数量
		AvgPriceVo avgPriceVo = new AvgPriceVo(sellSuccCount, sysSellAvgPrice, matchVol, matchPrice,orderScoreInfo.getTradeDirection(),jumpValue);
		//卖出均价
		//Double avgPrice = DoubleTools.scaleFormat(avgPriceVo.getAvgPrice(), scale);
		
		//已卖出数量
		sellSuccCount = avgPriceVo.getCount();
		
		//更新卖出时间
		orderScoreInfo.setLastSellDate(lastBuyDate);

		//更新已卖出成功手数
		orderScoreInfo.setSellSuccessCount(sellSuccCount);
		//更新卖出均价		
		orderScoreInfo.setSellAvgPrice(avgPriceVo.getAvgPrice());
		
		logger.info("[内盘]更新订单持仓, sysSellAvgPrice:{}, sellSuccCount:{}", avgPriceVo.getAvgPrice(), sellSuccCount);
		return orderScoreInfo;
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
	 * 获取 券商费用配置接口 
	 * @return investorFeeCfgApiService 
	 */
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
	public OrderScoreInfoDao getOrderScoreInfoDao() {
		return this.orderScoreInfoDao;
	}
	@Override
	public OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao() {
		return this.orderScoreEntrustInfoDao;
	}
	@Override
	public void doPersist(FutureMatchBean matchBean,
			OrderScoreEntrustInfo scoreEntrustRecord) {
		// TODO Auto-generated method stub
		
	}
	
}
