package com.lt.business.core.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.service.product.IProductService;
import com.lt.business.core.service.product.IProductTimeConfigService;
import com.lt.business.core.utils.PriceLimitUtils;
import com.lt.business.core.utils.QuotaTimeConfigUtils;
import com.lt.model.product.ProductTimeConfig;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.prop.CustomerPropertiesConfig;
import com.lt.vo.product.ProductVo;

@Service
public class QuotaInitializeServer implements InitializingBean {

	public static Logger logger = LoggerFactory
			.getLogger(QuotaInitializeServer.class);

	@Autowired
	private CustomerPropertiesConfig propCfg;

	@Autowired
	private RedisTemplate<String, String> redis;

	private static RedisTemplate<String, String> redisTemplate;

	@Autowired
	private IProductService productService;

	@Autowired
	private IProductTimeConfigService productTimeConfigService;

	/**
	 * 商品 ： 开始时间 结束时间
	 */
	public static Map<String, Map<String, String>> productMap = new ConcurrentHashMap<String, Map<String, String>>();

	/**
	 * 日k线使用时间 商品 ： 开市时间： 闭市时间 例子：AU1706 21:00 15:30
	 */
	public static Map<String, Map<String, String>> map = new ConcurrentHashMap<String, Map<String, String>>();

	/**
	 * 每一个商品一个处理进程
	 */
	private static Map<String, ExecutorService> executorMapPool = new ConcurrentHashMap<String, ExecutorService>();

	/**
	 * 未知商品使用一个线程
	 */
	private static ExecutorService executor = Executors
			.newSingleThreadExecutor();
	/**
	 * 如果为true 不等待 抛弃处理数据
	 */
	public static Map<String, Boolean> boolMap = new ConcurrentHashMap<String, Boolean>();

	/**
	 * 配置文件中文件生成路径 如：/data/futures/
	 */
	private static String path = "/data/futuresquota/";

	/**
	 * 路径结束标记 ：.fst
	 */
	private static String pathEndFlagFST = ".fst";

	/**
	 * 路径结束标记 ：.k
	 */
	private static String pathEndFlagK = ".k";

	/**
	 * 路径结束标记 ：.log
	 */
	private static String pathEndFlagLog = ".log";

	@Override
	public void afterPropertiesSet() throws Exception {
		loadInit();
		redisTemplate = redis;
		path = propCfg.getProperty("path") == null ? path : propCfg
				.getProperty("path");
		if (System.getProperties().getProperty("os.name").contains("Windows")) {
			path = "C:" + path;
		}
		pathEndFlagFST = propCfg.getProperty("path_end_flag_fst") == null ? pathEndFlagFST
				: propCfg.getProperty("path_end_flag_fst");
		pathEndFlagK = propCfg.getProperty("path_end_flag_k") == null ? pathEndFlagK
				: propCfg.getProperty("path_end_flag_k");
		pathEndFlagLog = propCfg.getProperty("path_end_flag_log") == null ? pathEndFlagLog
				: propCfg.getProperty("path_end_flag_log");

	}

	/**
	 * 初始化方法
	 */
	public void loadInit() {
		List<ProductVo> productList = productService.findProductList();

		if (productList != null && !productList.isEmpty()) {

			for (ProductVo product : productList) {
				String futureCode = product.getProductCode();
				// 根据品种加载对应的线程，一个品种一个线程
				executorMapPool.put(futureCode,
						Executors.newSingleThreadExecutor());

				// 加载行情开闭市时间
				initProductMap(futureCode, product.getId());
				boolMap.put(futureCode, false);
				if (product != null) {
					if (product.getIsOperate() != null) {
						PriceLimitUtils.isOperateMap.put(futureCode,
								product.getIsOperate());

					}
					if (product.getFloatLimit() != null) {
						PriceLimitUtils.changeRateMap.put(futureCode,
								product.getFloatLimit() * 100);
					}
				}
			}
		} else {
			for (String product : QuotaTimeConfigUtils.list) {
				// 根据品种加载对应的线程，一个品种一个线程
				executorMapPool.put(product,
						Executors.newSingleThreadExecutor());

				// 加载行情开闭市时间
				initLocalProductMap(product);
				boolMap.put(product, false);
				PriceLimitUtils.isOperateMap.put(product, 1);
				PriceLimitUtils.changeRateMap.put(product, 0.07 * 100);
			}
		}

	}

	// private void initPriceLimit(){
	//
	// }

	/**
	 * 当数据库找不到配置文件时加载本地时间配置
	 * 
	 * @param product
	 */
	private void initLocalProductMap(String product) {
		productMap.put(product, QuotaTimeConfigUtils.init(product));
		map.put(product, QuotaTimeConfigUtils.initLocalMap(product));
	}

	/**
	 * 加载商品行情开闭市时间
	 * 
	 * @param product
	 * @param productId
	 */
	private void initProductMap(String product, int productId) {
		try {
			// 加载开闭市时间
			List<ProductTimeConfig> list = productTimeConfigService
					.findProductTimeConfigByProductId(productId);
			if (null == list) {
				return;
			}
			Map<String, String> map = new ConcurrentHashMap<String, String>();
			for (ProductTimeConfig obj : list) {
				try {
					if (productId == obj.getProductId()) {
						if (StringTools.isNotEmpty(obj.getQuotaBeginTime())
								&& StringTools
										.isNotEmpty(obj.getQuotaEndTime())) {
							if (DateTools
									.toDefaultTime(obj.getQuotaBeginTime())
									.getTime() > DateTools.toDefaultTime(
									obj.getQuotaEndTime()).getTime()) {

								map.put(obj.getQuotaBeginTime(), "23:59:59");
								map.put("00:00:00", obj.getQuotaEndTime());

							} else {
								map.put(obj.getQuotaBeginTime(),
										obj.getQuotaEndTime());
							}

						}
					}
				} catch (Exception e) {
					logger.error("productTimeConfig:"
							+ JSONObject.toJSONString(obj));
					logger.error("初始化行情开闭市时间异常", e);
					continue;
				}

			}
			logger.info("product = {} , map = {} ", product,
					JSONObject.toJSONString(map));
			productMap.put(product, map);
		} catch (Exception e) {
			logger.error("初始化行情开闭市时间异常", e);
			return;
		}
	}

	public static ExecutorService getExecutorService(String product) {
		try {
			if (StringTools.isEmpty(product)) {
				return executor;
			}
			if (executorMapPool != null && !executorMapPool.isEmpty()) {
				ExecutorService service = executorMapPool.get(product);
				if (service != null) {
					return service;
				}
			}
		} catch (Exception e) {
			logger.error("业务模块获取线程处理异常.e={}.", e);

		}
		return executor;
	}


	public static String getPath() {
		return path;
	}

	public static String getPathEndFlagFST() {
		return pathEndFlagFST;
	}

	public static String getPathEndFlagK() {
		return pathEndFlagK;
	}

	public static String getPathEndFlagLog() {
		return pathEndFlagLog;
	}

	public static synchronized RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

}
