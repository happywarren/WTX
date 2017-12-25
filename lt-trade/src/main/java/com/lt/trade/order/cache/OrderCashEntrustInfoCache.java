/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.cache
 * FILE    NAME: CashEntrustRecordCache.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.cache;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

/**
 * 现金委托单缓存
 * @author XieZhibing
 * @date 2016年12月10日 下午5:46:00
 * @version <b>1.0.0</b>
 */
public class OrderCashEntrustInfoCache implements Serializable {


	private static final long serialVersionUID = 7068501451741442925L;
	
	/** 现金委托单缓存 */
	private static ConcurrentHashMap<String, OrderCashEntrustInfo> cache = new ConcurrentHashMap<String, OrderCashEntrustInfo>();

	/**
	 * 
	 *  添加现金委托单
	 * @author XieZhibing
	 * @date 2016年12月10日 下午6:53:50
	 * @param cashEntrustRecord
	 * @return
	 */
	public static boolean put(OrderCashEntrustInfo cashEntrustRecord){		
		if(cashEntrustRecord == null){
			//委托单缓存失败 
			throw new LTException(LTResponseCode.TD00010);
		}
		//报给交易所的委托单号
		String entrustId = cashEntrustRecord.getEntrustId();
		if(StringTools.isEmpty(entrustId)){
			//委托单缓存失败 
			throw new LTException(LTResponseCode.TD00011);
		}
		return cache.put(entrustId, cashEntrustRecord) != null;
	}
	/**
	 * 
	 * 获取现金委托单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:54:17
	 * @param entrustId
	 * @return
	 */
	public static OrderCashEntrustInfo get(String entrustId){
		return cache.get(entrustId);
	}
	/**
	 * 
	 *  删除现金委托单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:44:16
	 * @param entrustId
	 * @return
	 */
	public static boolean remove(String entrustId){
		return cache.remove(entrustId) != null;
	}
	
}
