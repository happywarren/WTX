package com.lt.adapter.adapter.user.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiAutoRechargeService;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：QryUserChargeBankListFund   
* 类描述：查询用户所有的充值银行卡限额信息   
* 创建人：yuanxin   
* 创建时间：2017年6月14日 下午2:00:31      
*/
@Component
public class QryUserChargeBankListFunc extends BaseFunction {

	@Autowired
	private IUserApiAutoRechargeService userApiAutoRechargeServiceImpl ;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		/** 用户id*/
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		
		Response response = null;
		List<UserchargeBankDetailInfo> list =  userApiAutoRechargeServiceImpl.qryBankChargeAmountList(userId);
		response = new Response(LTResponseCode.SUCCESS, "查询成功", list);
		
		return response;
	}
}
