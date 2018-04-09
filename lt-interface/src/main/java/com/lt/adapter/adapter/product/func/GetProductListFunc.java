package com.lt.adapter.adapter.product.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lt.vo.product.ProductQuotaVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.model.product.ProductType;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.UserProductSelectListVo;

@Service
public class GetProductListFunc extends BaseFunction{
	Logger logger = LoggerFactory.getLogger(getClass()); 
	@Autowired 
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		int type = StringTools.formatInt(paraMap.get("productTypeId"), -1);

		type = 1;  //只拿期货的数据
		
		String userId = StringTools.formatStr(paraMap.get("userId"), null);
		Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		logger.info("type:{}----------  user:{}",type,userId);
		//查询
		List<UserProductSelectListVo> list = productApiService.findAttentionList(userId,type);

		//获取最新行情
		List<String> prdcodeList = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			prdcodeList.add(list.get(i).getProductCode());
		}
		List<ProductQuotaVo> quotaList = productApiService.findQuotaObjectByCodeList(prdcodeList);
		for(ProductQuotaVo quotaVo : quotaList){
			UserProductSelectListVo tmp = null;

			for(UserProductSelectListVo userProductSelectListVo : list){
				if(userProductSelectListVo.getProductCode().equals(quotaVo.getCode())){
					tmp = userProductSelectListVo;
					break;
				}
			}

			if(tmp != null){
				tmp.setBidPrice(quotaVo.getBidPrice());
				tmp.setAskPrice(quotaVo.getAskPrice());
				tmp.setLastPrice(quotaVo.getLastPrice());
				tmp.setPercentage(quotaVo.getPercentage());
				tmp.setChangeValue(quotaVo.getChangeValue());
			}

		}
		response.setData(list);
		return response;	
	}
}
