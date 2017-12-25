package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.user.IUserRechargeService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：KqGetDynamicNum   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月31日 下午4:45:05      
*/
@Component
public class KqGetDynamicNumFunc extends BaseFunction {
	
	@Autowired
	private IUserRechargeService userRechargeServiceImpl;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		if(rate == null){
			throw new LTException(LTResponseCode.FU00000);
		}
		paraMap.put("rate", rate);
		Map<String,String> map =  userRechargeServiceImpl.getKQDynamicNum(paraMap);
		if(map != null && map.get("code").equals( LTResponseCode.SUCCESS)){
			return new Response(LTResponseCode.SUCCESS, "获取验证码成功",map);
		}
		
		return new Response(LTResponseCode.FUY00008, "快钱功能异常", map);
	}
}
