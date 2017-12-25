package com.lt.trade.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.business.product.IProductApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.trade.TradeServer;
import com.lt.trade.order.dao.OrderScoreInfoDao;
import com.lt.trade.order.service.IOrderFunctionService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
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

/**   
* 项目名称：lt-trade   
* 类名称：FunctionServiceImpl
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年12月15日 下午8:27:46      
*/
@Service
public class OrderFunctionServiceImpl implements IOrderFunctionService {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderScoreInfoDao scoreOrderDao;
	@Autowired
	private TradeServer tradeService;
	@Autowired
	private IProductApiService productApiService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private IProductInfoService productInfoServiceImpl;
	

	@Override
	public OrderScoreInfo getScoreOrderInfoById(String orderId) throws LTException {
		try{
			return scoreOrderDao.queryById(orderId);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}


	
	@Override
	public void updateScoreOrderInfo(OrderScoreInfo scoreOrdersInfo) throws LTException{
		try{
			scoreOrderDao.update(scoreOrdersInfo);
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
	public void setScoreOrderStopProfit(Double stopProfit,OrderScoreInfo newOrder, OrderScoreInfo oldOrder, InvestorFeeCfg feeCfg,ProductVo productVo)
			throws LTException {
		logger.info("止盈处理,StopProfitRange={},stopProfit={}",feeCfg.getStopProfitRange(),stopProfit);
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
		newOrder.setStopProfitPrice(TradeUtils.stopProfitPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopProfit, productVo.getJumpPrice(), productVo.getJumpValue()));
//		newOrder.setStopProfitPrice(DoubleUtils.scaleFormat(TradeUtils.stopProfitPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopProfit, productVo.getJumpPrice(), productVo.getJumpValue()), productVo.getDecimalDigits()));
	}

	
	@Override
	public void setScoreOrderStopLoss(Double stopLoss, OrderScoreInfo newOrder, OrderScoreInfo oldOrder,
			InvestorFeeCfg feeCfg, ProductVo productVo,Integer defstatu,Double mainScore,Double scoreAmt) throws LTException {
		int rangLen = feeCfg.getStopLossRange().split(",").length;
		newOrder.setActualHoldFund(DoubleTools.mul(DoubleTools.mul(DoubleTools.add(stopLoss  , feeCfg.getSurcharge()) ,oldOrder.getRate())  , oldOrder.getHoldCount()));
		newOrder.setStopLoss(DoubleTools.mul(DoubleTools.mul(stopLoss , oldOrder.getRate())  , oldOrder.getHoldCount()));
		String rangeMax = feeCfg.getStopLossRange().split(",")[rangLen -1];
		String rangeMin = feeCfg.getStopLossRange().split(",")[0];
		
		logger.info("止损区间设置判断max:{},min:{}",rangeMax,rangeMin);
		if(rangeMax != null && !rangeMax.equals("") && rangeMin != null && !rangeMin.equals("")){
			// 新的止损值不能大于最大范围， 不能小于浮动止盈止损绝对值 与最小止损 值的和
			if(Double.valueOf(rangeMax) < stopLoss ||  DoubleTools.add(Double.valueOf(rangeMin) , Math.abs(scoreAmt)) > stopLoss){
				throw new LTException("您当前可设置的最低止损金额为"+DoubleTools.mul(DoubleTools.add(Double.valueOf(rangeMin) , Math.abs(scoreAmt)),oldOrder.getHoldCount()) +"，请重新设置");
			}
		}else{
			throw new LTException(LTResponseCode.TDY0003);
		}
		
		newOrder.setPerStopLoss(stopLoss);
		newOrder.setPerSurcharge(feeCfg.getSurcharge());
		newOrder.setStopLossPrice(TradeUtils.stopLossPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopLoss, productVo.getJumpPrice(), productVo.getJumpValue()));
//		newOrder.setStopLossPrice(DoubleUtils.scaleFormat(TradeUtils.stopLossPrice(oldOrder.getTradeDirection(), oldOrder.getBuyAvgPrice(), stopLoss, productVo.getJumpPrice(), productVo.getJumpValue()), productVo.getDecimalDigits()));
		if(defstatu !=null ){
			if(defstatu.equals("1")){
				//newOrder.getActualHoldFund() - oldOrder.getActualHoldFund() + feeCfg.getDeferFund() * oldOrder.getRate()  * oldOrder.getHoldCount()
				double score = DoubleTools.add(DoubleTools.sub(newOrder.getActualHoldFund(),oldOrder.getActualHoldFund()) , DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate())  , oldOrder.getHoldCount()));
				if(mainScore < score){
					throw new LTException(LTResponseCode.FUY00001);
				}
			}else{
				double score = DoubleTools.sub(DoubleTools.sub(newOrder.getActualHoldFund(),oldOrder.getActualHoldFund()) , DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate())  , oldOrder.getHoldCount()));
				if(mainScore < score){
					throw new LTException(LTResponseCode.FUY00001);
				}
			}
		}else{
			double score = DoubleTools.sub(newOrder.getActualHoldFund(),oldOrder.getActualHoldFund());
			if(mainScore < score){
				throw new LTException(LTResponseCode.FUY00001);
			}
		}
	}



	@Override
	public void setScoreOrderDef(Integer defstatu, OrderScoreInfo newOrder, OrderScoreInfo oldOrder,
			InvestorFeeCfg feeCfg, ProductVo productVo,Double mainScore) throws LTException {
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
					if(DoubleTools.add(mainScore , stopLoss) < DoubleTools.mul(DoubleTools.mul(feeCfg.getDeferFund() , oldOrder.getRate()),  oldOrder.getHoldCount())){
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
	public void setScoreRiskBeanTrailLoss(RiskControlBean riskBean, OrderScoreInfo orderInfo,String opera,Integer trailStopLoss) throws LTException {
		riskBean.setProductName(orderInfo.getProductCode());
        riskBean.setDeferredOrderTimeStamp(orderInfo.getSysSetSellDate().getTime());
        
        TradeDirectVo tradeDirect = new TradeDirectVo(orderInfo.getTradeDirection(),TradeTypeEnum.BUY.getValue());
        riskBean.setProductName(orderInfo.getProductCode());
        riskBean.setDirect(tradeDirect.getTradeDirect());
        riskBean.setMatchPrice(orderInfo.getBuyAvgPrice());
        riskBean.setMutableStopLoss(trailStopLoss==1?true:false);//1开 0关
        
      //操作不包含修改止盈或者止损的情况下取值，否则前面已经赋值
        if(!opera.contains("1")){ 
        	
        	riskBean.setStopGainPrice(orderInfo.getStopProfitPrice());
        }
        
        if(!opera.contains("2")){
        	riskBean.setStopLossPrice(orderInfo.getStopLossPrice());
        }
		
	}

	@Override
	public int checkIfDeforLossAndProfit(InvestorFeeCfg investorFeeCfg, OrderScoreInfo scoreOrderInfo,String deferStatu, String stopLoss,
										 String stopfit,String trailStopLoss,Integer fundType,OrderLossProfitDefLog log) throws LTException {
		String type = "";
		double count = scoreOrderInfo.getHoldCount();
		double rate = scoreOrderInfo.getRate();

		Double oldStopProfit = scoreOrderInfo.getStopProfit();
		Double oldStopLoss = scoreOrderInfo.getStopLoss();
		Integer oldDeferStatus = scoreOrderInfo.getDeferStatus();
		Integer oldTrailStopLoss = scoreOrderInfo.getTrailStopLoss();

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

			logger.debug("deferStatu:{},scoreOrdersInfo:{}",deferStatu,oldDeferStatus);
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
}
