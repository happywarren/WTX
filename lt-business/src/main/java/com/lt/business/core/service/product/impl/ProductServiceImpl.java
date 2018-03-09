package com.lt.business.core.service.product.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.business.core.dao.product.*;
import com.lt.model.product.ProductRiskConfig;
import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;
import com.lt.vo.defer.ProductDeferRunRecordVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lt.api.user.IUserApiService;
import com.lt.business.core.server.QuotaInitializeServer;
import com.lt.business.core.service.product.IProductService;
import com.lt.business.core.utils.TimeSharingplanUtils;
import com.lt.constant.LTConstant;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.product.ProductMarketEnum;
import com.lt.enums.product.ProductTypeEnum;
import com.lt.model.dispatcher.enums.DispatcherRedisKey;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.model.product.Product;
import com.lt.model.product.ProductType;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.ProductQuotaVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.product.TimeConfigVO;
import com.lt.vo.product.TimeConfigVO.TradeAndQuotaTime;
import com.lt.vo.user.UserProductSelectListVo;

@Service
public class ProductServiceImpl implements IProductService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IProductDao productDao;
	@Autowired
	private IProductSelectDao productSelectDao;
	@Autowired
	private IProductTypeDao productTypeDao;
	@Autowired
	private IProductTagDao productTagDao;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private IUserApiService userApiService;
	@Autowired
	private IProductRiskConfigDao productRiskConfigDao;

	@Override
	public List<ProductVo> findProductList() {
		return productDao.findAllToProductVo();
	}

	@Override
	public void updateProductStatus(int marketStatus, String product) {
		try {
			productDao.updateProductStatus(marketStatus, product);
		} catch (Exception e) {
			throw new LTException(LTResponseCode.PR001, e.fillInStackTrace());
		}

	}

	@Override
	public List<List<ProductVo>> queryProductLobby(Map<String, Object> map) {
		try {
			String userId = StringTools.formatStr(map.get("userId"), null);
			UserBaseInfo info = userApiService.findUserByUserId(userId);
			if (StringTools.isNotEmpty(info) && StringTools.isNotEmpty(info.getInvestorAccountId())) {
				map.put("investorId", info.getInvestorAccountId());
			}
			List<List<ProductVo>> rlist = new ArrayList<List<ProductVo>>();
			// 如果版本号clientVersion小于2.2.1，排除差价合约商品类型
			String clientVersion = StringTools.formatStr(map.get("clientVersion"), "");
			String excludeProductTypeCode = "";
			// 查询商品类型信息
			List<ProductType> types = null;
			if (StringTools.isNotEmpty(clientVersion)) {
				if (clientVersion.compareTo(LTConstant.CFD_MIN_VERSION) < 0) {
					excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
					types = productTypeDao.selectProductTypesByCondition(excludeProductTypeCode);
				} else {
					types = productTypeDao.selectProductTypes(null);
				}
			} else {
				excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
				types = productTypeDao.selectProductTypesByCondition(excludeProductTypeCode);
			}


			for (ProductType type : types) {
				if(type.getCode().equals(ProductTypeEnum.HAS.getCode()) || type.getCode().equals(ProductTypeEnum.HKS.getCode()) || type.getCode().equals(ProductTypeEnum.CONTRACT.getCode()))
					continue;
				// 查询该类型下的商品信息
				map.put("productTypeId", type.getId());
				List<ProductVo> pros = productDao.selectProductLobby(map);
				if (StringTools.isNotEmpty(pros)) {
					if (StringTools.isNotEmpty(map.get("fundType"))) {
						for (ProductVo vo : pros) {
							vo.setFundType(Integer.valueOf(map.get("fundType").toString()));
						}
					}
					rlist.add(pros);
				}
			}
			return rlist;
		} catch (Exception e) {
			logger.error("获取商品大厅异常", e);
			throw new LTException(LTResponseCode.FU00000);
		}

	}

	@Override
	public ProductVo getProductInfo(String proCode) {
		// 获取缓存中所有商品信息
		BoundHashOperations<String, String, ProductVo> proRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
		ProductVo product = proRedis.get(proCode);
		return product;
	}

	@Override
	public ProductQuotaVo findQuotaObjectByCode(String productCode) {
		try {
			// 内存中获取最新行情数据
			RedisQuotaObject obj = TimeSharingplanUtils.map.get(productCode);
			// 若为空
			if (!StringTools.isNotEmpty(obj)) {
				// redis 中获取
				String key = DispatcherRedisKey.QUOTA_NEW + productCode;
				BoundHashOperations<String, String, RedisQuotaObject> hashOps = QuotaInitializeServer.getRedisTemplate()
						.boundHashOps(key);
				obj = hashOps.get(productCode);
				// 若为空，没有商品的对于缓存行情数据
				if (!StringTools.isNotEmpty(obj)) {
					return null;
				}
				// 把缓存中的数据加载到本地内存
				TimeSharingplanUtils.add(obj);
			}
			ProductQuotaVo vo = new ProductQuotaVo(productCode, obj.getBidPrice1(), obj.getAskPrice1(),
					obj.getLastPrice(), obj.getChangeRate());
			vo.setChangeValue(obj.getChangeValue());
			return vo;
		} catch (Exception e) {
			logger.error("查询单个大厅商品异常 . productCode:" + productCode, e);
			throw new LTException(LTResponseCode.FU00000);
		}
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2017年2月16日 上午10:11:39
	 * @see com.lt.business.core.service.product.IProductService#findQuotaObjectByCodeList(java.util.List)
	 * @param codeList
	 * @return
	 * @throws LTException
	 */
	@Override
	public List<ProductQuotaVo> findQuotaObjectByCodeList(List<String> codeList) throws LTException {
		try {
			List<ProductQuotaVo> objList = new ArrayList<ProductQuotaVo>();
			for (String productCode : codeList) {
				// 获取商品涨跌幅值及行情数据
				ProductQuotaVo obj = findQuotaObjectByCode(productCode);
				if (!StringTools.isNotEmpty(obj)) {
					continue;
				}
				objList.add(obj);
			}
			return objList;
		} catch (Exception e) {
			logger.error("获取商品大厅涨跌幅值及行情数据异常", e);
			throw new LTException(LTResponseCode.FU00000);
		}
	}

	@Override
	public List<ProductQuotaVo> findQuotaObjectListByCode() {
		try {
			// 查询所有大厅显示商品
			List<Product> list = productDao.findAllByStatus(ProductMarketEnum.PRODUCT_STATUS_UP.getValue());
			if (!StringTools.isNotEmpty(list)) {
				logger.error("上架的大厅商品不存在");
				throw new LTException(LTResponseCode.PR00002);
			}
			List<ProductQuotaVo> objList = new ArrayList<ProductQuotaVo>();
			for (Product product : list) {
				ProductQuotaVo obj = findQuotaObjectByCode(product.getProductCode());
				if (!StringTools.isNotEmpty(obj)) {
					continue;
				}
				objList.add(obj);
			}
			return objList;
		} catch (Exception e) {
			logger.error("查询大厅商品列表异常", e);
			throw new LTException(LTResponseCode.FU00000);
		}
	}

	@Override
	public List<Map<String, String>> queryMarketStatus(List<String> list) {
		return productDao.selectMarketStatus(list);
	}

	@Override
	public boolean isContinueThreeDayHoliday(Date date, String exchangeCode) throws Exception {
		if (!StringTools.isNotEmpty(date) || StringTools.isEmpty(exchangeCode)) {
			logger.info("-----判断当前时间,该交易所是否是连续三天放假----参数缺失-");
			return true;
		}
		String date2 = DateTools.parseToDefaultDateTimeString(DateTools.add(date, 2));
		Map<String, String> map = new HashMap<String, String>();
		map.put("date2", date2);
		map.put("exchangeCode", exchangeCode);
		Integer count = productDao.selectHolidayCount(map);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> queryStatusAndTimePeriod(String productCode) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		String now = DateTools.parseToDefaultTimeString(date);// 00:00:00
		// 商品信息
		BoundHashOperations<String, String, ProductVo> proRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);

		ProductVo product = proRedis.get(productCode);
		if (product == null) {
			logger.error("productCode = {}", productCode);
			throw new LTException(LTResponseCode.PR00002);
		}
		// 查询商品时间配置信息
		BoundHashOperations<String, String, TimeConfigVO> proTimeCfgRedis = redisTemplate
				.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
		TimeConfigVO timeConfigVO = proTimeCfgRedis.get(productCode);
		if (timeConfigVO == null) {
			throw new LTException(LTResponseCode.PRJ0001);
		}
		List<TradeAndQuotaTime> tradeTimes = timeConfigVO.getTradeTimeList();// 交易时间段
		if (product.getMarketStatus() == ProductMarketEnum.STOP_TRADING.getValue()) {// 闭市
			stopTrading(map, product, tradeTimes, now);
		} else if (product.getMarketStatus() == ProductMarketEnum.REST.getValue()) {// 休市
			rest(map, product, tradeTimes, now);
		} else if (product.getMarketStatus() == ProductMarketEnum.ONLY_LIQUIDATION.getValue()) {// 仅平
			onlyLiquidation(map, product, tradeTimes, now);
		} else if (product.getMarketStatus() == ProductMarketEnum.START_TRADING.getValue()) {// 开市
			startTrading(map, product, tradeTimes, now);
		} else if (product.getMarketStatus() == ProductMarketEnum.ONLY_OPEN.getValue()) {// 仅开仓
			onlyOpen(map, product, tradeTimes, now);
		}

		return map;
	}

	/**
	 * 闭市或节假日
	 * 
	 * @param map
	 * @param product
	 * @param tradeTimes
	 * @throws ParseException
	 */
	private void stopTrading(Map<String, Object> map, Product product, List<TradeAndQuotaTime> tradeTimes, String now)
			throws ParseException {
		map.put("isHoliday", true);// 默认节假日
		map.put("remark", "节假日");
		map.put("status", product.getMarketStatus());

		for (int i = 0; i < tradeTimes.size(); i++) {
			TradeAndQuotaTime tradeTime = tradeTimes.get(i);
			boolean b = DateTools.compareTo(DateTools.toDefaultTime(tradeTime.getBeginTime()),
					DateTools.toDefaultTime(now));
			if (b) {
				if (tradeTime.getEndTime().equals("23:59:59") && i == tradeTimes.size() - 2) {
					map.put("time", tradeTime.getBeginTime() + "-" + tradeTimes.get(i + 1).getEndTime());
				} else {
					map.put("time", tradeTime.getBeginTime() + "-" + tradeTime.getEndTime());
				}
				map.put("remark", "闭市");
				map.put("isHoliday", false);
				break;
			}
		}
	}

	/**
	 * 休市
	 * 
	 * @param map
	 * @param product
	 * @param tradeTimes
	 * @throws ParseException
	 */
	private void rest(Map<String, Object> map, Product product, List<TradeAndQuotaTime> tradeTimes, String now)
			throws ParseException {
		map.put("isHoliday", false);// 默认节假日
		map.put("remark", "休市");
		map.put("status", product.getMarketStatus());
		for (int i = 0; i < tradeTimes.size(); i++) {
			TradeAndQuotaTime tradeTime = tradeTimes.get(i);
			boolean b = DateTools.compareTo(DateTools.toDefaultTime(tradeTime.getBeginTime()),
					DateTools.toDefaultTime(now));
			if (b) {
				if (tradeTime.getEndTime().equals("23:59:59") && i == tradeTimes.size() - 2) {
					map.put("time", tradeTime.getBeginTime() + "-" + tradeTimes.get(i + 1).getEndTime());
				} else {
					map.put("time", tradeTime.getBeginTime() + "-" + tradeTime.getEndTime());
				}
				break;
			}
		}
	}

	/**
	 * 仅平
	 * 
	 * @param map
	 * @param product
	 * @param tradeTimes
	 * @throws ParseException
	 */
	private void onlyLiquidation(Map<String, Object> map, Product product, List<TradeAndQuotaTime> tradeTimes,
			String now) throws ParseException {
		map.put("isHoliday", false);// 默认节假日
		map.put("remark", "仅平仓");
		map.put("status", product.getMarketStatus());
		for (int i = 0; i < tradeTimes.size(); i++) {
			TradeAndQuotaTime tradeTime = tradeTimes.get(i);
			boolean b = DateTools.compareTo(DateTools.toDefaultTime(tradeTime.getBeginTime()),
					DateTools.toDefaultTime(now));
			if (b) {
				if (tradeTime.getEndTime().equals("23:59:59") && i == tradeTimes.size() - 2) {
					map.put("time", tradeTime.getBeginTime() + "-" + tradeTimes.get(i + 1).getEndTime());
				} else {
					map.put("time", tradeTime.getBeginTime() + "-" + tradeTime.getEndTime());
				}
				break;
			}
		}
	}

	/**
	 * 开市
	 * 
	 * @param map
	 * @param product
	 * @param tradeTimes
	 * @throws ParseException
	 */
	private void startTrading(Map<String, Object> map, Product product, List<TradeAndQuotaTime> tradeTimes, String now)
			throws ParseException {
		map.put("isHoliday", false);
		map.put("remark", "开市");
		map.put("status", product.getMarketStatus());
		for (TradeAndQuotaTime tradeTime : tradeTimes) {
			// 判断当前时间是否在当前获取的交易时间段内，如果在，则取出当前清仓时间点
			boolean b = DateTools.betweenDate1AndDate2(DateTools.toDefaultTime(tradeTime.getBeginTime()),
					DateTools.toDefaultTime(tradeTime.getEndTime()), DateTools.toDefaultTime(now));
			if (b) {
				map.put("time", tradeTime.getTime());
				break;
			}
		}
	}

	/**
	 * 仅开仓
	 * 
	 * @param map
	 * @param product
	 * @param tradeTimes
	 * @throws ParseException
	 */
	private void onlyOpen(Map<String, Object> map, Product product, List<TradeAndQuotaTime> tradeTimes, String now)
			throws ParseException {
		map.put("isHoliday", false);
		map.put("remark", "仅开仓");
		map.put("status", product.getMarketStatus());
		for (TradeAndQuotaTime tradeTime : tradeTimes) {
			// 判断当前时间是否在当前获取的交易时间段内，如果在，则取出当前清仓时间点
			boolean b = DateTools.betweenDate1AndDate2(DateTools.toDefaultTime(tradeTime.getBeginTime()),
					DateTools.toDefaultTime(tradeTime.getEndTime()), DateTools.toDefaultTime(now));
			if (b) {
				map.put("time", tradeTime.getTime());
				break;
			}
		}
	}

	@Override
	public List<UserProductSelectListVo> findAttentionList(int type) {
		if (type < 0) {
			// 热门标签 = 13
			return productTagDao.selectProductByTagId(13);
		} else {
			// 根据品种种类获取品种列表
			return productTypeDao.selectProductByTypeId(type);
		}
	}

	@Override
	public List<UserProductSelectListVo> findAttentionList(String userId, int type) {
		// 查询用户是否存在所属券商
		UserBaseInfo info = userApiService.findUserByUserId(userId);
		if (StringTools.isNotEmpty(info) && StringTools.isNotEmpty(info.getInvestorAccountId())) {
			if (type < 0) {
				// 热门标签 = 13
				return productTagDao.selectProductForTagId(13, userId, info.getInvestorAccountId());
			} else {
				// 根据品种种类获取品种列表
				return productTypeDao.selectProductForTypeId(userId, info.getInvestorAccountId(), type);
			}
		}
		return findAttentionList(type);
	}

	@Override
	public Product loadProduct(Integer id) {
		return productDao.loadProduct(id);
	}

	@Override
	public List<PeroidOrderHolidayVo> findAllCodeHoliday(List<String> list) {
		return productDao.findAllCodeHoliday(list);
	}

	@Override
	public List<ProNextTradePeriodVo> qryNextDayTradeTime(List<String> list) {
		return productDao.qryNextDayTradeTime(list);
	}

	@Override
	public ProductRiskConfig queryProductRiskConfigByUserId(Integer productId, String userId) {
		return productRiskConfigDao.selectProductRiskConfigByUserId(productId, userId);
	}

	@Override
	public List<UserProductSelectListVo> findAttentionListByUserId(String userId) {
		// 查询用户是否存在所属券商
		UserBaseInfo info = userApiService.findUserByUserId(userId);
		if (StringTools.isNotEmpty(info) && StringTools.isNotEmpty(info.getInvestorAccountId())) {
			return productSelectDao.selectProductForInvestorGroup(userId, info.getInvestorAccountId());
		}
		return productSelectDao.selectObjectByUserId(userId);
	}

	@Override
	public List<ProductType> findAllProductKinds() {
		return productTypeDao.selectProductTypes(null);
	}

	@Override
	public Double getRate(String currency) {
		String rate = productDao.selectRateByCurrency(currency);
		return Double.valueOf(rate);
	}

	@Override
	public Double getRateByProductId(Integer productId) {
		ProductVo product = productDao.selectProductInfoById(productId);
		return product.getRate();
	}

	@Override
	public List<ProductType> findProductKindsByCondition(String clientVersion, String userId) {
		// 如果版本号clientVersion小于2.2.1，排除差价合约商品类型
		String excludeProductTypeCode = "";
		UserBaseInfo user = userApiService.findUserByUserId(userId);
		String investorId = user.getInvestorAccountId();
		if (StringTools.isEmpty(investorId)) {
			investorId = LTConstant.DEFAULT_INVESTOR_ID + "";
		}
		if (StringTools.isEmpty(clientVersion)) {
			excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
		} else {
			if (clientVersion.compareTo(LTConstant.CFD_MIN_VERSION) < 0) {
				excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
			}
			// else {
				// 如果数字合约商品数量为0，不显示改商品类型
			//	return this.productTypeDao.selectProductTypeListByCondition(investorId, excludeProductTypeCode);
				// int productNum =
				// this.productDao.getProductCountByCondition(investorId,ProductTypeEnum.CONTRACT.getCode());
				// if(productNum<1){
				// excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
				// }
			}
		//}
		// return
		// productTypeDao.selectProductTypesByCondition(excludeProductTypeCode);
		return this.productTypeDao.selectProductTypeListByCondition(investorId, excludeProductTypeCode);
	}

}
