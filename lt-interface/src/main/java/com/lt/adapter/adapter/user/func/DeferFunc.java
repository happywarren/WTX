package com.lt.adapter.adapter.user.func;

import java.util.HashMap;
import java.util.Map;

import com.lt.model.product.ProductRiskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Component
public class DeferFunc extends BaseFunction {
	@Autowired
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Integer productId = StringTools.formatInt(paraMap.get("productId"), 0);
		Integer plateType = StringTools.formatInt(paraMap.get("plateType"), 0);
		String userId = paraMap.get("userId").toString();
        ProductRiskConfig prc = productApiService.queryProductRiskConfigByUserId(productId,userId);
		Double rate = productApiService.getRateByProductId(productId);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("deferFee", DoubleTools.mulDec2(prc.getDeferFee(), rate));//递延费
		map.put("deferFund", DoubleTools.mulDec2(prc.getDeferFund(),rate));//递延保证金
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
	}

}