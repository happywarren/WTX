package com.lt.adapter.adapter.product.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
/***
 * 获取市场状态
 * @author jingwb
 *
 */
@Service
public class QueryMarketStatusFunc extends BaseFunction{
	@Autowired 
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		List<String> list = new ArrayList<String>();
		String[] codes = String.valueOf(paraMap.get("proCodes")).split(",");
		for(String proCode : codes){
			list.add(proCode);
		}
		try {
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,
			productApiService.queryMarketStatus(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		return response;
	}
	
}
