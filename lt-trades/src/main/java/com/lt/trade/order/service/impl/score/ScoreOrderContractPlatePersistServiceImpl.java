/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderPersistServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import com.lt.api.sms.ISmsApiService;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.service.score.IScoreOrderBaseService;
import com.lt.trade.order.service.score.IScoreOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 差价合约积分数据持久化
 * @author yanzhenyu
 */
@Service
public class ScoreOrderContractPlatePersistServiceImpl implements IScoreOrderPersistService {

	private static final long serialVersionUID = -2389354929083213727L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ScoreOrderContractPlatePersistServiceImpl.class);

	@Autowired
	private IScoreOrderBaseService scoreOrderContractBuyReceiptServiceImpl;
	@Autowired
	private IScoreOrderBaseService scoreOrderContractSellReceiptServiceImpl;
	@Autowired
	private ISmsApiService smsApiService;

	/**
	 * 委托失败
	 */
	@Override
	public void doPersist2002(FutureErrorBean errorBean)throws LTException {
		//委托单编码
		int entrustId = errorBean.getPlatformId();
		//委托失败编码
		int errorId = errorBean.getErrorId();
		//失败原因
		String errorMsg = errorBean.getErrorMsg();
		logger.error("[2002]:[差价合约积分]委托失败: errorId:{}, errorMsg:{}", errorId, errorMsg);

		//委托单
		OrderScoreEntrustInfo scoreEntrustRecord = OrderScoreEntrustInfoCache.get(String.valueOf(entrustId));
		if(scoreEntrustRecord == null){
			logger.error("[2002]:没有在缓存中查找到委托单号为{}的差价合约积分委托单数据!", entrustId);
			return;
		}

		//订单显示ID
		String orderId = scoreEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(scoreOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的差价合约积分订单数据!", orderId);
			return;
		}
		OrderScoreInfo cache = scoreOrdersInfo;
		//设置失败原因
		scoreEntrustRecord.setErrorCode(String.valueOf(errorId));
		scoreEntrustRecord.setErrorMsg(errorMsg);
		//委托单状态: 0.初始化; 1.成功; 2.失败
		scoreEntrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());

		Integer tradeType = scoreEntrustRecord.getTradeType();

		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2002]:[差价合约积分]买入委托失败处理开始=========");
				//买入委托失败回执数据持久化
				scoreOrderContractBuyReceiptServiceImpl.doPersist2002(scoreEntrustRecord, errorId);
				logger.info("[2002]:[差价合约积分]买入委托失败处理完成=========");
			//卖
			} else {
				logger.info("[2002]:[差价合约积分]卖出委托失败处理开始=========");
				//卖出委托失败回执数据持久化
				scoreOrderContractSellReceiptServiceImpl.doPersist2002(scoreEntrustRecord, errorId);
				logger.info("[2002]:[差价合约积分]卖出委托失败处理完成=========");
			}
		} catch(RuntimeException e){
			OrderScoreInfoCache.put(cache);
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, scoreOrdersInfo.getUserId(), orderId);
			throw e;
		}
	}

	@Override
	public void doPersist2012(FutureErrorBean errorBean)throws LTException {
	}

	@Override
	public void doPersist2003(FutureMatchBean matchBean)throws LTException {
	}
	@Override
	public void doPersist2004(FutureMatchBean matchBean) throws LTException{
		//委托单编码
		int entrustId = matchBean.getPlatformId();
		//委托单
		OrderScoreEntrustInfo scoreEntrustRecord = OrderScoreEntrustInfoCache.get(String.valueOf(entrustId));
		if(scoreEntrustRecord == null){
			logger.error("[2004]:没有在缓存中查找到委托单号为{}的差价合约积分委托单数据!", entrustId);
			return;
		}
		//订单显示ID
		String orderId = scoreEntrustRecord.getOrderId();
		//订单基本信息数据
		OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(scoreOrdersInfo == null){
			logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的差价合约积分订单数据!");
			return;
		}

		//委托单用户交易方向
		Integer tradeType = scoreEntrustRecord.getTradeType();
		
		try{
			//买
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				logger.info("[2004]:[差价合约积分]买入完全成交回执处理开始=========");
				//买入完全成交回执数据持久化
				scoreOrderContractBuyReceiptServiceImpl.doPersist2004(matchBean,scoreEntrustRecord);
				logger.info("[2004]:[差价合约积分]买入完全成交回执处理完成=========");
			//卖
			} else {
				logger.info("[2004]:[差价合约积分]卖出完全成交回执处理开始=========");
				//卖出完全成交回执数据持久化
				scoreOrderContractSellReceiptServiceImpl.doPersist2004(matchBean,scoreEntrustRecord);
				logger.info("[2004]:[差价合约积分]卖出完全成交回执处理完成=========");
			}
		} catch(RuntimeException e){
			//发送短信
			TradeUtils.sendBalanceErrorSMS(smsApiService, scoreOrdersInfo.getUserId(), orderId);
			throw e;
		}
		
	}

}
