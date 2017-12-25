package com.lt.business.core.service.product.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lt.model.product.Product;
import com.lt.model.product.ProductRiskConfig;
import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;
import com.lt.vo.defer.ProductDeferRunRecordVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lt.api.business.product.IProductApiService;
import com.lt.business.core.service.product.IProductService;
import com.lt.business.core.service.product.IProductTaskService;
import com.lt.business.core.service.sys.IRulesOfTransactionsService;
import com.lt.business.core.utils.KLine;
import com.lt.model.product.ProductType;
import com.lt.model.sys.RulesOfTransactions;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.KLineBean;
import com.lt.vo.product.ProductQuotaVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.user.UserProductSelectListVo;

@Service
public class ProductApiServiceImpl implements IProductApiService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IProductService productService;
	@Autowired
	private IProductTaskService productTaskService;
	@Autowired
	private IRulesOfTransactionsService rulesOfTransactionsService;
	
	@Override
	public List<ProductVo> findProductList() {
		return productService.findProductList();
	}

	@Override
	public void updateProductStatus(int marketStatus,String product) {
		try {
			productService.updateProductStatus(marketStatus,product);
		} catch (Exception e) {
			throw new LTException(LTResponseCode.PR001, e.fillInStackTrace());
		}
		
	}

	@Override
	public List<List<ProductVo>> queryProductLobby(Map<String, Object> map) {
		return productService.queryProductLobby(map);
	}

	@Override
	public ProductVo getProductInfo(String proCode) {
		return productService.getProductInfo(proCode);
	}

	@Override
	public void loadProTimeCfgToRedis() throws Exception{
		productTaskService.loadProTimeCfgToRedis();
	}

	@Override
	public void refreshProMarketStatusForRedis() throws Exception {
		productTaskService.refreshProMarketStatusForRedis();
	}

	@Override
	public ProductQuotaVo findQuotaObjectByCode(String productCode)
			throws LTException {
		return productService.findQuotaObjectByCode(productCode);
	}

	
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月16日 上午10:10:36
	 * @see com.lt.api.business.product.IProductApiService#findQuotaObjectByCodeList(java.util.List)
	 * @param codeList
	 * @return
	 * @throws LTException
	 */
	@Override
	public List<ProductQuotaVo> findQuotaObjectByCodeList(List<String> codeList) throws LTException {
		// TODO Auto-generated method stub
		return productService.findQuotaObjectByCodeList(codeList);
	}

	@Override
	public List<ProductQuotaVo> findQuotaObjectListByCode() throws LTException {
		return productService.findQuotaObjectListByCode();
	}

	@Override
	public List<Map<String, String>> queryMarketStatus(List<String> list) {
		return productService.queryMarketStatus(list);
	}

	@Override
	public boolean isContinueThreeDayHoliday(Date date,String exchangeCode) throws Exception {
		return productService.isContinueThreeDayHoliday(date,exchangeCode);
	}

	@Override
	public Map<String, Object> queryStatusAndTimePeriod(String productCode)
			throws Exception {
		if(!StringTools.isNotEmpty(productCode)){
			throw new LTException(LTResponseCode.PR00001);
		}
		return productService.queryStatusAndTimePeriod(productCode);
	}
	

	@Override
	public List<UserProductSelectListVo> findAttentionList(String userId,int type) throws LTException {
		logger.info("userId:{},type:{}",userId,type);
		if(StringTools.isNotEmpty(type)){
			return productService.findAttentionList(userId,type);
		}
		return productService.findAttentionListByUserId(userId);
	}

	
	@Override
	public List<ProductType> findAllProductKinds()throws LTException {
		return productService.findAllProductKinds();
	}
	
	@Override
	public List<ProductType> findProductKindsByCondition(String clientVersion,String userId) throws LTException {
		return productService.findProductKindsByCondition(clientVersion,userId);
	}

	@Override
	public Double getRate(String currency) {
		return productService.getRate(currency);
	}

	@Override
	public RulesOfTransactions select(String shortCode){
		return rulesOfTransactionsService.select(shortCode);
	}
	
	@Override
	public Double getRateByProductId(Integer productId) {
		return productService.getRateByProductId(productId);
	}
	
	@Override
	public KLineBean getKLineBean(String product,Integer type){
		return KLine.getProductKLineByType(type, product);
	}

	@Override
	public Product loadProduct(Integer id) {
		return productService.loadProduct(id);
	}

	@Override
	public List<PeroidOrderHolidayVo> findAllCodeHoliday(List<String> list) {
		return productService.findAllCodeHoliday(list);
	}

	@Override
	public List<ProNextTradePeriodVo> qryNextDayTradeTime(List<String> list) {
		return productService.qryNextDayTradeTime(list);
	}

	@Override
	public ProductRiskConfig queryProductRiskConfigByUserId(Integer productId, String userId) {
		return productService.queryProductRiskConfigByUserId(productId,userId);
	}

}
