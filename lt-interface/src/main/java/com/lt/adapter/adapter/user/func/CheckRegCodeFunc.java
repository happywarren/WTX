package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：CheckRegCodeFunc   
* 类描述：用户校验验证码   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午3:40:05      
*/
@Component
public class CheckRegCodeFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String tele = StringTools.formatStr(paraMap.get("tele"), "");
		String clientCode = StringTools.formatStr(paraMap.get("code"), "");
		String msg_type =StringTools.formatStr(paraMap.get("msg_type"), "");
		String brandCode =StringTools.formatStr(paraMap.get("brand"), "");
		Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		
		if (!StringTools.isNotEmpty(clientCode)) {
			return LTResponseCode.getCode(LTResponseCode.US01107);
		}
		
		userApiService.checkRegCode(tele,brandCode, msg_type, clientCode);
		
		return response;
	}

}
