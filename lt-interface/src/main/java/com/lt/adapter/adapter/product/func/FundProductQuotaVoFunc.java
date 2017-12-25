package com.lt.adapter.adapter.product.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.product.ProductQuotaVo;
/**
 * 获取商品大厅涨跌幅值及行情数据
 * @author jingwb
 *
 */
@Service
public class FundProductQuotaVoFunc extends BaseFunction{
	@Autowired 
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String productCode = StringTools.formatStr(paraMap.get("productCode"),"");		
		Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		if(StringTools.isNotEmpty(productCode)){
			List<String> prdcodeList = new ArrayList<String>();
			String[] prdcodes = productCode.split(",");
			for (String prdcode : prdcodes) {
				if(StringTools.isNotEmpty(prdcode)){
					prdcodeList.add(prdcode);
				}
			}
			List<ProductQuotaVo> list = productApiService.findQuotaObjectByCodeList(prdcodeList);
			response.setData(list);
		}else{
			List<ProductQuotaVo> list = productApiService.findQuotaObjectListByCode();
			response.setData(list);
		}
		return response;	
	}
}
