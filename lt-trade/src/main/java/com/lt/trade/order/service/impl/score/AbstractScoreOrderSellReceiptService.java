/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: AbstractScoreOrderSellReceiptService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.score;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.OrderScoreEntrustInfoDao;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.order.service.score.IScoreOrderBaseService;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;

/**
 * TODO 订单卖出委托回执、成交回执处理
 * @author XieZhibing
 * @date 2016年12月14日 上午11:29:07
 * @version <b>1.0.0</b>
 */
public abstract class AbstractScoreOrderSellReceiptService extends AbstractScoreOrderBaseService implements IScoreOrderBaseService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 8770544145233600732L;
	
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(AbstractScoreOrderSellReceiptService.class);
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;
	/**
	 * 委托失败，更新失败原因
	 * 
	 * @author XieZhibing
	 * @date 2017年1月11日 下午1:33:05
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2002(com.lt.model.trade.ScoreOrdersEntrustRecord, int)
	 * @param entrustRecord
	 * @param errorId
	 */
	@Override
	public void doPersist2002(OrderScoreEntrustInfo entrustRecord, int errorId) {
		OrderScoreEntrustInfo eR =  entrustRecord;
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2002]:平仓订单委托失败, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//现金订单基本数据实例
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单基本数据!", orderId);
			return;
		}
		OrderScoreInfo cache =  ScoreOrdersInfo;
		try {
			//委托单修改时间
			Date modifyDate = new Date();
			//设置修改时间
			entrustRecord.setModifyDate(modifyDate);
			entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());
			if(errorId == 5888){//5888:由c++返回，报单失败, 未找到该证券账户(C++做的判断)				
				//保存失败卖出委托单
				getOrderScoreEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 5889){//5889:由c++返回，报单失败, 通讯异常（c++与交易所连接中断）
				//保存失败卖出委托单
				getOrderScoreEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 1111){//委托失败: errorId:1111, errorMsg:获取行情失败！
				//保存失败卖出委托单
				getOrderScoreEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 5890){//超时委托失败: errorId:5890, errorMsg:超时委托失败！
				OrderScoreEntrustInfo info = getOrderScoreEntrustInfoDao().queryByEntrustCode(entrustRecord.getEntrustId());
				if(StringTools.isNotEmpty(info)){
					//修改委托信息
					getOrderScoreEntrustInfoDao().update(entrustRecord);
				}else{
					//新增委托信息到数据库
					getOrderScoreEntrustInfoDao().add(entrustRecord);
				}
			}else{//交易所返回的错误信息：由于交易所已经返回过2012（委托成功），所以此处只做更新处理（更新成失败）
				//保存失败卖出委托单
				int i = getOrderScoreEntrustInfoDao().update(entrustRecord);
				logger.info("======平仓失败，更新委托单信息，更新是否成功i={}========",i);
			}
			
			//删除内存的订单
			OrderScoreInfoCache.remove(orderId);		
			//删除缓存中的委托单
			OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
			//向APP客户端推送交易结果
			TradeUtils.notifyAPPClient( ScoreOrdersInfo.getUserId(),sendOrderMsgProducer);
	
		} catch (RuntimeException e) {
			e.printStackTrace();
			cache.setSellSuccessCount(0);
			OrderScoreEntrustInfoCache.put(eR);
			OrderScoreInfoCache.put(cache);
			logger.error("[2012]委托失败，更新失败原因异常");
		}
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 上午11:29:07
	 * @see com.lt.trade.order.service.IScoreOrderBaseService#doPersist2012(com.lt.model.trade.ScoreOrdersEntrustRecord)
	 * @param entrustRecord
	 */
	@Override
	public void doPersist2012(OrderScoreEntrustInfo entrustRecord) {

		//订单ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2012]:收到C++交易回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//现金订单基本数据实例
		OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
		if(ScoreOrdersInfo == null){
			logger.error("[2012]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单基本数据!", orderId);
			return;
		}
		
		//修改时间
		Date modifyDate = new Date();
				
		//更新订单主数据的用户提交卖出价、用户提交卖出时间、数据修改时间
		ScoreOrdersInfo.setModifyDate(modifyDate);
		getOrderScoreInfoDao().update(ScoreOrdersInfo);
	
		//保存卖出委托单
		entrustRecord.setModifyDate(modifyDate);
		getOrderScoreEntrustInfoDao().add(entrustRecord);

	}
	
	
	/**
	 * 
	 * TODO 资金结算
	 * @author XieZhibing
	 * @date 2017年1月11日 下午5:00:18
	 * @param cashOrdersInfo
	 * @param sellCount 本次成交数量
	 * @param sysBuyAvgPrice 买入均价
	 * @param sysSellAvgPrice 卖出均价
	 */
	public void fundBalance(OrderScoreInfo scoreOrdersInfo,Integer sellCount) {
		logger.info("=======================scoreOrdersInfo={}=============================================",JSONObject.toJSONString(scoreOrdersInfo));
		//订单显示ID
		String orderId = scoreOrdersInfo.getOrderId();		
		//查询商品信息
		ProductVo productVo = productInfoServiceImpl.queryProduct(scoreOrdersInfo.getProductCode());

		//持仓递延状态（1递延 0非递延） 
		Integer deferStatus = StringTools.formatInt(scoreOrdersInfo.getDeferStatus(), 0);
		//每手止损保证金参数(原币种) 
		Double perSurcharge = scoreOrdersInfo.getPerSurcharge();
		//每手止损金额(原币种)
		Double perStopLoss = scoreOrdersInfo.getPerStopLoss();
		//每手递延保证金(原币种)
		Double perDeferFund = scoreOrdersInfo.getPerDeferFund();
		//每手止损保证金
		double perHoldFund = DoubleTools.add(perSurcharge , perStopLoss);

		
		//退回递延费用
		double deferFund2 = 0.00;
		if(DeferStatusEnum.DEFER.getValue() == deferStatus){
			deferFund2 = DoubleTools.mul(DoubleTools.mul(perDeferFund, scoreOrdersInfo.getRate()),sellCount);
		}
		//退回保证金
		double holdFund2 = DoubleTools.mul(DoubleTools.mul(perHoldFund, scoreOrdersInfo.getRate()),sellCount);
		//结算
		FundOrderVo fundOrderVo = new FundOrderVo(
				FundTypeEnum.SCORE, scoreOrdersInfo.getProductName(), orderId, scoreOrdersInfo.getUserId(), 
				holdFund2, deferFund2, scoreOrdersInfo.getThisLossProfit(), scoreOrdersInfo.getInvestorId());	
		
		getFundTradeApiService().doBalance(fundOrderVo);
		logger.info("积分结算完成, displayId:{}", orderId);
		
	}
	
	/**
	 * 计算用户盈亏
	 * @param cashOrdersInfo
	 * @param sellCount
	 * @param sysBuyAvgPrice
	 * @param sysSellAvgPrice
	 */
	public void dealLossProfit(OrderScoreInfo scoreOrdersInfo,Integer sellCount,Integer thisCount,Double sysBuyAvgPrice, Double sysSellAvgPrice){
		logger.info("=======================cashOrdersInfo={}=============================================",JSONObject.toJSONString(scoreOrdersInfo));
		//订单显示ID
		String orderId = scoreOrdersInfo.getOrderId();		
		
		//每手收益:按用户提交平仓价计算
		double perBenefit = 0.00;
		//每手收益:按系统平均平仓价计算
		double perSysbenefit = 0.00;
		//查询商品信息
		ProductVo productVo = productInfoServiceImpl.queryProduct(scoreOrdersInfo.getProductCode());
		
		//最小波动点
		Double jumpValue = productVo.getJumpValue();
		//最小变动价格
		Double jumpPrice = productVo.getJumpPrice();
		//订单买入交易方向: 1 多, 2 空
		Integer tradeDirection = scoreOrdersInfo.getTradeDirection();
		
		//点位
		double points = DoubleTools.div(jumpPrice, jumpValue);
		logger.info("orderId:{}, jumpValue:{}, jumpPrice:{}, tradeType:{}, points:{}", orderId, jumpValue, jumpPrice, tradeDirection, points);
		
		//=========================盈亏计算==============================//
		//计算收益
		if(TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection){//多
			perSysbenefit = DoubleUtils.mul(DoubleUtils.sub(sysSellAvgPrice , sysBuyAvgPrice), points);			
		} else if(TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection){
			perSysbenefit = DoubleUtils.mul(DoubleUtils.sub(sysBuyAvgPrice , sysSellAvgPrice), points);			
		}
		//用户盈亏
		perBenefit = perSysbenefit;
		logger.info("开平仓均价计算每手盈亏, displayId:{}, sysSellAvgPrice:{}, sysBuyAvgPrice:{}, perSysbenefit:{}, perBenefit:{}", orderId, sysSellAvgPrice, sysBuyAvgPrice, perSysbenefit, perBenefit);
		
		//==========================防穿仓计算开始=========================
		//持仓递延状态（1递延 0非递延） 
		Integer deferStatus = StringTools.formatInt(scoreOrdersInfo.getDeferStatus(), 0);

		//每手止损金额(原币种)
		Double perStopLoss = scoreOrdersInfo.getPerStopLoss();
		//每手止盈金额(原币种)
		Double perStopProfit = scoreOrdersInfo.getPerStopProfit();
		//每手递延保证金(原币种)
		Double perDeferFund = scoreOrdersInfo.getPerDeferFund();
		//每手保证金参数
		Double perSurcharge = scoreOrdersInfo.getPerSurcharge();		
		//每手保证金
		Double perHoldFund = DoubleTools.add(perStopLoss, perSurcharge);
		//======判断是否盈利=======
		//递延单处理
		if(DeferStatusEnum.DEFER.getValue() == deferStatus ){
			//穿破止损保证金 + 递延保证金
			perBenefit = (DoubleTools.add(DoubleTools.add(perSysbenefit , perHoldFund) , perDeferFund) < 0) ? -(DoubleTools.add(perStopLoss , perDeferFund)) : perSysbenefit;
		} else {//非递延单处理
			perBenefit = (DoubleTools.add(perSysbenefit , perHoldFund) < 0) ? - perHoldFund : perSysbenefit;
		}		
		//判断是否盈利
		perBenefit = perBenefit > perStopProfit ? perStopProfit : perBenefit;
		logger.info("防穿仓计算每手盈亏, displayId:{}, perSysbenefit:{}, perBenefit:{}", orderId, perSysbenefit, perBenefit);
		
		//==========================防穿仓计算结束=========================
		

		//用户总盈亏
		double userBenefitRate = DoubleTools.mul(perBenefit, sellCount);
		double userBenefit = DoubleTools.mul(DoubleTools.mul(perBenefit, productVo.getRate()),sellCount);
		double thisUserBenefit = DoubleTools.mul(DoubleTools.mul(perBenefit, productVo.getRate()),thisCount);
		logger.info("平仓{}手用户总盈亏, displayId:{}, userBenefit:{}", sellCount, orderId, userBenefit);
		
		//系统总盈亏
		double sysBenefit =DoubleTools.mul(DoubleTools.mul(perSysbenefit, productVo.getRate()),sellCount);
		double thisSysBenefit = DoubleTools.mul(DoubleTools.mul(perSysbenefit, productVo.getRate()),thisCount);
		logger.info("平仓{}手系统总盈亏, displayId:{}, sysBenefit:{}", sellCount, orderId, sysBenefit);
		
		//结算盈亏
		scoreOrdersInfo.setLossProfit(userBenefit);
		//系统结算收益
		scoreOrdersInfo.setSysLossProfit(sysBenefit);
		//结算盈亏
		scoreOrdersInfo.setThisLossProfit(thisUserBenefit);
		//结算盈亏原币种
		scoreOrdersInfo.setLossProfitRate(userBenefitRate);
		//系统结算收益
		scoreOrdersInfo.setThisSysLossProfit(thisSysBenefit);
	}
	
	/** 
	 * 获取 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @return fundTradeApiService 
	 */
	public abstract IFundTradeApiService getFundTradeApiService();
	
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
	 * 获取 券商费用配置接口 
	 * @return investorFeeCfgApiService 
	 */
	public abstract IInvestorFeeCfgApiService getInvestorFeeCfgApiService();
}
