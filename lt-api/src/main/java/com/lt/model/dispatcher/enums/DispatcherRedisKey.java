package com.lt.model.dispatcher.enums;

public class DispatcherRedisKey {

	/**
	 *  redis中 行情Key 公共拼凑字段 例：Key : QUOTA_CL1601
	 */
	public static final String redisKey = "QUOTA_";

	/**
	 * 商品最新行情数据    key(商品): RedisQuotaObject 行情对象
	 */
	public static final String QUOTA_NEW = "QUOTA_NEW_";

	public static final String REDIS_PRODUCT_LAST_QUOTA = "redis:product:last:quota:";
	
	/**
	 *  商品最新行情数据    key(商品): TimeSharingplanBean 分时图对象
	 */
//	public static final String QUOTA_FST = "QUOTA_FST_";
	
	/**
	 *  商品最新行情数据    key(商品): TimeSharingplanBean 分时图对象
	 */
//	public static final String QUOTA_K = "QUOTA_K_";
	
	/**
	 * 在线用户与通道对应关系
	 */
	public static final String ONLINE = "ONLINE_USERID_CHANNEL";
}
