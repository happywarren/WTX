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
* 类名称：GetUserBankInfoListFunc </br> 
* 类描述：查询用户开户的步骤 </br> 
* 创建人：yubei   </br> 
* 创建时间：2017-05-24 
*/
@Component
public class GetUserBaseInfoCountByIdCardNumFunc extends BaseFunction {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			logger.info("*****************根据身份证号码查询此用户是否开户成功历史***********************");
			String idCardNum = StringTools.formatStr((String)paraMap.get("idCardNum"));

			//注册品牌
			String brandCode = StringTools.formatStr(paraMap.get("brand"),"");

			Map<String, Object>  result = userApiBussinessService.getUserBaseInfoCountByIdCardNum(idCardNum,brandCode);
			String sucCount = (String) result.get("sucCount");
			if("-1".equals(sucCount)){
				result.put("code", LTResponseCode.US01124);
				return new Response(LTResponseCode.US01124, (String)result.get("msg"));
			}
			result.put("code", LTResponseCode.SUCCESS);
			Response response = new Response(LTResponseCode.SUCCESS, "查询成功", result);
			return response;
		}catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage());
		}
	}
}
