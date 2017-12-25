package com.lt.adapter.adapter.product.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 获取市场状态和交易时间段
 * 
 * @author jingwb
 *
 */
@Service
public class QueryStatusAndTimePeriodFunc extends BaseFunction {

	@Autowired
	private IProductApiService productApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		String productCode = StringTools.formatStr(paraMap.get("productCode"),"");// 商品code，格式：CL1612
		try {
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,
					productApiService.queryStatusAndTimePeriod(productCode));
		} catch (Exception e) {
			throw new LTException(e.getMessage());
		}
	}
}
