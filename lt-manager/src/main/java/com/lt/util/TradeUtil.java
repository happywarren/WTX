package com.lt.util;

import java.util.Random;

import com.lt.api.trade.IOrderScoreApiService;
import org.slf4j.Logger;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.trade.IOrderApiService;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.dispatcher.enums.DispatcherRedisKey;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;

public class TradeUtil {
	private static Logger logger = LoggerTools.getInstance(TradeUtil.class);

	
	/**
	 * 计算汇率后浮动盈亏
	 * @param proCode
	 * @param TradeType
	 * @param sysBuyAvgPrice
	 * @param jumpPrice
	 * @param jumpValue
	 * @param holdCount
	 * @return
	 */
	public static Double dealFloatLossProfit(String proCode,Integer TradeType,Double sysBuyAvgPrice,Double jumpPrice,
								Double jumpValue,Integer holdCount,Double rate,RedisTemplate<String, String> redisTemplate) {
		Double floatLossProfit = 0.0;
		
		//从redis中获取最新的行情
		String key = DispatcherRedisKey.QUOTA_NEW + proCode;
		BoundHashOperations<String, String, RedisQuotaObject> redisHash = redisTemplate.boundHashOps(key);
		
		//获取当前行情
		RedisQuotaObject obj = redisHash.get(proCode);
		logger.info("======获取行情数据={}",JSONObject.toJSONString(obj));
		if(obj == null){
			return 0.0;
		}
		//计算每个点多少钱
		double price = DoubleUtils.div(jumpPrice,jumpValue);
		//判断订单方向：看多 看空
		if(TradeType == TradeDirectionEnum.DIRECTION_UP.getValue()){//看多					
			floatLossProfit =  DoubleTools.mul(DoubleTools.mul(DoubleTools.mul(DoubleUtils.sub(Double.valueOf(obj.getBidPrice1()),sysBuyAvgPrice),price),rate),holdCount);
		}else if(TradeType == TradeDirectionEnum.DIRECTION_DOWN.getValue()){//看空
			floatLossProfit = DoubleTools.mul(DoubleTools.mul(DoubleTools.mul(DoubleUtils.sub(sysBuyAvgPrice,Double.valueOf(obj.getAskPrice1())) , price) ,rate), holdCount) ;
		}
		return floatLossProfit;
	}
	/**
	 * 计算实际浮动盈亏
	 * @param proCode
	 * @param TradeType
	 * @param sysBuyAvgPrice
	 * @param jumpPrice
	 * @param jumpValue
	 * @param holdCount
	 * @return
	 */
	public static Double dealFloatLossProfitRate(String proCode,Integer TradeType,Double sysBuyAvgPrice,Double jumpPrice,
								Double jumpValue,Integer holdCount,Double rate,RedisTemplate<String, String> redisTemplate) {
		Double floatLossProfit = 0.0;
		
		//从redis中获取最新的行情
		String key = DispatcherRedisKey.QUOTA_NEW + proCode;
		BoundHashOperations<String, String, RedisQuotaObject> redisHash = redisTemplate.boundHashOps(key);
		
		//获取当前行情
		RedisQuotaObject obj = redisHash.get(proCode);
		logger.info("======获取行情数据={}",JSONObject.toJSONString(obj));
		if(obj == null){
			return 0.0;
		}
		//计算每个点多少钱
		double price = DoubleUtils.div(jumpPrice,jumpValue);
		//判断订单方向：看多 看空
		if(TradeType == TradeDirectionEnum.DIRECTION_UP.getValue()){//看多					
			//实际盈亏
			floatLossProfit =  DoubleTools.mul(DoubleTools.mul(DoubleTools.sub(Double.valueOf(obj.getBidPrice1()),sysBuyAvgPrice),price),holdCount);
		}else if(TradeType == TradeDirectionEnum.DIRECTION_DOWN.getValue()){//看空
			//实际盈亏
			floatLossProfit = DoubleTools.mul(DoubleTools.mul(DoubleUtils.sub(sysBuyAvgPrice,Double.valueOf(obj.getAskPrice1())) , price) , holdCount) ;
		}
		return floatLossProfit;
	}
	public static void dealMoveStopLoss(OrderParamVO order,IOrderApiService orderApiService){

		//移动止损最高价
		Double sentinelPrice = orderApiService.getMoveStopLoss(order.getOrderId(), order.getProductCode(), order.getPlate());
		logger.info("===========orderId:{},sentinelPrice:{}===========",order.getOrderId(),sentinelPrice);
		if(sentinelPrice == null){
			return;
		}
		//移动止损价格点位
		Double dw = DoubleUtils.div(DoubleUtils.mul(order.getPerStopLoss(),order.getJumpValue()),order.getJumpPrice());

		//移动止损价最高价
		Double moveStopLossPrice = DoubleTools.mul((sentinelPrice-dw),order.getHoldCount());
		double price  = DoubleTools.div(order.getJumpPrice(),order.getJumpValue());
		//移动止损金额
		Double moveStopLoss = 0.0;
		if(order.getTradeDirection() == TradeDirectionEnum.DIRECTION_UP.getValue()){//看多
			moveStopLoss = DoubleUtils.mul(DoubleUtils.sub(DoubleUtils.sub(sentinelPrice,dw),order.getBuyAvgPrice()),price);

			//计算移动止损价格
			Double dd = DoubleUtils.mul(DoubleUtils.div(moveStopLoss, order.getJumpPrice()),order.getJumpValue());
			moveStopLossPrice = DoubleTools.mul(order.getBuyAvgPrice()+dd, 1,8);
			moveStopLoss = DoubleUtils.mul(moveStopLoss,order.getRate());
			moveStopLoss = DoubleTools.mul(moveStopLoss,order.getHoldCount());
		}else{//看空
			moveStopLoss = DoubleUtils.mul(DoubleUtils.sub(DoubleUtils.sub(order.getBuyAvgPrice(),sentinelPrice),dw),price);

			//计算移动止损价格
			Double dd = DoubleUtils.mul(DoubleUtils.div(moveStopLoss, order.getJumpPrice()),order.getJumpValue());
			moveStopLossPrice = DoubleTools.mul(order.getBuyAvgPrice()-dd, 1,8);
			moveStopLoss = DoubleUtils.mul(moveStopLoss,order.getRate());
			moveStopLoss = DoubleTools.mul(moveStopLoss,order.getHoldCount());

		}


		order.setMoveStopLoss(moveStopLoss);//当前移动止损金额
		order.setMoveStopLossPrice(moveStopLossPrice);//当前移动止损价
	}


