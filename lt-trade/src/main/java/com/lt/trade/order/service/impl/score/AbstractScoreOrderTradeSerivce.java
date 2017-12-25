/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: AbstractscoreOrderTrade.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.SellTriggerTypeEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.OrderScoreEntrustInfoDao;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.riskcontrol.bean.MutableStopLossBean;
import com.lt.trade.tradeserver.bean.FutureOrderBean;
import com.lt.trade.utils.LTConstants;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.OrderVo;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年12月13日 上午9:14:43
 * @version <b>1.0.0</b>
 */
public abstract class AbstractScoreOrderTradeSerivce implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 4418807558601779288L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(AbstractScoreOrderTradeSerivce.class);	
	
	@Autowired
	private TradeServer tradeService;
	@Autowired
	private IProductInfoService productInfoServiceImpl;
		
	/**
	 * 将委托单放入交易队列
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:29:13
	 * @param scoreOrder
	 * @param entrustRecord
	 * @param futureOrder
	 */
	public void sendBuyEntrustMessage(OrderScoreInfo scoreOrder, OrderScoreEntrustInfo entrustRecord) {
		
		//委托单转换为委托协议entrustRecord
		FutureOrderBean futureOrder = new FutureOrderBean(scoreOrder.getUserId(),
				entrustRecord.getSecurityCode(), entrustRecord.getExchangeCode(),
				entrustRecord.getProductCode(), entrustRecord.getOrderId(), 
				Integer.valueOf(entrustRecord.getEntrustId()), entrustRecord.getEntrustCount(), 
				entrustRecord.getLimitedPriceValue(), entrustRecord.getActTradeDirection(), 
				entrustRecord.getActTradeType(), entrustRecord.getEntrustType(),
				LTConstants.FUND_TYPE_SCORE, System.currentTimeMillis());
		try {
			//内盘开仓
			if(scoreOrder.getPlate() == PlateEnum.INNER_PLATE.getValue()){
				//普通用户报单
				getTradeServer().getInnerFutureTrade().produceNormalFutureOrders(futureOrder);
				
				logger.info("内盘开仓委托请求已发出, entrustId:{}, productCode:{}, exchangeCode:{}",
					entrustRecord.getEntrustId(), entrustRecord.getProductCode(), entrustRecord.getExchangeCode());
			}
			//外盘开仓
			else if(scoreOrder.getPlate() == PlateEnum.OUTER_PLATE.getValue()){
				//普通用户报单
				getTradeServer().getOuterFutureTrade().produceNormalFutureOrders(futureOrder);
				
				logger.info("外盘开仓委托请求已发出, entrustId:{}, productCode:{}, exchangeCode:{}",
						entrustRecord.getEntrustId(), entrustRecord.getProductCode(), entrustRecord.getExchangeCode());
			}
			
			//设置委托时间
			Date now = new Date();
			scoreOrder.setEntrustBuyDate(now);
			entrustRecord.setEntrustDate(now);
			//设置委托状态
			entrustRecord.setEntrustStatus(EntrustStatusEnum.SUCCESS.getValue());	
			//TODO
			//加入超时队列
			TradeTimeOut.put(scoreOrder.getOrderId(), System.currentTimeMillis());
			//加入订单与委托号关联关系队列
			//TradeTimeOut.entrustMapPut(scoreOrder.getOrderId(), entrustRecord.getEntrustId());
			TradeTimeOut.entrustMapPut(scoreOrder.getOrderId(), entrustRecord.getEntrustId(),entrustRecord.getEntrustType());
		} catch (Exception e) {
			//退款并持久化失败订单
			this.refund(scoreOrder, entrustRecord);
			logger.info("开仓委托请求发送失败, entrustId:{}", entrustRecord.getEntrustId());
			throw new LTException(LTResponseCode.TD00007);
		}
	}
	/**
	 * 
	 * TODO 平仓发送委托消息到C++
	 * @author XieZhibing
	 * @date 2017年1月6日 下午3:05:46
	 * @param scoreOrder
	 * @param entrustRecord
	 */
	public void sendSellEntrustMessage(OrderScoreInfo scoreOrder, OrderScoreEntrustInfo entrustRecord) {
		
		//委托单转换为委托协议entrustRecord
		FutureOrderBean futureOrder = new FutureOrderBean(scoreOrder.getUserId(),
				entrustRecord.getSecurityCode(), entrustRecord.getExchangeCode(),
				entrustRecord.getProductCode(), entrustRecord.getOrderId(), 
				Integer.valueOf(entrustRecord.getEntrustId()), entrustRecord.getEntrustCount(), 
				entrustRecord.getLimitedPriceValue(), entrustRecord.getActTradeDirection(), 
				entrustRecord.getActTradeType(), entrustRecord.getEntrustType(),
				LTConstants.FUND_TYPE_SCORE,System.currentTimeMillis());

		try {

			//内盘平仓
			if(scoreOrder.getPlate() == PlateEnum.INNER_PLATE.getValue()){
				//用户平仓、管理员触发		
				if (Integer.valueOf(scoreOrder.getSellTriggerType()).equals(SellTriggerTypeEnum.CUSTOMER.getValue()) ||
						Integer.valueOf(scoreOrder.getSellTriggerType()).equals(SellTriggerTypeEnum.ADMINISTRATOR.getValue())) {
					getTradeServer().getInnerFutureTrade().produceNormalFutureOrders(futureOrder);
				} else {//风控平仓、系统清仓、策略平仓
					getTradeServer().getInnerFutureTrade().produceSpecialFutureOrders(futureOrder);					
				}
				
				logger.info("内盘平仓委托请求已发出, entrustId:{}, productCode:{}, exchangeCode:{}",
						 entrustRecord.getEntrustId(), entrustRecord.getProductCode(), entrustRecord.getExchangeCode());
			}
			//外盘平仓
			else if(scoreOrder.getPlate() == PlateEnum.OUTER_PLATE.getValue()){
				//用户平仓、管理员触发
				if (Integer.valueOf(scoreOrder.getSellTriggerType()).equals(SellTriggerTypeEnum.CUSTOMER.getValue()) ||
						Integer.valueOf(scoreOrder.getSellTriggerType()).equals(SellTriggerTypeEnum.ADMINISTRATOR.getValue())) {
					getTradeServer().getOuterFutureTrade().produceNormalFutureOrders(futureOrder);
					logger.info("===========用户平仓=============");
				} else {//风控平仓、系统清仓、策略平仓
					getTradeServer().getOuterFutureTrade().produceSpecialFutureOrders(futureOrder);					
				}
				
				logger.info("外盘平仓委托请求已发出, entrustId:{}, productCode:{}, exchangeCode:{}",
						entrustRecord.getEntrustId(), entrustRecord.getProductCode(), entrustRecord.getExchangeCode());
			}
			
			//设置委托时间
			Date now = new Date();
			scoreOrder.setEntrustSellDate(now);
			entrustRecord.setEntrustDate(now);
			//设置委托状态
			entrustRecord.setEntrustStatus(EntrustStatusEnum.SUCCESS.getValue());	
			//TODO 是否要区分开仓委托 平仓委托，开仓委托号与平仓委托号不同
			//加入超时队列
			TradeTimeOut.put(scoreOrder.getOrderId(), System.currentTimeMillis());
			//加入订单与委托号关联关系队列
			//TradeTimeOut.entrustMapPut(scoreOrder.getOrderId(), entrustRecord.getEntrustId());
			TradeTimeOut.entrustMapPut(scoreOrder.getOrderId(), entrustRecord.getEntrustId(),entrustRecord.getEntrustType());
		} catch (Exception e) {
			entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());//失败
			entrustRecord.setEntrustDate(new Date());
			entrustRecord.setErrorCode("3002");
			entrustRecord.setErrorMsg("将委托单放到交易队列中异常");
			this.getOrderScoreEntrustInfoDao().add(entrustRecord);
			
			//删除内存的订单
			OrderScoreInfoCache.remove(entrustRecord.getOrderId());
			OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
			
			logger.info("平仓发送委托消息到C++失败, entrustId:{}", entrustRecord.getEntrustId());
			throw new LTException(LTResponseCode.TD00007);
		}
	}
	/**
	 * TODO 退款并持久化失败订单
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:24:34
	 * @param scoreOrder
	 * @param entrustRecord
	 */
	private void refund(OrderScoreInfo scoreOrder, OrderScoreEntrustInfo entrustRecord) {
		try {
			//订单ID
			String orderId = scoreOrder.getOrderId();			
			//失败手数
			Integer failCount = scoreOrder.getBuyEntrustCount();
			//汇率
			Double rate = scoreOrder.getRate();
			
			//退回递延费用
			double deferFund = 0.00;			
			//递延状态
			if(DeferStatusEnum.DEFER.getValue() == scoreOrder.getDeferStatus()){				
				deferFund = DoubleTools.mul(scoreOrder.getPerDeferFund(), DoubleTools.mul(failCount , rate));
			}
			//每手止损保证金 = 每手止损金额 + 每手止损保证金参数
			double perHoldFund = DoubleTools.add(scoreOrder.getPerStopLoss() , scoreOrder.getPerSurcharge());
			// 退回保证金
			double holdFund = DoubleTools.mul(perHoldFund, DoubleTools.mul(failCount , rate));
			// 退回手续费
			double actualCounterFee = DoubleTools.mul(scoreOrder.getPerCounterFee(), DoubleTools.mul(failCount , rate));
			
			// 初始化订单资金对象
			FundOrderVo fundOrderVo = new FundOrderVo(
					FundTypeEnum.SCORE, scoreOrder.getProductName(),
					orderId, scoreOrder.getUserId(),
					holdFund, deferFund, actualCounterFee);
			//退款
			getFundTradeApiService().doRefund(fundOrderVo);
			
			/*********************持久化订单数据**************************/
			Date now = new Date();
			
			//持久化失败的委托单数据
			entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());//失败
			entrustRecord.setEntrustDate(now);
			entrustRecord.setErrorCode("3002");
			entrustRecord.setErrorMsg("将委托单放到交易队列中异常");
			this.getOrderScoreEntrustInfoDao().add(entrustRecord);
			//委托单号
			String entrustId = entrustRecord.getEntrustId();
			logger.info("持久化失败的委托单数据, entrustId:{}", entrustId);
			
			
			//持久化失败的订单数据
			scoreOrder.setBuyFailCount(failCount);
			scoreOrder.setEntrustBuyDate(now);
			this.getOrderScoreInfoDao().add(scoreOrder);
			logger.info("持久化失败的订单数据, orderId:{}", orderId);
			
			//删除缓存
			OrderScoreInfoCache.remove(orderId);
			OrderScoreEntrustInfoCache.remove(entrustId);
			//移除超时处理
			TradeTimeOut.remove(orderId);
			//移除预警处理
			TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
			logger.info("删除缓存中的订单和委托单, orderId:{}, entrustId:{}", orderId, entrustId);
		} catch (Exception e) {
			//退款失败
			//可以在此处做个报警
			throw new LTException(LTResponseCode.FU00006);
		}
	}

	/**
	 * 处理订单的移动止损金额、移动止损价
	 * @param cashOrdersInfo
	 * @param product
	 */
	public void dealMoveStopLoss(OrderScoreInfo scoreOrdersInfo,OrderVo orderVo){
		if(scoreOrdersInfo.getTrailStopLoss() != 1){//未开启移动止损
			return;
		}
		//产品
		ProductVo product = productInfoServiceImpl.queryProduct(scoreOrdersInfo.getProductCode());
		//移动止损最高价
		Double sentinelPrice = orderVo.getSentinelPrice();
		
		MutableStopLossBean mbean = null;	
		if(sentinelPrice == null){
			if(PlateEnum.INNER_PLATE.getValue() == scoreOrdersInfo.getPlate()){
			    mbean = tradeService.getInnerFutureTrade().getMutableStopLossOrder(scoreOrdersInfo.getProductCode(), scoreOrdersInfo.getOrderId());
				
			} else if(PlateEnum.OUTER_PLATE.getValue() ==  scoreOrdersInfo.getPlate()){
				mbean = tradeService.getOuterFutureTrade().getMutableStopLossOrder(scoreOrdersInfo.getProductCode(), scoreOrdersInfo.getOrderId());
			}
			
			logger.info("======================mbean={}===============",JSONObject.toJSONString(mbean));
			if(mbean == null){
				return;
			}
			sentinelPrice = mbean.getSentinelPrice();	
		}
			
		//移动止损价格点位
		Double dw = DoubleUtils.div(DoubleUtils.mul(scoreOrdersInfo.getPerStopLoss(),product.getJumpValue()),product.getJumpPrice());
		//移动止损价
		Double moveStopLossPrice = DoubleUtils.mul(DoubleUtils.sub(sentinelPrice,dw),scoreOrdersInfo.getHoldCount());
		double price  = DoubleTools.div(product.getJumpPrice(),product.getJumpValue()); 
		//移动止损金额
		Double moveStopLoss = null;
		if(scoreOrdersInfo.getTradeDirection() == TradeDirectionEnum.DIRECTION_UP.getValue()){//看多
			moveStopLoss = DoubleUtils.mul(DoubleUtils.sub(DoubleUtils.sub(sentinelPrice,dw),scoreOrdersInfo.getBuyAvgPrice()),price);
			
			//计算移动止损价格
			Double dd = DoubleUtils.mul(DoubleUtils.div(moveStopLoss, product.getJumpPrice()),product.getJumpValue());
			moveStopLossPrice = DoubleTools.mul(scoreOrdersInfo.getBuyAvgPrice()+dd, 1,8);
			moveStopLoss = DoubleUtils.mul(moveStopLoss,scoreOrdersInfo.getRate());
			moveStopLoss = DoubleTools.mul(moveStopLoss,scoreOrdersInfo.getHoldCount());
		
		}else{//看空
			moveStopLoss = DoubleUtils.mul(DoubleUtils.sub(DoubleUtils.sub(scoreOrdersInfo.getBuyAvgPrice(),sentinelPrice),dw),price);
			
			//计算移动止损价格
			Double dd = DoubleUtils.mul(DoubleUtils.div(moveStopLoss, product.getJumpPrice()),product.getJumpValue());
			moveStopLossPrice = DoubleTools.mul(scoreOrdersInfo.getBuyAvgPrice()-dd, 1,8);
			moveStopLoss = DoubleUtils.mul(moveStopLoss,scoreOrdersInfo.getRate());
			moveStopLoss = DoubleTools.mul(moveStopLoss,scoreOrdersInfo.getHoldCount());
		}

		logger.info("=========坑爹啊=orderId={},moveStopLossPrice={}========",scoreOrdersInfo.getOrderId(),moveStopLossPrice);
		scoreOrdersInfo.setMoveStopLossPrice(moveStopLossPrice);
		scoreOrdersInfo.setMoveStopLoss(moveStopLoss);
	}
	
	/** 
	 * 获取 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @return fundTradeApiService 
	 */
	public abstract IFundTradeApiService getFundTradeApiService();
	
	/**
	 * 
	 * TODO 获取交易服务
	 * @author XieZhibing
	 * @date 2016年12月13日 上午9:21:02
	 * @return
	 */
	public abstract TradeServer getTradeServer();
	/**
	 * 
	 * TODO 订单基本信息数据接口
	 * @author XieZhibing
	 * @date 2016年12月29日 下午2:30:23
	 * @return
	 */
	public abstract OrderScoreInfoDao getOrderScoreInfoDao() ;
	
	/**
	 * 
	 * TODO 委托单数据接口 
	 * @author XieZhibing
	 * @date 2016年12月29日 下午2:30:34
	 * @return
	 */
	public abstract OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao();
	
}
