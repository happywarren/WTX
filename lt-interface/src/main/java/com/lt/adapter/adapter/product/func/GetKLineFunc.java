package com.lt.adapter.adapter.product.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.product.KLineBean;

@Service
public class GetKLineFunc extends BaseFunction{
	@Autowired 
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String product = StringTools.formatStr(paraMap.get("productCode"),"");
		Integer type = StringTools.formatInt(paraMap.get("type"),-1);
		Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		//查询
		KLineBean bean = productApiService.getKLineBean(product, type);
		response.setData(bean);
		return response;	
	}
}
