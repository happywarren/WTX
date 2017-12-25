/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderPersistServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.sms.ISmsApiService;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.score.IScoreOrderBaseService;
import com.lt.trade.order.service.score.IScoreOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;

/**
 * TODO 外盘数据持久化
 * @author XieZhibing
 * @date 2016年12月11日 下午8:51:35
 * @version <b>1.0.0</b>
 */
@Service
public class ScoreOrderOuterPlatePersistServiceImpl implements IScoreOrderPersistService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2389354929083213727L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ScoreOrderOuterPlatePersistServiceImpl.class);
	
	@Autowired
	private IScoreOrderBaseService scoreOrderOuterBuyReceiptServiceImpl;
	@Autowired
	private IScoreOrderBaseService scoreOrderOuterSellReceiptServiceImpl;
	@Autowired
	private ISmsApiService smsApiService;
	/** 
	 * 委托失败
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:51:35
	 * @see com.lt.trade.order.service.IScoreOrderPersistService#doPersist2002(com.lt.trade.tradeserver.bean.FutureErrorBean)
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
		OrderScoreEntrustInfo ScoreEntrustRecord = OrderScoreEntrustInfoCache.get(String.valueOf(entrustId));
		if(ScoreEntrustRecord == null){
			logger.error("[2002]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			return;
		}
		
		//订单显示ID
		String orderId = ScoreEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		OrderScoreInfo cache = ScoreOrdersInfo;	
		//设置失败原因		
		ScoreEntrustRecord.setErrorCode(String.valueOf(errorId));
		ScoreEntrustRecord.setErrorMsg(errorMsg);
		//委托单状态: 0.初始化; 1.成功; 2.失败
		ScoreEntrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());				
		
		Integer tradeType = ScoreEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2002]:[外盘]买入委托失败处理开始=========");
				//买入委托失败回执数据持久化
				scoreOrderOuterBuyReceiptServiceImpl.doPersist2002(ScoreEntrustRecord, errorId);
				logger.info("[2002]:[外盘]买入委托失败处理完成=========");
			//卖
			} else {
				logger.info("[2002]:[外盘]卖出委托失败处理开始=========");
				//卖出委托失败回执数据持久化
				scoreOrderOuterSellReceiptServiceImpl.doPersist2002(ScoreEntrustRecord, errorId);
				logger.info("[2002]:[外盘]卖出委托失败处理完成=========");
			}
		} catch(RuntimeException e){
			OrderScoreInfoCache.put(cache);
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, ScoreOrdersInfo.getUserId(), orderId);
			throw e;
		}
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:51:35
	 * @see com.lt.trade.order.service.IScoreOrderPersistService#doPersist2012(com.lt.trade.tradeserver.bean.FutureErrorBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2012(FutureErrorBean errorBean)throws LTException {
		//委托单编码
		int entrustId = errorBean.getPlatformId();
		//委托单
		OrderScoreEntrustInfo ScoreEntrustRecord = OrderScoreEntrustInfoCache.get(String.valueOf(entrustId));
		if(ScoreEntrustRecord == null){
			logger.error("[2012]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			return;
		}
		//委托单用户交易方向
		Integer tradeType = ScoreEntrustRecord.getTradeType();
		String orderId = ScoreEntrustRecord.getOrderId();
		//移除超时处理
		TradeTimeOut.remove(orderId);
		//买
		if(tradeType == TradeTypeEnum.BUY.getValue()){
			logger.info("[2012]:[外盘]买入委托处理开始=========");
			//买入委托回执数据持久化
			scoreOrderOuterBuyReceiptServiceImpl.doPersist2012(ScoreEntrustRecord);
			logger.info("[2012]:[外盘]买入委托处理完成=========");
		//卖
		} else {
			logger.info("[2012]:[外盘]卖出委托处理开始=========");
			//卖出委托回执数据持久化
			scoreOrderOuterSellReceiptServiceImpl.doPersist2012(ScoreEntrustRecord);
			logger.info("[2012]:[外盘]卖出委托处理完成=========");
		}
		
	}

	/**
	 * 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 下午2:17:52
	 * @see com.lt.trade.order.service.IScoreOrderPersistService#doPersist2003(com.lt.trade.tradeserver.bean.FutureMatchBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2003(FutureMatchBean matchBean)throws LTException {
		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderScoreEntrustInfo ScoreEntrustRecord = OrderScoreEntrustInfoCache.get(String.valueOf(entrustId));
		if(ScoreEntrustRecord == null){
			logger.error("[2003]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			return;
		}
		//订单显示ID
		String orderId = ScoreEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2003]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}

		//委托单用户交易方向
		Integer tradeType = ScoreEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2003]:[外盘]买入部分成交回执处理开始=========");
				//买入部分成交回执数据持久化
				scoreOrderOuterBuyReceiptServiceImpl.doPersist(matchBean,ScoreEntrustRecord);
				logger.info("[2003]:[外盘]买入部分成交回执处理完成=========");
			//卖
			} else {
				logger.info("[2003]:[外盘]卖出部分成交回执处理开始=========");
				//卖出部分成交回执数据持久化
				scoreOrderOuterSellReceiptServiceImpl.doPersist(matchBean,ScoreEntrustRecord);
				logger.info("[2003]:[外盘]卖出部分成交回执处理完成=========");
			}
		} catch(RuntimeException e){
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, ScoreOrdersInfo.getUserId(), orderId);
			throw e;
		}
		
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:18:42
	 * @see com.lt.trade.order.service.IScoreOrderPersistService#doPersist2004(com.lt.trade.tradeserver.bean.FutureMatchBean)
	 * @param matchBean
	 */
	@Override
	public void doPersist2004(FutureMatchBean matchBean) throws LTException{

		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderScoreEntrustInfo ScoreEntrustRecord = OrderScoreEntrustInfoCache.get(String.valueOf(entrustId));
		if(ScoreEntrustRecord == null){
			logger.error("[2004]:没有在缓存中查找到委托单号为{}的外盘委托单数据!", entrustId);
			return;
		}
		//订单显示ID
		String orderId = ScoreEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", ScoreOrdersInfo);
			return;
		}

		//委托单用户交易方向
		Integer tradeType = ScoreEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2004]:[外盘]买入完全成交回执处理开始=========");
				//买入完全成交回执数据持久化
				scoreOrderOuterBuyReceiptServiceImpl.doPersist2004(matchBean,ScoreEntrustRecord);
				logger.info("[2004]:[外盘]买入完全成交回执处理完成=========");	
			//卖
			} else {
				logger.info("[2004]:[外盘]卖出完全成交回执处理开始=========");
				//卖出完全成交回执数据持久化
				scoreOrderOuterSellReceiptServiceImpl.doPersist2004(matchBean,ScoreEntrustRecord);
				logger.info("[2004]:[外盘]卖出完全成交回执处理完成=========");
			}
		} catch(RuntimeException e){
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, ScoreOrdersInfo.getUserId(), orderId);
			throw e;
		}
		
	}

}
