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
* 类名称：RechargeDailyTopAmtChkFunc   
* 类描述：校验用户单日入金限额   
* 创建人：yuanxin   
* 创建时间：2017年4月10日 上午11:21:51      
*/
@Component
public class RechargeDailyTopAmtChkFunc extends BaseFunction {
	
	@Autowired
	private IFundAccountApiService fundAccountServiceImpl;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		String userIdStr = StringTools.formatStr(paraMap.get("userId"), "-1");
		String inAmtStr =  StringTools.formatStr(paraMap.get("inAmt"), "0");
		
		/** 充值类型，0 ：默认 1：平安银行*/
		String chargeType = StringTools.formatStr(paraMap.get("chargeType"), "0");
		
		Response response = new Response(LTResponseCode.SUCCESS, "查询成功");
		
		if(inAmtStr.equals(0) || !StringTools.isNumberic(inAmtStr, false, true, true)
				|| userIdStr.equals("-1")){
			throw new LTException(LTResponseCode.FU00003);
		}
		
		Double inAmt = Double.valueOf(inAmtStr);
		String userId = userIdStr;
		Double restAmt = fundAccountServiceImpl.getDailyUserRechargeTotalAmt(userId);
		
		if(inAmt > restAmt){
			response.setData(new Boolean(false));
			response.setMsg("单日限额已超限，请明日再来尝试！");
		}else{
			if(chargeType.equals("1")){
				Double restPingan = fundAccountServiceImpl.getPingAnRechargeAmtInYear(userId);
				Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
				
				if(((inAmt+restPingan)/rate) > 200000){
					response.setData(new Boolean(false));
					response.setMsg("平安入金通道已超限额，请尝试其他入金方式");
				}else{
					response.setData(new Boolean(true));
				}
			}else{
				response.setData(new Boolean(true));
			}
		}
		
		return response;
	}
}
