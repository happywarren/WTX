/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.cache
 * FILE    NAME: CashOrderFundCache.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.vo.fund.FundOrderVo;

/**
 * TODO 现金订单付款数据缓存
 * @author XieZhibing
 * @date 2016年12月11日 下午5:10:11
 * @version <b>1.0.0</b>
 */
public class CashOrderFundCache {
	
	/** 现金订单付款数据缓存 */
	public static ConcurrentHashMap<String, FundOrderVo> cache = new ConcurrentHashMap<String, FundOrderVo>();
	
	/**
	 * 
	 * TODO 添加现金订单
	 * @author XieZhibing
	 * @date 2016年12月10日 下午6:53:50
	 * @param fundOrderVo
	 * @return
	 */
	public static boolean put(FundOrderVo fundOrderVo){		
		if(fundOrderVo == null){
			//订单扣款记录缓存失败 
			throw new LTException(LTResponseCode.TD00012);
		}
		//订单显示ID
		String orderId = fundOrderVo.getOrderId();
		if(StringTools.isEmpty(orderId)){
			//订单扣款记录缓存失败 
			throw new LTException(LTResponseCode.TD00012);
		}
		return cache.put(orderId, fundOrderVo) != null;
	}
	/**
	 * 
	 * TODO 获取现金订单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:54:17
	 * @param orderId
	 * @return
	 */
	public static FundOrderVo get(String orderId){
		return cache.get(orderId);
	}
	/**
	 * 
	 * TODO 删除现金订单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:44:16
	 * @param orderId
	 * @return
	 */
	public static boolean remove(String orderId){
		return cache.remove(orderId) != null;
	}
	
}
