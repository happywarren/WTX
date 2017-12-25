/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.cache
 * FILE    NAME: ScoreEntrustRecordCache.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.cache.score;

import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 积分委托单缓存
 * @author XieZhibing
 * @date 2016年12月10日 下午5:46:00
 * @version <b>1.0.0</b>
 */
public class OrderScoreEntrustInfoCache implements Serializable {


	private static final long serialVersionUID = 7068501451741442925L;
	
	/** 积分委托单缓存 */
	private static ConcurrentHashMap<String, OrderScoreEntrustInfo> cache = new ConcurrentHashMap<String, OrderScoreEntrustInfo>();

	/**
	 * 
	 *  添加积分委托单
	 * @author XieZhibing
	 * @date 2016年12月10日 下午6:53:50
	 * @param ScoreEntrustRecord
	 * @return
	 */
	public static boolean put(OrderScoreEntrustInfo ScoreEntrustRecord){		
		if(ScoreEntrustRecord == null){
			//委托单缓存失败 
			throw new LTException(LTResponseCode.TD00010);
		}
		//报给交易所的委托单号
		String entrustId = ScoreEntrustRecord.getEntrustId();
		if(StringTools.isEmpty(entrustId)){
			//委托单缓存失败 
			throw new LTException(LTResponseCode.TD00011);
		}
		return cache.put(entrustId, ScoreEntrustRecord) != null;
	}
	/**
	 * 
	 * 获取积分委托单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:54:17
	 * @param entrustId
	 * @return
	 */
	public static OrderScoreEntrustInfo get(String entrustId){
		return cache.get(entrustId);
	}
	/**
	 * 
	 *  删除积分委托单
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:44:16
	 * @param entrustId
	 * @return
	 */
	public static boolean remove(String entrustId){
		return cache.remove(entrustId) != null;
	}
	
}
