package com.lt.trade.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.business.product.IProductApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.trade.TradeServer;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.service.IOrderFunctionService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.vo.product.ProductVo;
import com.lt.vo.product.TimeConfigVO;
import com.lt.vo.product.TimeConfigVO.SysSaleTime;
import com.lt.vo.trade.TradeDirectVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
* 项目名称：lt-trade
* 类名称：CashFunctionServiceImpl
* 类描述：
* 创建人：yuanxin
* 创建时间：2016年12月15日 下午8:27:46
*/
@Service
public class OrderFunctionServiceImpl implements IOrderFunctionService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderCashInfoDao cashOrderDao;
	@Autowired
	private TradeServer tradeService;
	@Autowired
	private IProductApiService productApiService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private IProductInfoService productInfoServiceImpl;

	@Override
	public OrderCashInfo getCashOrderInfoById(String orderId) throws LTException {
		try{
			return cashOrderDao.queryById(orderId);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}


	@Override
	public int checkIfDeforLossAndProfit(InvestorFeeCfg investorFeeCfg, OrderCashInfo cashOrdersInfo, String deferStatu, String stopLoss,
										 String stopfit, String trailStopLoss, Integer fundType, OrderLossProfitDefLog log) throws LTException {
		String type = "";
		double count = cashOrdersInfo.getHoldCount();
		double rate = cashOrdersInfo.getRate();

		Double oldStopProfit = cashOrdersInfo.getStopProfit();
		Double oldStopLoss = cashOrdersInfo.getStopLoss();
		Integer oldDeferStatus = cashOrdersInfo.getDeferStatus();
		Integer oldTrailStopLoss = cashOrdersInfo.getTrailStopLoss();

		StringBuilder operateLog = new StringBuilder("");
		try{
			logger.debug("进入到判断操作方法");
			if(!stopfit.equals("") && DoubleTools.mul(DoubleTools.mul(Double.valueOf(stopfit), rate, 8) , count)  != oldStopProfit.doubleValue()){
				type += "1";
				operateLog.append(UserContant.ORDER_UPDATE_PROFIT_LOG.replace("older", oldStopProfit.toString())
						.replace("new", String.valueOf(DoubleTools.mul(DoubleTools.mul(Double.valueOf(stopfit) , count ), rate))));
			}

			if(!stopLoss.equals("") && DoubleTools.mul(DoubleTools.mul(Double.valueOf(stopLoss), rate, 8) , count)  != oldStopLoss.doubleValue()){
				type += "2";
				operateLog.append(UserContant.ORDER_UPDATE_LOSS_LOG.replace("older", oldStopLoss.toString())
						.replace("new", String.valueOf(DoubleTools.mul(DoubleTools.mul(Double.valueOf(stopLoss) , count), rate))));
			}

			logger.debug("deferStatu:{},cashOrdersInfo:{}",deferStatu,oldDeferStatus);
			if(!deferStatu.equals("") && Integer.parseInt(deferStatu) != oldDeferStatus.intValue()){
				type += "3";
				operateLog.append(Integer.parseInt(deferStatu) == 1 ? UserContant.ORDER_OPEN_DEFER_LOG : UserContant.ORDER_CLOSE_DEFER_LOG);
			}

			if(!trailStopLoss.equals("") && Integer.parseInt(trailStopLoss) != oldTrailStopLoss.intValue()){
				type += "4";
				operateLog.append(Integer.parseInt(trailStopLoss) == 1 ? UserContant.ORDER_OPEN_MOVELOSS_LOG : UserContant.ORDER_CLOSE_MOVELOSS_LOG);
			}

			log.setContent(operateLog.toString());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00004);
		}
		return type.equals("")?0:Integer.parseInt(type);
	}

	@Override
	public int updateCashOrderInfo(OrderCashInfo cashOrdersInfo) throws LTException{
		try{
			return cashOrderDao.update(cashOrdersInfo);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00003);
		}

	}

	@Override
	public int updateCashOrderInfo(OrderCashInfo cashOrdersInfo,Double hf) throws LTException{
		try{
			//目前线上出现的问题都是保证金差100左右，所以这里做个差值，如果差1的则把订单的信息替换下，把小数点的对不上账问题忽略掉
			Double cz = DoubleTools.sub(cashOrdersInfo.getActualHoldFund(),Math.abs(hf));
			if(Math.abs(cz) > 1){//订单信息和资金的重制
				logger.info("===========修改止盈止损时，发现订单的保证金和资金明细的保证金不一致，orderId={},订单保证金={},资金冻结保证金={}=============",
						cashOrdersInfo.getOrderId(),cashOrdersInfo.getActualHoldFund(),hf);
				cashOrdersInfo.setActualHoldFund(hf);
				Double stopLoss = DoubleTools.sub(hf, DoubleTools.mul(cashOrdersInfo.getPerSurcharge(), cashOrdersInfo.getBuySuccessCount()));
				cashOrdersInfo.setStopLoss(stopLoss);

				//重新计算止损价
				//查询产品信息
				ProductVo productInfo = productInfoServiceImpl.queryProduct(cashOrdersInfo.getProductCode());
				//最小变动价格，如10
				Double jumpPrice = productInfo.getJumpPrice();
				//最小波动点，如 0.01
				Double jumpValue = productInfo.getJumpValue();
				// 重新计算止损价
				double stopLossPrice = TradeUtils.stopLossPrice(cashOrdersInfo.getTradeDirection(), cashOrdersInfo.getBuyAvgPrice(), stopLoss, jumpPrice, jumpValue);
				//小数位处理
				stopLossPrice = DoubleTools.scaleFormat(stopLossPrice, productInfo.getDecimalDigits());
				cashOrdersInfo.setStopLossPrice(stopLossPrice);
				return cashOrderDao.update(cashOrdersInfo);
			}
			return 1;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00003);
		}

	}


	public Date getDefSysSetSaleTime(List<SysSaleTime> list, Date sysSetSaleTime, int operate) throws LTException {
		if(CollectionUtils.isNotEmpty(list)){
			boolean isOpen = operate==1?true:false;
			try{
				logger.debug("开始处理修改递延时间：{},修改递延操作：{},系统原清仓时间为：{}",list.size(),isOpen,sysSetSaleTime);
				if(list.size() == 1){
					// 一天的清仓时间点只有一个情况下，开启/关闭递延都是跨越一天
					return DateTools.add(sysSetSaleTime, isOpen?1:-1);
				}else{
					if(list.size() == 2){
						String time1 = list.get(0).getBeginTime();
						String time2 = list.get(1).getBeginTime();
						String sysSetSaleStr = DateTools.parseToDefaultTimeString(sysSetSaleTime);

						// 获取最大时间 和最小清仓时间
						String maxTime = Integer.parseInt(time1.trim().split(":")[0]) > Integer.parseInt(time2.trim().split(":")[0]) ?time1:time2;
						String minTime = Integer.parseInt(time1.trim().split(":")[0]) > Integer.parseInt(time2.trim().split(":")[0]) ?time2:time1;
						//判断是否匹配最大时间
						boolean isBig = sysSetSaleStr.equals(maxTime) ;
						logger.debug("系统清仓时间串为：{}，系统清仓时间1为：{},系统清仓时间2：{}", sysSetSaleStr,maxTime,minTime);

						if(isOpen){
							if(isBig){
								 return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 DateTools.add(sysSetSaleTime, 1)) +" "+ minTime);
							}else{
								return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 sysSetSaleTime) +" "+ maxTime);
							}
						}else{
							if(isBig){
								 return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 sysSetSaleTime) +" "+ minTime);
							}else{
								return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										DateTools.add(sysSetSaleTime, -1)) +" "+ maxTime);
							}
						}
					}else{
						int size = list.size();
						String saleTimeStr = DateTools.parseToDefaultTimeString(sysSetSaleTime) ;
						int sysSetSaleHour = Integer.parseInt(saleTimeStr.split(":")[0]);
						String usefulTime = ""; // 最接近清仓时间的值
						String maxTime = "";  // 配置中最大的时间值
						String minTime = ""; // 配置中最小的时间值
						String timeConfig = "";  // 当前遍历的时间值
						for(int i=0;i<size;i++){
							timeConfig = list.get(i).getBeginTime();
							// 获取最接近系统清仓时间的时间值（开递延取接近的大值，关递延取接近的小值，结果为空的话时间为最大或最小值）
							if(!usefulTime.equals("")){
								if(isOpen){
									if(Integer.parseInt(usefulTime.split(":")[0]) > Integer.parseInt(timeConfig.split(":")[0]) &&
											Integer.parseInt(timeConfig.split(":")[0]) > sysSetSaleHour){
										usefulTime = timeConfig;
									}
								}else{
									if(Integer.parseInt(timeConfig.split(":")[0]) < Integer.parseInt(usefulTime.split(":")[0]) &&
											Integer.parseInt(usefulTime.split(":")[0]) < sysSetSaleHour){
										usefulTime = timeConfig;
									}
								}
							}else{
								if(isOpen){
									if(Integer.parseInt(timeConfig.split(":")[0]) > sysSetSaleHour){
										usefulTime = timeConfig;
									}
								}else{
									if(Integer.parseInt(timeConfig.split(":")[0]) < sysSetSaleHour){
										usefulTime = timeConfig;
									}
								}
							}

							if(i == 0){
								maxTime = timeConfig;
								minTime = timeConfig;
							}else{
								if(Integer.parseInt(timeConfig.split(":")[0]) > Integer.parseInt(maxTime.split(":")[0])){
									maxTime = timeConfig;
								}

								if(Integer.parseInt(timeConfig.split(":")[0]) < Integer.parseInt(minTime.split(":")[0])){
									minTime = timeConfig;
								}
							}
						}

						logger.debug("系统清仓时间串为：{}，最晚清仓时间为：{},最早清仓时间点为：{},最接近清仓时间点为：{}", saleTimeStr,maxTime,minTime,usefulTime);
						if(isOpen){
							if(usefulTime.trim().equals("")){
								return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 DateTools.add(sysSetSaleTime, 1)) +" "+ minTime);
							}else{
								return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 sysSetSaleTime) +" "+ usefulTime);
							}
						}else{
							if(usefulTime.trim().equals("")){
								return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 DateTools.add(sysSetSaleTime, -1)) +" "+ maxTime);
							}else{
								return DateTools.toDefaultDateTime(DateTools.parseToDefaultDateString(
										 sysSetSaleTime) +" "+ usefulTime);
							}
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				throw new LTException(LTResponseCode.US00004);
			}
		}else{
			throw new LTException(LTResponseCode.US03101);
		}
	}

	@Override
	public void setCashOrderStopProfit(Double stopProfit,OrderCashInfo newOrder, OrderCashInfo oldOrder, InvestorFeeCfg feeCfg,ProductVo productVo)
			throws LTException {
		logger.info("止盈处理");
		logger.info("止盈处理,StopProfitRange={},stopProfit={}",feeCfg.getStopLossRange(),stopProfit);
		int rangLen = feeCfg.getStopProfitRange().split(",").length;
		newOrder.setStopProfit(stopProfit * oldOrder.getRate() * oldOrder.getHoldCount());
		String rangeMax = feeCfg.getStopProfitRange().split(",")[rangLen -1];
		String rangeMin = feeCfg.getStopProfitRange().split(",")[0];

		logger.info("止盈区间设置判断");
		if(rangeMax != null && !rangeMax.equals("") && rangeMin != null && !rangeMin.equals("")){
			if(Double.valueOf(rangeMax) < stopProfit ||  Double.valueOf(rangeMin) > stopProfit){
				throw new LTException(LTResponseCode.TDY0002);
			}
		}else{
			throw new LTException(LTResponseCode.TDY0002);
		}

		newOrder.setPerStopProfit(stopProfit);
//		newOrder.setStopProfitPrice(DoubleUtils.scaleFormat(TradeUtils.stopProfitPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopProfit, productVo.getJumpPrice(), productVo.getJumpValue()), productVo.getDecimalDigits()));
		newOrder.setStopProfitPrice(TradeUtils.stopProfitPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopProfit, productVo.getJumpPrice(), productVo.getJumpValue()));
	}

	@Override
	public void setCashOrderStopLoss(Double stopLoss, OrderCashInfo newOrder, OrderCashInfo oldOrder,
			InvestorFeeCfg feeCfg, ProductVo productVo,Integer defstatu,Double mainCash,Double floatAmt) throws LTException {
		int rangLen = feeCfg.getStopLossRange().split(",").length;
		newOrder.setActualHoldFund(DoubleTools.mul(DoubleTools.mul(DoubleTools.add(stopLoss  , feeCfg.getSurcharge()) , oldOrder.getRate()),  oldOrder.getHoldCount()));
		newOrder.setStopLoss(DoubleTools.mul(DoubleTools.mul(stopLoss , oldOrder.getRate())  , oldOrder.getHoldCount()));
		String rangeMax = feeCfg.getStopLossRange().split(",")[rangLen -1];
		String rangeMin = feeCfg.getStopLossRange().split(",")[0];

		logger.info("止损区间设置判断max:{},min:{},stopLoss:{},floatAmt:{}",rangeMax,rangeMin,stopLoss,floatAmt);
		if(rangeMax != null && !rangeMax.equals("") && rangeMin != null && !rangeMin.equals("")){
			// 新的止损值不能大于最大范围， 不能小于浮动止盈止损绝对值 与最小止损 值的和
			if(Double.valueOf(rangeMax) < stopLoss ||  DoubleTools.add(Double.valueOf(rangeMin) , Math.abs(floatAmt)) > stopLoss){
				throw new LTException("您当前可设置的最低止损金额为"+DoubleTools.mul(DoubleTools.add(Double.valueOf(rangeMin) , Math.abs(floatAmt)), oldOrder.getHoldCount())+",请重新设置");
			}
		}else{
			throw new LTException(LTResponseCode.TDY0003);
		}

		newOrder.setPerStopLoss(stopLoss);
		newOrder.setPerSurcharge(feeCfg.getSurcharge());
//		newOrder.setStopLossPrice(DoubleUtils.scaleFormat(TradeUtils.stopLossPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopLoss, productVo.getJumpPrice(), productVo.getJumpValue()), productVo.getDecimalDigits()));
		newOrder.setStopLossPrice(TradeUtils.stopLossPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopLoss, productVo.getJumpPrice(), productVo.getJumpValue()));
		if(defstatu !=null ){
			if(defstatu.equals("1")){
//				if(mainCash < newOrder.getActualHoldFund() - oldOrder.getActualHoldFund() + feeCfg.getDeferFund() * oldOrder.getRate()  * oldOrder.getHoldCount()){
				double cash = DoubleTools.add(DoubleTools.sub(newOrder.getActualHoldFund(), oldOrder.getActualHoldFund()),DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate())  , oldOrder.getHoldCount()));
				if(mainCash < cash){
					throw new LTException(LTResponseCode.FUY00001);
				}
			}else{
				logger.info("mainCash:{},newOrder:{},olderOrder:{}",JSONObject.toJSONString(mainCash),JSONObject.toJSONString(newOrder),JSONObject.toJSONString(oldOrder));
//				if(mainCash < newOrder.getActualHoldFund() - oldOrder.getActualHoldFund() - oldOrder.getPerDeferFund() * oldOrder.getRate()  * oldOrder.getHoldCount()){
				double cash = DoubleTools.sub(DoubleTools.sub(newOrder.getActualHoldFund(), oldOrder.getActualHoldFund()),DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate())  , oldOrder.getHoldCount()));
				if(mainCash <cash){
					throw new LTException(LTResponseCode.FUY00001);
				}
			}
		}else{
//			if(mainCash < newOrder.getActualHoldFund() - oldOrder.getActualHoldFund()){
			double cash = DoubleTools.sub(newOrder.getActualHoldFund(), oldOrder.getActualHoldFund());
			if(mainCash < cash){
				throw new LTException(LTResponseCode.FUY00001);
			}
		}
	}


	/**
	 * 检查是否达到新的止损价
	 * @param newStopLoss
	 * @return
	 */
	public boolean checkAchieveStopLoss(Integer plate,Integer direction,String produceCode,Double buyPrice, Double newStopLoss,Double jumpPrice,Double jumpValue,Double minStopLoss){
		//获取当前价
		ProductPriceBean bean = null;
		if(Objects.equals(PlateEnum.INNER_PLATE.getValue(), plate)){
		    bean = tradeService.getInnerFutureTrade().getQuotePrice(produceCode);
		} else if(Objects.equals(PlateEnum.OUTER_PLATE.getValue(), plate)){
			bean = tradeService.getOuterFutureTrade().getQuotePrice(produceCode);
		} else if (Objects.equals(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue(), plate)){
			bean = tradeService.getContractTrade().getQuotePrice(produceCode);
		}

		Double currentPrice = null;
		if (TradeDirectionEnum.DIRECTION_UP.getValue() == direction) {
			currentPrice = bean==null?0.0: bean.getAskPrice();
		} else {
			currentPrice = bean==null?0.0: bean.getBidPrice();
		}

		//Double currentPrice = 8495.0;
		logger.info("获取当前价currentPrice = "+currentPrice);
		Double subValue = DoubleTools.sub(currentPrice, buyPrice);//当前价-买入价 计算差额

		if(TradeDirectionEnum.DIRECTION_UP.getValue() == direction){//看多
			if(subValue >= 0){
				return false;
			}else{
				subValue = DoubleTools.mul(subValue, -1);//转换成整数
			}
		}else{//看空
			if(subValue <= 0){
				return false;
			}
		}

		Double divValue =  DoubleTools.div(subValue, jumpValue);//计算波动点数
		Double lossIncome = DoubleTools.mul(divValue, jumpPrice);//负收益
		logger.info("=====收益===lossIncome={}",lossIncome);
		//如果负收益加上最小止损大于等于新的止损则不允许修改
		if(DoubleTools.add(lossIncome, minStopLoss) < newStopLoss){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void setCashOrderDef(Integer defstatu, OrderCashInfo newOrder, OrderCashInfo oldOrder,
			InvestorFeeCfg feeCfg, ProductVo productVo,Double mainCash) throws LTException {
		BoundHashOperations<String, String, TimeConfigVO>  saleTime_redis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
		List<SysSaleTime> time_list = saleTime_redis.get(oldOrder.getProductCode()).getSysSaleTimeList();
		logger.debug("获取时间配置信息：{}",JSONObject.toJSONString(time_list));

		newOrder.setDeferStatus(defstatu);
		Date date = getDefSysSetSaleTime(time_list, oldOrder.getSysSetSellDate(), defstatu);
		if(defstatu == 1){
			newOrder.setDeferStatus(defstatu);
			// 时间是否通过校验
			try{
				if(productApiService.isContinueThreeDayHoliday(date,oldOrder.getExchangeCode())){
					throw new LTException(LTResponseCode.TDY0001);
				}else{

					Date expiration = DateTools.toDefaultDateTime(productVo.getExpirationTime());
					if(DateTools.compareTo(new Date(), DateTools.add(expiration, -3))){
						throw new LTException(LTResponseCode.TDY0005);
					}
					double stopLoss = 0.0;
					if(newOrder.getStopLoss() !=null ){
						stopLoss = DoubleTools.sub(oldOrder.getStopLoss() , newOrder.getStopLoss());
					}
					if(DoubleTools.add(mainCash , stopLoss) < DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate()),  oldOrder.getHoldCount())){
						throw new LTException(LTResponseCode.FUY00001);
					}

					newOrder.setPerDeferFund(feeCfg.getDeferFund());
					newOrder.setPerDeferInterest(feeCfg.getDeferFee());
					newOrder.setActualDeferFund(DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate())  , oldOrder.getHoldCount()));
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new LTException(e.getMessage());
			}

		}else{
			newOrder.setDeferStatus(DeferStatusEnum.NOT_DEFER.getValue());
			newOrder.setActualDeferFund(0.0);
		}

		newOrder.setSysSetSellDate(date);
	}


	@Override
	public void setCashRiskBeanTrailLoss(RiskControlBean riskBean, OrderCashInfo orderInfo,String opera,Integer trailStopLoss) throws LTException {
		riskBean.setProductName(orderInfo.getProductCode());
        riskBean.setDeferredOrderTimeStamp(orderInfo.getSysSetSellDate().getTime());

        TradeDirectVo tradeDirect = new TradeDirectVo(orderInfo.getTradeDirection(),TradeTypeEnum.BUY.getValue());
        riskBean.setProductName(orderInfo.getProductCode());
        riskBean.setDirect(tradeDirect.getTradeDirect());
        riskBean.setMatchPrice(orderInfo.getBuyAvgPrice());
        riskBean.setMutableStopLoss(trailStopLoss == 1);//1开 0关
        //操作不包含修改止盈或者止损的情况下取值，否则前面已经赋值
        if(!opera.contains("1")){

        	riskBean.setStopGainPrice(orderInfo.getStopProfitPrice());
        }

        if(!opera.contains("2")){
        	riskBean.setStopLossPrice(orderInfo.getStopLossPrice());
        }

	}

}
