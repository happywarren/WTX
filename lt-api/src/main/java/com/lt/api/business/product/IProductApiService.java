package com.lt.api.business.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lt.model.product.Product;
import com.lt.model.product.ProductRiskConfig;
import com.lt.model.product.ProductType;
import com.lt.model.sys.RulesOfTransactions;
import com.lt.util.error.LTException;
import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;
import com.lt.vo.defer.ProductDeferRunRecordVo;
import com.lt.vo.product.KLineBean;
import com.lt.vo.product.ProductQuotaVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.user.UserProductSelectListVo;

/**
 * 业务模块 商品提供的api接口
 * @author guodw
 *
 */
public interface IProductApiService {
	
	/**
	 * 查询所有商品信息      
	 * 不管是否在售状态 
	 * @return
	 */
	public List<ProductVo> findProductList();
	
	/**
	 * 更新商品交易状态
	 * @param marketStatus市场状态 0：闭市、1：可买不可卖（仅开仓）、2：可卖不可买（仅平仓）、3：可买可卖 、4：休市
	 * @param product商品名称
	 */
	public void updateProductStatus(int marketStatus, String product);
	
	/**
	 * 获取商品大厅信息
	 * @param map
	 * @return
	 */
	public List<List<ProductVo>> queryProductLobby(Map<String,Object> map);
	
	/**
	 * 查询商品市场状态
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> queryMarketStatus(List<String> list);
	
	/**
	 * 查询市场状态和时间段
	 * @param productCode
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryStatusAndTimePeriod(String productCode) throws Exception;
	
	/**
	 * 获取商品相关信息
	 * @param proCode
	 * @return
	 */
	public ProductVo getProductInfo(String proCode);
	
	/**
	 * 每天早上6点加载商品时间配置信息到redis
	 */
	public void loadProTimeCfgToRedis() throws Exception;
	
	/**
	 * 每分钟刷新商品市场状态
	 * @throws Exception
	 */
	public void refreshProMarketStatusForRedis() throws Exception;

	/**
	 * 判断当前时间,改交易所是否是连续三天放假
	 * true:是 false:否
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public boolean isContinueThreeDayHoliday(Date date,String exchangeCode) throws Exception;
	
	/**
	 * 获取商品大厅涨跌幅值及行情数据
	 * @param productCode
	 * @return
	 */
	ProductQuotaVo findQuotaObjectByCode(String productCode) throws LTException;
	/**
	 * 
	 * TODO 获取商品大厅涨跌幅值及行情数据 
	 * @author XieZhibing
	 * @date 2017年2月16日 上午10:09:38
	 * @param codeList 产品编码列表
	 * @return
	 * @throws LTException
	 */
	List<ProductQuotaVo> findQuotaObjectByCodeList(List<String> codeList) throws LTException;
	/**
	 * 获取商品大厅涨跌幅值及行情数据 
	 * @return
	 */
	List<ProductQuotaVo> findQuotaObjectListByCode() throws LTException;

	 /**
	  * 获取全部商品类型
	  * @return
	  * @throws LTException
	  */
	List<ProductType> findAllProductKinds() throws LTException;
	
	 /**
	  * 根据条件获取商品类型
	  * @param clientVersion 版本号
	  * @return
	  * @throws LTException
	  */
	List<ProductType> findProductKindsByCondition(String clientVersion,String userId) throws LTException;

	/**
	 * 查询关注列表
	 * @param userId
	 * @param type
	 * @return
	 * @throws LTException
	 */
	List<UserProductSelectListVo> findAttentionList(String userId, int type)
			throws LTException;
	/**
	 * 根据币种获取汇率
	 * @param currency
	 * @return
	 */
	public Double getRate(String currency);

	/**
	 * 交易规则查询
	 * @param shortCode
	 * @return
	 */
	RulesOfTransactions select(String shortCode);
	/**
	 * 根据productId获取汇率
	 * @param productId
	 * @return
	 */
	Double getRateByProductId(Integer productId);

	/**
	 * 获取最后一个K线图数据
	 * @param product
	 * @param type
	 * @return
	 */
	KLineBean getKLineBean(String product, Integer type);

	/**
	 * 获取商品基本信息
	 * @param id
	 * @return
	 */
	Product loadProduct(Integer id);

	/**
	 * 查询相关品种的未开始的节假日
	 * @param list
	 * @return
	 * @return:       List<PeroidOrderHoliday>
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月8日 下午7:38:34
	 */
	public List<PeroidOrderHolidayVo> findAllCodeHoliday(List<String> list);

	/**
	 * 查询品种的清仓时间
	 * @param list
	 * @return
	 * @return:       List<ProNextTradePeriod>
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月9日 上午10:23:51
	 */
	public List<ProNextTradePeriodVo> qryNextDayTradeTime(List<String> list);

	/**
	 * 查询风险等级配置信息
	 * @param productId
	 * @param userId
	 * @return
	 */
	public ProductRiskConfig queryProductRiskConfigByUserId(Integer productId, String userId);


}
