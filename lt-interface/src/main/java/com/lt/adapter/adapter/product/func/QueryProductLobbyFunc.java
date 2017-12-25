package com.lt.adapter.adapter.product.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import com.lt.vo.product.ProductVo;
/**
 * 查询商品大厅
 * @author jingwb
 *
 */
@Service
public class QueryProductLobbyFunc extends BaseFunction{
	
	@Autowired 
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		try {
			List<List<ProductVo>> list = productApiService.queryProductLobby(paraMap);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		
	}
}
