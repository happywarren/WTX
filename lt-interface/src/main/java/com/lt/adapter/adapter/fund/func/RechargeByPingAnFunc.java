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
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：RechargeByPingAnFunc   
* 类描述：平安银行充值   
* 创建人：yuanxin   
* 创建时间：2017年5月8日 下午5:46:41      
*/
@Component
public class RechargeByPingAnFunc extends BaseFunction {
	
	@Autowired
	private IFundAccountApiService fundAccountServiceImpl;
	
	@Autowired
	private IProductApiService productApiServiceImpl ;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		if(rate == null ){
			throw new LTException(LTResponseCode.FU00000);
		}
		paraMap.put("rate", rate);
		fundAccountServiceImpl.pingAnTransfer(paraMap);
		Response response = new Response(LTResponseCode.SUCCESS, "充值成功");
		return response;
	}
}