	public static void dealMoveStopLoss(OrderParamVO order,IOrderScoreApiService orderApiService){

		//移动止损最高价
		Double sentinelPrice = orderApiService.getMoveStopLoss(order.getOrderId(), order.getProductCode(), order.getPlate());
		logger.info("===========orderId:{},sentinelPrice:{}===========",order.getOrderId(),sentinelPrice);
		if(sentinelPrice == null){
			return;
		}
		//移动止损价格点位
		Double dw = DoubleUtils.div(DoubleUtils.mul(order.getPerStopLoss(),order.getJumpValue()),order.getJumpPrice());

		//移动止损价最高价
		Double moveStopLossPrice = DoubleTools.mul((sentinelPrice-dw),order.getHoldCount());
		double price  = DoubleTools.div(order.getJumpPrice(),order.getJumpValue());
		//移动止损金额
		Double moveStopLoss = 0.0;
		if(order.getTradeDirection() == TradeDirectionEnum.DIRECTION_UP.getValue()){//看多
			moveStopLoss = DoubleUtils.mul(DoubleUtils.sub(DoubleUtils.sub(sentinelPrice,dw),order.getBuyAvgPrice()),price);

			//计算移动止损价格
			Double dd = DoubleUtils.mul(DoubleUtils.div(moveStopLoss, order.getJumpPrice()),order.getJumpValue());
			moveStopLossPrice = DoubleTools.mul(order.getBuyAvgPrice()+dd, 1,8);
			moveStopLoss = DoubleUtils.mul(moveStopLoss,order.getRate());
			moveStopLoss = DoubleTools.mul(moveStopLoss,order.getHoldCount());
		}else{//看空
			moveStopLoss = DoubleUtils.mul(DoubleUtils.sub(DoubleUtils.sub(order.getBuyAvgPrice(),sentinelPrice),dw),price);

			//计算移动止损价格
			Double dd = DoubleUtils.mul(DoubleUtils.div(moveStopLoss, order.getJumpPrice()),order.getJumpValue());
			moveStopLossPrice = DoubleTools.mul(order.getBuyAvgPrice()-dd, 1,8);
			moveStopLoss = DoubleUtils.mul(moveStopLoss,order.getRate());
			moveStopLoss = DoubleTools.mul(moveStopLoss,order.getHoldCount());

		}


		order.setMoveStopLoss(moveStopLoss);//当前移动止损金额
		order.setMoveStopLossPrice(moveStopLossPrice);//当前移动止损价
	}


	/**
	 * 获取当前行情价
	 * @param proCode
	 * @param TradeType
	 * @param redisTemplate
	 * @return
	 */
	public static Double getQuotaPrice(String proCode,Integer TradeType,RedisTemplate<String, String> redisTemplate){
		//从redis中获取最新的行情
		String key = DispatcherRedisKey.QUOTA_NEW + proCode;
		BoundHashOperations<String, String, RedisQuotaObject> redisHash = redisTemplate.boundHashOps(key);
				
		//获取当前行情
		RedisQuotaObject obj = redisHash.get(proCode);
//		logger.info("======获取行情数据={}",JSONObject.toJSONString(obj));
		if(obj == null){
			return 0.0;
		}
		//判断订单方向：看多 看空
		if(TradeType == TradeDirectionEnum.DIRECTION_UP.getValue()){//看多	
			return Double.valueOf(obj.getBidPrice1());		
		}else{
			return Double.valueOf(obj.getAskPrice1());	
		}
	}
	//根据指定长度生成纯数字的随机数
    public static int createData(int length) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        String data=sb.toString();
        return Integer.valueOf(data);
    }

}
