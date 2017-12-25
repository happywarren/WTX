/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderPersistServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.cash;

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
import com.lt.trade.order.dao.OrderCashEntrustChildInfoDao;
import com.lt.trade.order.dao.OrderCashEntrustInfoDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.ICashOrderBaseService;
import com.lt.trade.order.service.ICashOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;

/**
 * TODO 外盘数据持久化
 * @author XieZhibing
 * @date 2016年12月11日 下午8:51:35
 * @version <b>1.0.0</b>
 */
@Service
public class CashOrderOuterPlatePersistServiceImpl implements ICashOrderPersistService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2389354929083213727L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(CashOrderOuterPlatePersistServiceImpl.class);
	
	@Autowired
	private ICashOrderBaseService cashOrderOuterBuyReceiptServiceImpl;
	@Autowired
	private ICashOrderBaseService cashOrderOuterSellReceiptServiceImpl;
	@Autowired
	private ISmsApiService smsApiService;
	@Autowired
	private OrderCashEntrustInfoDao orderCashEntrustInfoDao;
	@Autowired
	private OrderCashEntrustChildInfoDao orderCashEntrustChildInfoDao;
	/** 
	 * 委托失败
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:51:35
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2002(com.lt.trade.tradeserver.bean.FutureErrorBean)
	 * @param errorBean
	 */
	@Override
	public void doPersist2002(FutureErrorBean errorBean)throws LTException {
		//委托单编码
		int entrustId = errorBean.getPlatformId();
		//委托失败编码
		int errorId = errorBean.getErrorId();
		//失败原因
		String errorMsg = errorBean.getErrorMsg();
		logger.error("[2002]:[外盘]委托失败: errorId:{}, errorMsg:{}", errorId, errorMsg);
				
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2002]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			cashEntrustRecord = getOrderCashEntrustInfo(entrustId+"");
			if(cashEntrustRecord != null){
				TradeUtils.sendOrderIdErrorSMS(smsApiService, null, entrustId+"","2002",cashEntrustRecord.getTradeType());
			}
			return;
		}
		OrderCashEntrustInfo cache = cashEntrustRecord;
		//订单显示ID
		String orderId = cashEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			TradeUtils.sendOrderIdErrorSMS(smsApiService, orderId, null,"2002",cashEntrustRecord.getTradeType());
			return;
		}
		//设置失败原因		
		cashEntrustRecord.setErrorCode(String.valueOf(errorId));
		cashEntrustRecord.setErrorMsg(errorMsg);
		//委托单状态: 0.初始化; 1.成功; 2.失败
		cashEntrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());				
		
		Integer tradeType = cashEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2002]:[外盘]买入委托失败处理开始=========");
				//买入委托失败回执数据持久化
				cashOrderOuterBuyReceiptServiceImpl.doPersist2002(cashEntrustRecord, errorId);
				logger.info("[2002]:[外盘]买入委托失败处理完成=========");
			//卖
			} else {
				logger.info("[2002]:[外盘]卖出委托失败处理开始=========");
				//卖出委托失败回执数据持久化
				cashOrderOuterSellReceiptServiceImpl.doPersist2002(cashEntrustRecord, errorId);
				logger.info("[2002]:[外盘]卖出委托失败处理完成=========");
			}
		} catch(RuntimeException e){
			OrderCashEntrustInfoCache.put(cache);
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, cashOrdersInfo.getUserId(), orderId);
			throw e;
		}
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:51:35
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2012(com.lt.trade.tradeserver.bean.FutureErrorBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2012(FutureErrorBean errorBean)throws LTException {
		//委托单编码
		int entrustId = errorBean.getPlatformId();
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2012]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			return;
		}
		String orderId = cashEntrustRecord.getOrderId();
		//移除超时处理
		TradeTimeOut.remove(orderId);
		
		//委托单用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		
		//买
		if(tradeType == TradeTypeEnum.BUY.getValue()){
			logger.info("[2012]:[外盘]买入委托处理开始=========");
			//买入委托回执数据持久化
			cashOrderOuterBuyReceiptServiceImpl.doPersist2012(cashEntrustRecord);
			logger.info("[2012]:[外盘]买入委托处理完成=========");
		//卖
		} else {
			logger.info("[2012]:[外盘]卖出委托处理开始=========");
			//卖出委托回执数据持久化
			cashOrderOuterSellReceiptServiceImpl.doPersist2012(cashEntrustRecord);
			logger.info("[2012]:[外盘]卖出委托处理完成=========");
		}
		
	}

	/**
	 * 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 下午2:17:52
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2003(com.lt.trade.tradeserver.bean.FutureMatchBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean)throws LTException {
		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2003]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			cashEntrustRecord = getOrderCashEntrustInfo(entrustId+"");
			if(cashEntrustRecord != null){
				TradeUtils.sendOrderIdErrorSMS(smsApiService, null, entrustId+"","2003",cashEntrustRecord.getTradeType());
			}
			return;
		}
		//订单显示ID
		String orderId = cashEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			TradeUtils.sendOrderIdErrorSMS(smsApiService, orderId, null,"2003",cashEntrustRecord.getTradeType());
			return;
		}

		//委托单用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2003]:[外盘]买入部分成交回执处理开始=========");
				//买入部分成交回执数据持久化
//				cashOrderOuterBuyReceiptServiceImpl.doPersist2003(matchBean,cashEntrustRecord);
				cashOrderOuterBuyReceiptServiceImpl.doPersist(matchBean,cashEntrustRecord);
				logger.info("[2003]:[外盘]买入部分成交回执处理完成=========");
			//卖
			} else {
				logger.info("[2003]:[外盘]卖出部分成交回执处理开始=========");
				//卖出部分成交回执数据持久化
//				cashOrderOuterSellReceiptServiceImpl.doPersist2003(matchBean,cashEntrustRecord);
				cashOrderOuterSellReceiptServiceImpl.doPersist(matchBean,cashEntrustRecord);
				logger.info("[2003]:[外盘]卖出部分成交回执处理完成=========");
			}
		} catch(RuntimeException e){
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, cashOrdersInfo.getUserId(), orderId);
			throw e;
		}
		
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:18:42
	 * @see com.lt.trade.order.service.ICashOrderPersistService#doPersist2004(com.lt.trade.tradeserver.bean.FutureMatchBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean)throws LTException {

		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderCashEntrustInfo cashEntrustRecord = OrderCashEntrustInfoCache.get(String.valueOf(entrustId));
		if(cashEntrustRecord == null){
			logger.error("[2004]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			cashEntrustRecord = getOrderCashEntrustInfo(entrustId+"");
			if(cashEntrustRecord != null){
				TradeUtils.sendOrderIdErrorSMS(smsApiService, null, entrustId+"","2004",cashEntrustRecord.getTradeType());
			}
			return;
		}
		//订单显示ID
		String orderId = cashEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			TradeUtils.sendOrderIdErrorSMS(smsApiService, orderId, null,"2004",cashEntrustRecord.getTradeType());
			return;
		}

		//委托单用户交易方向
		Integer tradeType = cashEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2004]:[外盘]买入完全成交回执处理开始=========");
				//买入完全成交回执数据持久化
//				cashOrderOuterBuyReceiptServiceImpl.doPersist2004(matchBean,cashEntrustRecord);
				cashOrderOuterBuyReceiptServiceImpl.doPersist(matchBean,cashEntrustRecord);
				logger.info("[2004]:[外盘]买入完全成交回执处理完成=========");	
			//卖
			} else {
				logger.info("[2004]:[外盘]卖出完全成交回执处理开始=========");
				//卖出完全成交回执数据持久化
//				cashOrderOuterSellReceiptServiceImpl.doPersist2004(matchBean,cashEntrustRecord);
				cashOrderOuterSellReceiptServiceImpl.doPersist(matchBean,cashEntrustRecord);
				logger.info("[2004]:[外盘]卖出完全成交回执处理完成=========");
			}
		} catch(RuntimeException e){
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, cashOrdersInfo.getUserId(), orderId);
			throw e;
		}
		
	}

	private OrderCashEntrustInfo getOrderCashEntrustInfo (String entrustId){
		OrderCashEntrustInfo cashEntrustRecord = orderCashEntrustInfoDao.queryByEntrustCode(entrustId);
		if(!StringTools.isNotEmpty(cashEntrustRecord)){
			cashEntrustRecord =  orderCashEntrustChildInfoDao.queryByEntrustCode(entrustId);
		}
		return cashEntrustRecord;
	}
}
