/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: AbstractScoreOrderBuyReceiptService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.OrderScoreEntrustInfoDao;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.order.service.score.IScoreOrderBaseService;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.bean.FutureOrderBean;
import com.lt.trade.utils.LTConstants;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.TradeDirectVo;
/**
 * TODO 订单买入委托回执、成交回执处理
 * @author XieZhibing
 * @date 2016年12月14日 上午11:13:27
 * @version <b>1.0.0</b>
 */
public abstract class AbstractScoreOrderBuyReceiptService extends
		AbstractScoreOrderBaseService implements IScoreOrderBaseService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -3926431578655553844L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(AbstractScoreOrderBuyReceiptService.class);	
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;
	/**
	 * 更新委托失败原因，并退款
	 * 
	 * @author XieZhibing
	 * @date 2017年1月11日 上午11:59:21
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2002(com.lt.model.trade.ScoreOrdersEntrustRecord, int)
	 * @param entrustRecord
	 * @param errorId
	 */
	public void doPersist2002(OrderScoreEntrustInfo entrustRecord, int errorId){
		
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2002]:开仓订单委托失败, orderId:{}, entrustId:{}", orderId, entrustRecord.getEntrustId());
		
		//订单基本信息
		OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(scoreOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		OrderScoreInfo cache = scoreOrdersInfo;
		try {
			//删除缓存中的订单主数据实例
			OrderScoreInfoCache.remove(orderId);
			logger.info("[2002]:已删除缓存中的订单主数据");		
			//删除缓存中的委托单
			OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
			logger.info("[2002]:删除缓存中的委托单");
			//移除超时处理
			TradeTimeOut.remove(orderId);
			//移除预警处理
			TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
			
			//委托单修改时间
			Date date = new Date();
			entrustRecord.setModifyDate(date);
			
			//订单修改时间
			scoreOrdersInfo.setModifyDate(date);
			//订单失败数量
			scoreOrdersInfo.setBuyFailCount(entrustRecord.getEntrustCount());
			entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());
			if(errorId == 5888){//5888:由c++返回，下单失败, 未找到该证券账户(C++做的判断)					
				//新增订单到数据库
				getOrderScoreInfoDao().add(scoreOrdersInfo);
				//新增委托信息到数据库
				getOrderScoreEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 5889){//5889:由c++返回，报单失败, 通讯异常（c++与交易所连接中断）
				//新增订单到数据库
				getOrderScoreInfoDao().add(scoreOrdersInfo);
				//新增委托信息到数据库
				getOrderScoreEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 1111){//委托失败: errorId:1111, errorMsg:获取行情失败！
				//新增订单到数据库
				getOrderScoreInfoDao().add(scoreOrdersInfo);
				//新增委托信息到数据库
				getOrderScoreEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 5890){//超时委托失败: errorId:5890, errorMsg:超时委托失败！
				OrderScoreInfo info = getOrderScoreInfoDao().queryByOrderId(orderId);
				if(StringTools.isNotEmpty(info)){
					//修改订单信息
					getOrderScoreInfoDao().update(scoreOrdersInfo);
					//修改委托信息
					getOrderScoreEntrustInfoDao().update(entrustRecord);
				}else{
					//新增订单到数据库
					getOrderScoreInfoDao().add(scoreOrdersInfo);
					//新增委托信息到数据库
					getOrderScoreEntrustInfoDao().add(entrustRecord);
				}
			}else{//交易所返回的错误信息：由于交易所已经返回过2012（委托成功），所以此处只做更新处理（更新成失败）
				//修改订单信息
				getOrderScoreInfoDao().update(scoreOrdersInfo);
				//修改委托信息
				getOrderScoreEntrustInfoDao().update(entrustRecord);
			}
					
			
			//退款
			this.refund(scoreOrdersInfo, entrustRecord.getEntrustCount());
			logger.info("[2002]:开仓失败退款完成");
			
			//向APP客户端推送交易结果
			TradeUtils.notifyAPPClient(scoreOrdersInfo.getUserId(),sendOrderMsgProducer);
	
			
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			OrderScoreInfoCache.put(cache);
			logger.error("[2002] 事务回滚异常");
		}
		
	
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:52:10
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2012(com.lt.model.trade.ScoreOrdersInfo, com.lt.model.trade.ScoreOrdersEntrustRecord, com.lt.model.trade.ScoreOrdersCount)
	 * @param ScoreOrdersInfo
	 * @param entrustRecord
	 * @param ScoreOrdersCount
	 */
	@Override
	public void doPersist2012(OrderScoreEntrustInfo entrustRecord) {

		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2012]:收到C++交易回执, orderId:{}, entrustId:{}", orderId, entrustRecord.getEntrustId());
		
		OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(scoreOrdersInfo == null){
			logger.error("[2012]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单数据!", orderId);
			return;
		}
		//修改时间
		Date modifyDate = new Date();
		
		//保存订单主数据
		scoreOrdersInfo.setModifyDate(modifyDate);
		getOrderScoreInfoDao().add(scoreOrdersInfo);

		
		//绑定订单ID,修改时间
		entrustRecord.setOrderId(orderId);
		entrustRecord.setModifyDate(modifyDate);
		//委托实体入库
		getOrderScoreEntrustInfoDao().add(entrustRecord);
		
	}

	
	/**
	 * TODO 重新计算止盈价、止损价
	 * @author XieZhibing
	 * @date 2016年12月15日 下午7:58:14
	 * @param ScoreOrdersInfo
	 * @param scale 小数位数
	 */
	public void calculateStopLossProfitPrice(OrderScoreInfo scoreOrdersInfo, Integer scale) {		
		//查询产品信息
		ProductVo productInfo = productInfoServiceImpl.queryProduct(scoreOrdersInfo.getProductCode());
		//最小变动价格，如10 
		Double jumpPrice = productInfo.getJumpPrice();
		//最小波动点，如 0.01 
		Double jumpValue = productInfo.getJumpValue();
		
		// 计算每手的止损值
		double stopLoss = DoubleTools.div(scoreOrdersInfo.getStopLoss(), DoubleTools.mul(scoreOrdersInfo.getBuyEntrustCount() , scoreOrdersInfo.getRate()));
		// 重新计算止损价
		double stopLossPrice = TradeUtils.stopLossPrice(scoreOrdersInfo.getTradeDirection(), scoreOrdersInfo.getBuyAvgPrice(), stopLoss, jumpPrice, jumpValue);
		//小数位处理
		stopLossPrice = DoubleTools.scaleFormat(stopLossPrice, scale);
		scoreOrdersInfo.setStopLossPrice(stopLossPrice);
		
		//计算每手的止盈值
		double stopProfit = DoubleTools.div(scoreOrdersInfo.getStopProfit(), DoubleTools.mul(scoreOrdersInfo.getBuyEntrustCount() , scoreOrdersInfo.getRate()));
		//重新计算止盈价
		double stopProfitPrice = TradeUtils.stopProfitPrice(scoreOrdersInfo.getTradeDirection(), scoreOrdersInfo.getBuyAvgPrice(), stopProfit, jumpPrice, jumpValue);
		//小数位处理
		stopProfitPrice = DoubleTools.scaleFormat(stopProfitPrice, scale);
		scoreOrdersInfo.setStopProfitPrice(stopProfitPrice);
		
		logger.info("重新计算止盈价:{}, 止损价:{}", stopProfitPrice, stopLossPrice);
	}

	/**
	 * TODO 将成功的订单加入到风控监控队列
	 * @author XieZhibing
	 * @date 2016年12月19日 上午10:27:23
	 * @param ScoreOrdersInfo
	 */
	public void fillRiskControlQueue(OrderScoreInfo scoreOrdersInfo) {
		//交易方向
		TradeDirectVo tradeDirect = new TradeDirectVo(scoreOrdersInfo.getTradeDirection(),TradeTypeEnum.BUY.getValue());
		//移动止损
		boolean isMutableStopLoss = scoreOrdersInfo.getTrailStopLoss() == LTConstants.MUTABLE_STOPLOSS_OPEN;
		
		//订单显示ID
		String displayId = scoreOrdersInfo.getOrderId();
		
		//订单参数
		FutureOrderBean orderBean = new FutureOrderBean(scoreOrdersInfo.getUserId(),
				scoreOrdersInfo.getSecurityCode(), scoreOrdersInfo.getExchangeCode(),
				scoreOrdersInfo.getProductCode(), displayId,
				888888, scoreOrdersInfo.getBuyEntrustCount(), scoreOrdersInfo.getBuyAvgPrice(),
				tradeDirect.getTradeDirect(), LTConstants.TRADE_OFFSET_CLOSE,
				LTConstants.TRADE_ORDER_TYPE_MARKET, LTConstants.FUND_TYPE_SCORE,
				scoreOrdersInfo.getCreateDate().getTime());
		
		//监控对象
		RiskControlBean riskBean = new RiskControlBean(
				scoreOrdersInfo.getProductCode(), displayId,
				tradeDirect.getTradeDirect(), isMutableStopLoss,
				scoreOrdersInfo.getSysSetSellDate().getTime(), 
				scoreOrdersInfo.getBuyAvgPrice(), scoreOrdersInfo.getStopProfitPrice(), 
				scoreOrdersInfo.getStopLossPrice(), orderBean);
		
		if(PlateEnum.INNER_PLATE.getValue() == scoreOrdersInfo.getPlate()) {
			//放入监控队列
			getTradeServer().getInnerFutureTrade().fillRiskControlQueue(riskBean);
			logger.info("[内盘]将订单放入监控队列, displayId:{}", displayId);
		} else if(PlateEnum.OUTER_PLATE.getValue() == scoreOrdersInfo.getPlate()) {
			//放入监控队列
			getTradeServer().getOuterFutureTrade().fillRiskControlQueue(riskBean);
			logger.info("[外盘]将订单放入监控队列, displayId:{}", displayId);
		}
	}	
	
	/**
	 * 移除风控 中订单
	 * @param cashOrdersInfo
	 */
	public void removeRiskControlOrder(OrderScoreInfo scoreOrdersInfo){
		RiskControlBean riskBean = new RiskControlBean(
				scoreOrdersInfo.getProductCode(), scoreOrdersInfo.getOrderId(),
				-999, null,scoreOrdersInfo.getSysSetSellDate().getTime(), 
				scoreOrdersInfo.getBuyAvgPrice(), scoreOrdersInfo.getStopProfitPrice(), 
				scoreOrdersInfo.getStopLossPrice(), null);
		if(PlateEnum.INNER_PLATE.getValue() == scoreOrdersInfo.getPlate()) {
			//删除监控队列
			getTradeServer().getInnerFutureTrade().removeRiskControlOrder(riskBean);
			logger.info("[内盘]将订单移除监控队列, displayId:{}", scoreOrdersInfo.getOrderId());
		} else if(PlateEnum.OUTER_PLATE.getValue() == scoreOrdersInfo.getPlate()) {
			//删除监控队列
			getTradeServer().getOuterFutureTrade().removeRiskControlOrder(riskBean);
			logger.info("[外盘]将订单移除监控队列, displayId:{}", scoreOrdersInfo.getOrderId());
		}
		
	}
	
	
	/** 
	 * 获取 订单基本信息数据接口 
	 * @return ScoreOrderDao 
	 */
	public abstract OrderScoreInfoDao getOrderScoreInfoDao();

	/** 
	 * 获取 委托单数据接口 
	 * @return ScoreOrderEntrustDao 
	 */
	public abstract OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao();
	
	/**
	 * 
	 * TODO 获取 交易服务 
	 * @author XieZhibing
	 * @date 2016年12月19日 上午10:29:55
	 * @return
	 */
	public abstract TradeServer getTradeServer();
}
