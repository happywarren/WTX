package com.lt.adapter.adapter.product.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
/**
 * 获取人民币汇率 
 * @author jingwb
 *
 */
@Service
public class GetRateByCurrencyFunc  extends BaseFunction{

	@Autowired 
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String currency = String.valueOf(paraMap.get("currency"));
		Double rate = productApiService.getRate(currency);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, rate);
	}
}
