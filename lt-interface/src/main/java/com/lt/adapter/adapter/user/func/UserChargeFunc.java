package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.user.charge.IUserApiRechargeService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UserChargeFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月3日 上午11:51:11      
*/
@Component
public class UserChargeFunc extends BaseFunction {
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Autowired
	private IUserApiRechargeService userRechargeServiceImpl;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		if(rate == null){
			throw new LTException(LTResponseCode.FU00000);
		}
		
		paraMap.put("rate", rate);
		
		String amtStr =  StringTools.formatStr(paraMap.get("amt"), "0.00");
		String rmbAmtStr = StringTools.formatStr(paraMap.get("rmbAmt"), "0.00");
		
		// 金额校验，银行卡id校验，三级业务功能校验
		if (!StringTools.isNumberic(amtStr, false, true, true)|| !StringTools.isNumberic(rmbAmtStr, false, true, true)
				|| !StringTools.isNotEmptyObject(paraMap.get("bankCardId")) || !StringTools.isNotEmptyObject(paraMap.get("thirdOptCode")) ) {
            throw new LTException(LTResponseCode.FU00003);
        }
		logger.info("当前支付渠道代码:{}"+paraMap.get("thirdOptCode"));
		Map<String, Object> map = userRechargeServiceImpl.userChargeFund(paraMap);
		logger.info("服务输出:{}",map.toString());
		Response response = new Response(map.get("code").toString(), map.get("msg").toString(), map);
		
		return response;
	}
}
