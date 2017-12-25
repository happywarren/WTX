/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.cache
 * FILE    NAME: OrderCache.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.cache;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.lt.model.trade.OrderCashInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

/**
 *现金订单缓存
 * @author XieZhibing
 * @date 2016年12月10日 下午5:46:00
 * @version <b>1.0.0</b>
 */
public class OrderCashInfoCache implements Serializable {

	private static final long serialVersionUID = 7068501451741442925L;
	
	/** 现金订单缓存 */
	private static ConcurrentHashMap<String, OrderCashInfo> cache = new ConcurrentHashMap<String, OrderCashInfo>();
	
	/**
	 * 
	 * 添加现金订单
	 * @author XieZhibing
	 * @date 2016年12月10日 下午6:53:50
	 * @param cashOrdersInfo
	 * @return
	 */
	public static boolean put(OrderCashInfo cashOrdersInfo){
		if(cashOrdersInfo == null){
			//订单缓存失败 
			throw new LTException(LTResponseCode.TD00009);
		}
		//订单显示ID
		String orderId = cashOrdersInfo.getOrderId();
		if(StringTools.isEmpty(orderId)){
			//订单缓存失败 
			throw new LTException(LTResponseCode.TD00009);
		}
		return cache.put(orderId, cashOrdersInfo) != null;
	}
	
	/**
	 * 
	 * 获取现金订单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:54:17
	 * @param orderId
	 * @return
	 */
	public static OrderCashInfo get(String orderId){
		return cache.get(orderId);
	}
	/**
	 * 
	 * 删除现金订单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:44:16
	 * @param orderId
	 * @return
	 */
	public static boolean remove(String orderId){
		return cache.remove(orderId) != null;
	}
	
}
