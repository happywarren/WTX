package com.lt.adapter.adapter.product.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.model.product.ProductType;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Service
public class GetProductKindsFunc extends BaseFunction {
	@Autowired
	private IProductApiService productApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {

		// 用户版本号
		String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");
		String userId = StringTools.formatStr(paraMap.get("userId"), null);
		
		Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		// 查询
		List<ProductType> list = null;
/*		if (StringTools.isEmpty(clientVersion)) {
			list = productApiService.findAllProductKinds();
		} else {
			list = productApiService.findProductKindsByCondition(clientVersion);
	}
*/		list = productApiService.findProductKindsByCondition(clientVersion,userId);
		response.setData(list);
		return response;
	}
}
