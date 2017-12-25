package com.lt.adapter.adapter.order.func;

import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.fund.FundTypeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductRiskConfigService;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;


/**
 * 
 * 查询用户订单递延费
 * @author yubei
 * @date 2017-11-03
 */
@Service
public class FindOrderDeferredFeeFunc extends BaseFunction {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IOrderApiService orderApiService;
	
	@Autowired
	private IProductRiskConfigService productRiskConfig;

	@Autowired
	private IOrderScoreApiService orderScoreApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		if(!StringTools.isAllNotEmpty((String)paraMap.get("tradeDirection"),
				(String)paraMap.get("plate"),
				(String)paraMap.get("investorId"),
				(String)paraMap.get("fundType"),
				(String)paraMap.get("productId"),
				(String)paraMap.get("productCode")
				
		)){
			throw new LTException(LTResponseCode.TRJ0000);
		}
		Integer tradeDirection = StringTools.formatInt(paraMap.get("tradeDirection"), 0);
		String userId = StringTools.formatStr(paraMap.get("userId"), "");
		Integer plate = StringTools.formatInt(paraMap.get("plate"), 0);
		String investorId = StringTools.formatStr(paraMap.get("investorId"), "");
		int fundType = StringTools.formatInt(paraMap.get("fundType"), 0);
		//double deferInterest = StringTools.formatDouble((String)paraMap.get("deferInterest"));
		int buySuccCount = StringTools.formatInt(paraMap.get("buySuccCount"), 1);
		String productCode = StringTools.formatStr(paraMap.get("productCode"), "");
		int productId = StringTools.formatInt((String)paraMap.get("productId"), 0);
		double deferFee = productRiskConfig.queryProductRiskConfig(productId, userId);
		double deferredFee = 0L;

		if (fundType == FundTypeEnum.SCORE.getValue()) {
			deferredFee = orderScoreApiService.getOrderDeferredFee(plate, tradeDirection, investorId,deferFee,productCode);
		} else {
			deferredFee = orderApiService.getOrderDeferredFee(plate, tradeDirection, investorId,deferFee,productCode);
		}
		//deferredFee = deferredFee*buySuccCount;
		logger.info("===========data={}=============", JSONObject.toJSONString(deferredFee));
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, deferredFee);
	}
}
