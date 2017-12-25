/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderInnerPlatePersistServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.cash;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.sms.ISmsApiService;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.ICashOrderBaseService;
import com.lt.trade.order.service.ICashOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DateTools;

/**
 * TODO 外盘数据持久化
 * @author XieZhibing
 * @date 2016年12月12日 上午10:06:11
 * @version <b>1.0.0</b>
 */
@Service
public class CashOrderInnerPlatePersistServiceImpl implements ICashOrderPersistService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2995596976094627206L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(CashOrderInnerPlatePersistServiceImpl.class);
	
	@Autowired
	private ICashOrderBaseService cashOrderInnerBuyReceiptServiceImpl;
	@Autowired
	private ICashOrderBaseService cashOrderInnerSellReceiptServiceImpl;
	@Autowired
	private ISmsApiService smsApiService;
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:06:11
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2002(com.lt.trade.tradeserver.bean.FutureErrorBean)
	 * @param errorBean
	 */
	@Override
	public void doPersist2002(FutureErrorBean errorBean) throws RuntimeException{
		// TODO Auto-generated method stub
		//委托单编码
		int entrustId = errorBean.getPlatformId();
		//委托失败编码
		int errorId = errorBean.getErrorId();
		//失败原因
		String errorMsg = errorBean.getErrorMsg();
		logger.error("[2002]:[内盘]委托失败: errorId:{}, errorMsg:{}", errorId, errorMsg);
		
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2002]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			return;
		}
		OrderCashEntrustInfo  cache= cashEntrustRecord;
		//订单显示ID
		String orderId = cashEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单数据!", orderId);
			return;
		}
		
		//设置失败原因	
		cashEntrustRecord.setErrorCode(String.valueOf(errorId));
		cashEntrustRecord.setErrorMsg(errorMsg);

		//委托单状态: 0.初始化; 1.成功; 2.失败
		cashEntrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());		
		
		//用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		try{
			//买多、买空
			if(tradeType == TradeTypeEnum.BUY.getValue()) {
				logger.info("[2002]:[内盘]买入委托失败处理开始=========");
				//买入委托失败回执数据持久化
				cashOrderInnerBuyReceiptServiceImpl.doPersist2002(cashEntrustRecord, errorId);
				logger.info("[2002]:[内盘]买入委托失败处理完成=========");
			} 
			//卖多、卖空
			else{
				logger.info("[2002]:[内盘]卖出委托失败处理开始=========");
				//委托失败回执数据持久化
				cashOrderInnerSellReceiptServiceImpl.doPersist2002(cashEntrustRecord, errorId);
				logger.info("[2002]:[内盘]卖出委托失败处理完成=========");
			}
		} catch (RuntimeException e) {
			OrderCashEntrustInfoCache.put(cache);
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, cashOrdersInfo.getUserId(), orderId);
			throw e;
		}
	}
	
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:06:11
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2012(com.lt.trade.tradeserver.bean.FutureErrorBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2012(FutureErrorBean errorBean) {
		//委托单编码
		int entrustId = errorBean.getPlatformId();
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2012]:没有在缓存中查找到委托单号为{}的内盘委托单数据!", entrustId);
			return;
		}
		String orderId = cashEntrustRecord.getOrderId();
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		//买多、买空
		if(tradeType == TradeTypeEnum.BUY.getValue() ) {
			logger.info("[2012]:[内盘]买入委托处理开始=========");
			//买入委托回执数据持久化
			cashOrderInnerBuyReceiptServiceImpl.doPersist2012(cashEntrustRecord);
			logger.info("[2012]:[内盘]买入委托处理完成=========");
		} 
		//卖多、卖空
		else {
			logger.info("[2012]:[内盘]卖出委托处理开始=========");
			//卖出委托回执数据持久化
			cashOrderInnerSellReceiptServiceImpl.doPersist2012(cashEntrustRecord);
			logger.info("[2012]:[内盘]卖出委托处理完成=========");
		}
		
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:06:11
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2003(com.lt.trade.tradeserver.bean.FutureMatchBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean) {
		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2003]:没有在缓存中查找到委托单号为{}的内盘委托单数据!", entrustId);
			return;
		}
				
		//成交日期
		String matchDateTime = matchBean.getMatchDateTime();
		//内盘成交日期格式转换
		matchDateTime = DateTools.parse(matchDateTime, "yyyyMMdd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
		matchBean.setMatchDateTime(matchDateTime);
		
		//用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		//买多、买空		
		if(tradeType == TradeTypeEnum.BUY.getValue()) {
			logger.info("[2003]:[内盘]买入成交回执处理开始=========");			
			//成交结果数据持久化
			cashOrderInnerBuyReceiptServiceImpl.doPersist2003(matchBean, cashEntrustRecord);
			logger.info("[2003]:[内盘]买入成交回执处理完成=========");
		} 
		//卖多、卖空
		else{
			logger.info("[2003]:[内盘]卖出成交回执处理开始=========");
			//成交结果数据持久化
			cashOrderInnerSellReceiptServiceImpl.doPersist2003(matchBean, cashEntrustRecord);
			logger.info("[2003]:[内盘]卖出成交回执处理完成=========");
		}
		
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:06:11
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2004(com.lt.trade.tradeserver.bean.FutureMatchBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean) {
		// TODO Auto-generated method stub
		logger.info("matchBean:{}", JSONObject.toJSON(matchBean));
		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2004]:没有在缓存中查找到委托单号为{}的内盘委托单数据!", entrustId);
			return;
		}
		//订单显示ID
		String orderId = cashEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的内盘现金订单数据!", orderId);
			return;
		}
		
		//用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		
		try {
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()) {			
				logger.info("[2004]:[内盘]买入成交回执处理开始=========");			
				//成交结果数据持久化
				cashOrderInnerBuyReceiptServiceImpl.doPersist(matchBean, cashEntrustRecord);
//				cashOrderInnerBuyReceiptServiceImpl.doPersist2004(matchBean, cashEntrustRecord);
				logger.info("[2004]:[内盘]买入成交回执处理完成=========");
			} 
			//卖
			else{
				logger.info("[2004]:[内盘]卖出成交回执处理开始=========");
				//成交结果数据持久化
//				cashOrderInnerSellReceiptServiceImpl.doPersist2004(matchBean,cashEntrustRecord);
				cashOrderInnerSellReceiptServiceImpl.doPersist(matchBean,cashEntrustRecord);
				logger.info("[2004]:[内盘]卖出成交回执处理完成=========");
			}
		} catch (RuntimeException e) {
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, cashOrdersInfo.getUserId(), orderId);
			throw e;
		}		
	}
}
