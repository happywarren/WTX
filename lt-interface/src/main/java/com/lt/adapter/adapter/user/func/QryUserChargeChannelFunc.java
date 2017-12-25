package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiAutoRechargeService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：QryUserChargeChannelFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年6月13日 上午10:50:43      
*/
@Component
public class QryUserChargeChannelFunc extends BaseFunction {
	
	@Autowired
	private IUserApiAutoRechargeService userApiAutoRechargeServiceImpl ;
	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = new Response(LTResponseCode.SUCCESS, "查询成功");
		try {
			
			/** 用户id*/
			String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
			/** 银行编号*/
			String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "-1");
			/** 银行卡id*/
			String bankCard = StringTools.formatStr(paraMap.get("bankCard"), "-1");
			/** 充值金额*/
			String amountStr =  StringTools.formatStr(paraMap.get("amount"), "0");
			
			Double amount = Double.valueOf(amountStr);
			
			amount = DoubleTools.scaleFormat(amount,2) ;
			
			Map<String,String> map = userApiAutoRechargeServiceImpl.qryChargeChannel(bankCard, userId, bankCode, amount);
			response.setData(map);
			return response;
		} catch (Exception e) {
			logger.error("++++++++++++++++++"+e.getMessage());
			return LTResponseCode.getCode(e.getMessage());
		}
	}

}
