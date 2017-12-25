package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiAutoRechargeService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：QryUserChargeMaxAmtFunc   
* 类描述： 查询用户最大入金金额
* 创建人：yuanxin   
* 创建时间：2017年6月13日 上午10:28:17      
*/
@Component
public class QryUserChargeMaxAmtFunc extends BaseFunction {
	
	@Autowired
	private IUserApiAutoRechargeService userApiAutoRechargeServiceImpl ;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		/** 用户id*/
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		/** 银行编号*/
		String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "-1");
		/** 银行卡id*/
		String bankCardId = StringTools.formatStr(paraMap.get("bankCard"), "-1");
		
		Response response = null ;
		
		try{

			Map<String,Object> map = userApiAutoRechargeServiceImpl.qryBankMaxRechargeAmount(bankCardId, userId, bankCode);
			
			response = new Response(LTResponseCode.SUCCESS, "查询成功", map);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		
		return response;
	}
}
