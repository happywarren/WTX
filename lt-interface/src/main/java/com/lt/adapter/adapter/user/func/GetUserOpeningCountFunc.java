package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

import javolution.util.FastMap;

/**   
* 项目名称：lt-interface  </br> 
* 类名称：GetUserBankInfoListFunc   </br> 
* 类描述：查询用户开户的开户次数</br> 
* 创建人：yubei   </br> 
* 创建时间：2017-05-24 
*/
@Component
public class GetUserOpeningCountFunc extends BaseFunction {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	
	@Override
	public Response response(Map<String, Object> paraMap) {
			logger.info("**********开始查询***************");
			String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
			Map<String, Object> result = FastMap.newInstance();
			int openingStep = userApiBussinessService.getActivityAccountStep(userId);
			int openingCount = userApiBussinessService.getUserOpeningCount(userId,openingStep);
			String userName =  StringTools.formatStr(userApiBussinessService.queryUserBuyId(userId).getUserName(), "");
			result.put("openingStep",openingStep);
			result.put("openingCount",openingCount);
			result.put("userName",userName );
			Response response = new Response(LTResponseCode.SUCCESS, "查询成功", result);
			return response;
	}
}
