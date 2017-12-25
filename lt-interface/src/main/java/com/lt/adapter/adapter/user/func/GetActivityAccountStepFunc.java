package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface  </br> 
* 类名称：GetUserBankInfoListFunc   </br> 
* 类描述：查询用户开户的步骤</br> 
* 创建人：yubei   </br> 
* 创建时间：2017-05-24 
*/
@Component
public class GetActivityAccountStepFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
			//Map<String, Object> map = userApiBussinessService.getActivityAccountStep(userId);
			//Response response = new Response(LTResponseCode.SUCCESS, "查询成功", map);
			return null;
		}catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage());
		}
	}
}
