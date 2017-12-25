package com.lt.constant.redis;

/**
 * 所有redis缓存的key都放在该util中定义，便于管理
 * @author jingwb
 *
 */
public class RedisUtil {

	/**
	 * 缓存商品信息key
	 * BoundHashOperations<String, String, ProductVo>  redis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
	 * ProductVo product = redis.get("productCode")
	 */
	public static final String PRODUCT_INFO="PRODUCT_INFO";	
	
	/**
	 * 用于存储商品时间配置信息key
	 * BoundHashOperations<String, String, TimeConfigVO>  redis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
	 * TimeConfigVO vo = redis.get("CL1601")
	 */
	public static final String PRODUCT_TIME_CONFIG="PRODUCT_TIME_CONFIG";
	
	/**
	 * 系统配置缓存key
	 * BoundHashOperations<String, String, String>  sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
	 * String cfg_value = sysCfgRedis.get("cfg_code")
	 */
	public static final String SYS_CONFIG="SYS_CONFIG";
	
	/**
	 * 快钱API充值，获取手机验证码返回快钱token令牌信息等数据
	 */
	public static final String FINANCY_KUAIQIAN_PAY_INFO="financy:kuaiqian_pay_info";
	
	/**
	 * 外部订单号与用户id关联的redis
	 */
	public static final String ORDER_EXTERNAL_ID = "ORDER_EXTERNAL_ID";
	
	
	/**
	 * 有盾配置信息
	 * 
	*/
	public static final String YOUDUN_CONFIG ="YOUDUN_CONFIG";
	
	/**
	 * 智付配置
	 */
	public static final String DINPAY_CONFIG ="DINPAY_CONFIG";
	
	/**
	 * 卖出唯一限制redis
	 */
	public static final String ORDER_EXTERNAL_ID_SELL = "ORDER_EXTERNAL_ID_SELL";
	/**
	 * 卖出唯一限制redis
	 */
	public static final String ORDER_EXTERNAL_ID_SELL_SUCCESS = "ORDER_EXTERNAL_ID_SELL_SUCCESS";

	/**
	 * push配置信息
	 */
	public static final String PUSH_CONFIG="PUSH_CONFIG";
	

	/**
	 *  redis中 常盈/常亏用户列表key
	 */
	public static final String PROFIT_LOSS_USER_LIST = "PROFIT_LOSS_USER_LIST";

	/**
	 *  跟单用户列表key
	 */
	public static final String FOLLOW_USER_LIST = "FOLLOW_USER_LIST";
	/**
	 *  商品清仓时间自动加载开关
	 */
	public static final String  PRODUCT_CLEAR_TIME_FLAG= "PRODUCT_CLEAR_TIME_FLAG";

	/**
	 *  redis 智付提现结果查询接口并发锁
	 */
	public static final String  DINPAY_QUERY_TRANSFER_LOCK= "DINPAY_QUERY_TRANSFER_LOCK";
	/**
	 *  redis 钱通提现结果查询接口并发锁
	 */
	public static final String  QTPAY_QUERY_TRANSFER_LOCK= "QTPAY_QUERY_TRANSFER_LOCK";

	/**
	 * 限制条件
	 */
	public static final String LIMIT_CONDITION = "LIMIT_CONDITION";
	
	/**
	 * 资金--充值--配置
	 */
	public static final String FUND_RECHARGE_CONFIG	= "FUND_RECHARGE_CONFIG";

	/**
	 * 默认品牌
	 */
	public static final String DEFAULT_BRAND	= "DEFAULT_BRAND";//'20170905020140725'

}
