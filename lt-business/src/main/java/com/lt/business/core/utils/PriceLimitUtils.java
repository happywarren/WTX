package com.lt.business.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 涨跌幅限制刷新市场状态
 * 
 * @author guodw
 *
 */
public class PriceLimitUtils {

	static Logger logger = LoggerFactory.getLogger(PriceLimitUtils.class);
	
	/**
	 * 商品:权限状态     0：没权限 1 有权限
	 */
	public static Map<String, Integer> isOperateMap = new ConcurrentHashMap<String, Integer>();
	
	/**
	 * 商品：涨跌幅
	 */
	public static Map<String, Double> changeRateMap = new ConcurrentHashMap<String, Double>();

	
	
}
