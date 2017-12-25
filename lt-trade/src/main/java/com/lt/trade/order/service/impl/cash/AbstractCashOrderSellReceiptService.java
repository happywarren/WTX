/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: AbstractCashOrderSellReceiptService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.cash;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.OrderCashEntrustChildInfoDao;
import com.lt.trade.order.dao.OrderCashEntrustInfoDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.ICashOrderBaseService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * TODO 订单卖出委托回执、成交回执处理
 * @author XieZhibing
 * @date 2016年12月14日 上午11:29:07
 * @version <b>1.0.0</b>
 */
public abstract class AbstractCashOrderSellReceiptService extends AbstractCashOrderBaseService implements ICashOrderBaseService {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 8770544145233600732L;
	
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(AbstractCashOrderSellReceiptService.class);
	@Autowired
	private IProductInfoService productInfoServiceImpl;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;
	/**
	 * 委托失败，更新失败原因
	 * 
	 * @author XieZhibing
	 * @date 2017年1月11日 下午1:33:05
	 * @see com.lt.trade.order.service.ICashOrderBaseService#doPersist2002(com.lt.model.trade.CashOrdersEntrustRecord, int)
	 * @param entrustRecord
	 * @param errorId
	 */
	@Override
	public void doPersist2002(OrderCashEntrustInfo entrustRecord, int errorId) {
		OrderCashEntrustInfo eR = entrustRecord;
		//订单显示ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2002]:平仓订单委托失败, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//现金订单基本数据实例
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单基本数据!", orderId);
			return;
		}
		OrderCashInfo cashCache = cashOrdersInfo ;
		try {
			//委托单修改时间
			Date modifyDate = new Date();
			//设置修改时间
			entrustRecord.setModifyDate(modifyDate);
			entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());
			if(errorId == 5888){//5888:由c++返回，报单失败, 未找到该证券账户(C++做的判断)				
				//保存失败卖出委托单
//				getOrderCashEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 5889){//5889:由c++返回，报单失败, 通讯异常（c++与交易所连接中断）
				//保存失败卖出委托单
//				getOrderCashEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 1111){//委托失败: errorId:1111, errorMsg:获取行情失败！
				//保存失败卖出委托单
//				getOrderCashEntrustInfoDao().add(entrustRecord);
			}else if(errorId == 5890){//超时委托失败: errorId:5890, errorMsg:超时委托失败！
				//新增委托子信息到数据库
				//不成功平仓委托记录不入库
//				getOrderCashEntrustChildInfoDao().add(entrustRecord);
			}else{//交易所返回的错误信息：由于交易所已经返回过2012（委托成功），所以此处只做更新处理（更新成失败）
				//保存失败卖出委托单
				int i = getOrderCashEntrustInfoDao().update(entrustRecord);
				logger.info("======平仓失败，更新委托单信息，更新是否成功i={}========",i);
			}
			//不成功平仓委托记录不入库
			getOrderCashEntrustChildInfoDao().add(entrustRecord);
			//删除内存的订单
			OrderCashInfoCache.remove(orderId);		
			//删除缓存中的委托单
			OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
			//移除超时处理
			TradeTimeOut.remove(orderId);
			//移除预警处理
			TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
			//预警发送 委托失败
		} catch (RuntimeException e) {
			e.printStackTrace();
			OrderCashEntrustInfoCache.put(eR);
			cashCache.setSellSuccessCount(0);
			OrderCashInfoCache.put(cashCache);
			logger.error("[2002]:平仓订单委托失败");
		}finally{
			TradeUtils.delKeyLockSell(redisTemplate, orderId);
			//向APP客户端推送交易结果
			TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(),sendOrderMsgProducer);
			//重新加入风控
		}
		
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月14日 上午11:29:07
	 * @see com.lt.trade.order.service.ICashOrderBaseService#doPersist2012(com.lt.model.trade.CashOrdersEntrustRecord)
	 * @param entrustRecord
	 */
	@Override
	public void doPersist2012(OrderCashEntrustInfo entrustRecord) {

		//订单ID
		String orderId = entrustRecord.getOrderId();
		logger.info("[2012]:收到C++交易回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
		
		//现金订单基本数据实例
		OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
		if(cashOrdersInfo == null){
			logger.error("[2012]:没有在缓存中查找到订单显示ID为:{}的外盘现金订单基本数据!", orderId);
			return;
		}
		
		//修改时间
		Date modifyDate = new Date();
				
		//更新订单主数据的用户提交卖出价、用户提交卖出时间、数据修改时间
		cashOrdersInfo.setModifyDate(modifyDate);
		logger.info("========2012==cashOrdersInfo={}=======",JSONObject.toJSONString(cashOrdersInfo));
		getOrderCashInfoDao().update(cashOrdersInfo);
	
		//保存卖出委托单
		entrustRecord.setModifyDate(modifyDate);
		getOrderCashEntrustInfoDao().add(entrustRecord);
		//保存卖出委托子单
//		getOrderCashEntrustChildInfoDao().add(entrustRecord);
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
	public void fundBalance(final OrderCashInfo cashOrdersInfo,Integer sellCount) {
			logger.info("=======================cashOrdersInfo={}=============================================",JSONObject.toJSONString(cashOrdersInfo));
			//订单显示ID
			String orderId = cashOrdersInfo.getOrderId();		
			//查询商品信息
			ProductVo productVo = productInfoServiceImpl.queryProduct(cashOrdersInfo.getProductCode());

			//持仓递延状态（1递延 0非递延） 
			Integer deferStatus = StringTools.formatInt(cashOrdersInfo.getDeferStatus(), 0);
			//每手止损保证金参数(原币种) 
			Double perSurcharge = DoubleTools.scaleFormat(cashOrdersInfo.getPerSurcharge());
			//每手止损金额(原币种)
			Double perStopLoss = DoubleTools.scaleFormat(cashOrdersInfo.getPerStopLoss());
			//每手递延保证金(原币种)
			Double perDeferFund = DoubleTools.scaleFormat(cashOrdersInfo.getPerDeferFund());
			//每手止损保证金
			double perHoldFund = DoubleTools.scaleFormat(DoubleTools.add(perSurcharge, perStopLoss)) ;

			//退回递延费用
			double deferFund2 = 0.00;
			if(DeferStatusEnum.DEFER.getValue() == deferStatus){
				deferFund2 = DoubleTools.mul(DoubleTools.mul(perDeferFund, cashOrdersInfo.getRate()),sellCount);
			}
			logger.info("deferFund2:{}",deferFund2);
			//退回保证金
			double holdFund2 = DoubleTools.mul(DoubleTools.mul(perHoldFund, cashOrdersInfo.getRate()),sellCount);
			logger.info("holdFund2:{}",holdFund2);
			//结算
			FundOrderVo fundOrderVo = new FundOrderVo(
					FundTypeEnum.CASH, cashOrdersInfo.getProductName(), orderId, cashOrdersInfo.getUserId(), 
					holdFund2, deferFund2, cashOrdersInfo.getThisLossProfit(), cashOrdersInfo.getInvestorId());		
			
			logger.info("=======FundTypeEnum.CASH={}======",FundTypeEnum.CASH);
			getFundTradeApiService().doBalance(fundOrderVo);
			logger.info("现金结算完成, displayId:{}", orderId);
	}
	
	
	/**
	 * 计算用户盈亏
	 * @param cashOrdersInfo
	 * @param sellCount
	 * @param sysBuyAvgPrice
	 * @param sysSellAvgPrice
	 */
	public void dealLossProfit(OrderCashInfo cashOrdersInfo,Integer sellCount,Integer thisCount,Double sysBuyAvgPrice, Double sysSellAvgPrice){
		logger.info("=======================cashOrdersInfo={}=============================================",JSONObject.toJSONString(cashOrdersInfo));
		//订单显示ID
		String orderId = cashOrdersInfo.getOrderId();		
		
		//每手收益:按用户提交平仓价计算
		double perBenefit = 0.00;
		//每手收益:按系统平均平仓价计算
		double perSysbenefit = 0.00;
		//查询商品信息
		ProductVo productVo = productInfoServiceImpl.queryProduct(cashOrdersInfo.getProductCode());
		
		//最小波动点
		Double jumpValue = productVo.getJumpValue();
		//最小变动价格
		Double jumpPrice = productVo.getJumpPrice();
		//订单买入交易方向: 1 多, 2 空
		Integer tradeDirection = cashOrdersInfo.getTradeDirection();
		
		//1个点位跳动
		double points = DoubleTools.div(jumpPrice, jumpValue);
		logger.info("orderId:{}, jumpValue:{}, jumpPrice:{}, tradeType:{}, points:{}", orderId, jumpValue, jumpPrice, tradeDirection, points);
		logger.info("perSysbenefit:{}, perBenefit:{}", perSysbenefit, perBenefit);
		
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
		Integer deferStatus = StringTools.formatInt(cashOrdersInfo.getDeferStatus(), 0);

		//每手止损金额(原币种)
		Double perStopLoss = cashOrdersInfo.getPerStopLoss();
		//每手止盈金额(原币种)
		Double perStopProfit = cashOrdersInfo.getPerStopProfit();
		//每手递延保证金(原币种)
		Double perDeferFund = cashOrdersInfo.getPerDeferFund();
		//每手保证金参数
		Double perSurcharge = cashOrdersInfo.getPerSurcharge();		
		//每手保证金
		Double perHoldFund = DoubleTools.add(perStopLoss, perSurcharge);
		//======判断是否盈利=======
		//递延单处理
		if(DeferStatusEnum.DEFER.getValue() == deferStatus && cashOrdersInfo.getDeferTimes() > 0){
			//穿破止损保证金 + 递延保证金
			perBenefit = (DoubleTools.add(DoubleTools.add(perSysbenefit, perHoldFund) , perDeferFund) < 0) ? -(DoubleTools.add(perHoldFund , perDeferFund)) : perSysbenefit;
		} else {//非递延单处理
			perBenefit = (DoubleTools.add(perSysbenefit , perHoldFund) < 0) ? - perHoldFund : perSysbenefit;
		}		
		//判断是否盈利
		perBenefit = perBenefit > perStopProfit ? perStopProfit : perBenefit;
		logger.info("防穿仓计算每手盈亏, displayId:{}, perSysbenefit:{}, perBenefit:{}", orderId, perSysbenefit, perBenefit);
		
		//==========================防穿仓计算结束=========================
		
//		double rmbMultiple = DoubleTools.mul(sellCount, productVo.getRate());
//		double thisRmbMultiple = DoubleTools.mul(thisCount, productVo.getRate());
		
		//用户总盈亏
		double userBenefitRate = DoubleTools.mul(perBenefit, thisCount);
		//double userBenefit= DoubleTools.mul(DoubleTools.mul(perBenefit, productVo.getRate()),sellCount);
		double thisUserBenefit = DoubleTools.mul(DoubleTools.mul(perBenefit, productVo.getRate()),thisCount);
		logger.info("平仓{}手用户总盈亏, displayId:{}, userBenefit:{}", thisCount, orderId, thisUserBenefit);
		
		//系统总盈亏
		//double sysBenefit =DoubleTools.mul(DoubleTools.mul(perSysbenefit, productVo.getRate()),sellCount);
		double thisSysBenefit = DoubleTools.mul(DoubleTools.mul(perSysbenefit, productVo.getRate()),thisCount);
		logger.info("平仓{}手系统总盈亏, displayId:{}, sysBenefit:{}", thisCount, orderId, thisSysBenefit);
		
		//结算盈亏
		cashOrdersInfo.setLossProfit(cashOrdersInfo.getLossProfit()+ thisUserBenefit);
		//系统结算收益
		cashOrdersInfo.setSysLossProfit(cashOrdersInfo.getSysLossProfit()+ thisSysBenefit);
		//结算盈亏
		cashOrdersInfo.setThisLossProfit(thisUserBenefit);
		//结算盈亏原币种
		if(cashOrdersInfo.getLossProfitRate()==null){
			cashOrdersInfo.setLossProfitRate(0.0);
		}
		logger.info("==========好坑啊orderId={},LossProfitRate={},userBenefitRate={}=======",cashOrdersInfo.getOrderId(),cashOrdersInfo.getLossProfitRate(),userBenefitRate);
		cashOrdersInfo.setLossProfitRate(cashOrdersInfo.getLossProfitRate()+userBenefitRate);
		logger.info("==========是的cashOrdersInfo.getLossProfitRate()={}============",cashOrdersInfo.getLossProfitRate());
		//系统结算收益
		cashOrdersInfo.setThisSysLossProfit(thisSysBenefit);
	}
	
	/** 
	 * 获取 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @return fundTradeApiService 
	 */
	public abstract IFundTradeApiService getFundTradeApiService();
	
	/** 
	 * 获取 订单基本信息数据接口 
	 * @return cashOrderDao 
	 */
	public abstract OrderCashInfoDao getOrderCashInfoDao();


	/** 
	 * 获取 委托单数据接口 
	 * @return cashOrderEntrustDao 
	 */
	public abstract OrderCashEntrustInfoDao getOrderCashEntrustInfoDao();
	
	/** 
	 * 获取 委托子订单单数据接口 
	 * @return cashOrderEntrustDao 
	 */
	public abstract OrderCashEntrustChildInfoDao getOrderCashEntrustChildInfoDao();

	/** 
	 * 获取 券商费用配置接口 
	 * @return investorFeeCfgApiService 
	 */
	public abstract IInvestorFeeCfgApiService getInvestorFeeCfgApiService();
	
	public static void main(String[] args) {
		double sysSellAvgPrice = 25767;
		double sysBuyAvgPrice = 25765;
		System.out.println(DoubleTools.mul(DoubleTools.sub(sysSellAvgPrice , sysBuyAvgPrice,8), 50));
		;
	}
}
