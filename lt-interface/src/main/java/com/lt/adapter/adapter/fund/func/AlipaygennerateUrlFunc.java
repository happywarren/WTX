package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：ChargeByAliPay   
* 类描述：支付宝充值生成链接接口   
* 创建人：yuanxin   
* 创建时间：2017年3月29日 下午4:02:51      
*/
@Component
public class AlipaygennerateUrlFunc extends BaseFunction {
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String userIdPara = StringTools.formatStr(paraMap.get("userId"), "-1");
		// 美元
		String income = StringTools.formatStr(paraMap.get("income"), "0");
		String url = StringTools.formatStr(paraMap.get("url"), "");
		String token = StringTools.formatStr(paraMap.get("token"),"");
		
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		
		if(rate == null){
			throw new LTException(LTResponseCode.FU00000);
		}
		url = url +"?token= "+token;
		Map<String,String> map = fundAccountApiService.createAlipayUrl(userIdPara, Double.valueOf(income), url,rate);
		
		return new Response(LTResponseCode.SUCCESS, "操作成功", map);
	}
}
