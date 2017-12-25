package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UpdateUserInfoFunc   
* 类描述：用户修改信息   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午3:42:21      
*/
@Component
public class UpdateUserInfoFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String uptType = StringTools.formatStr(paraMap.get("updateType"), "");
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		String deviceModel =  StringTools.formatStr(paraMap.get("deviceModel"), "");
		
		String nick_name = StringTools.formatStr(paraMap.get("nick_name"), "");
		//String personSign = StringTools.formatStr(paraMap.get("personSign"), "");
		String ip =  StringTools.formatStr(paraMap.get("ip"), "");
		
		
		UserBaseInfo baseInfo = new UserBaseInfo();
		baseInfo.setUserId(userId);
		baseInfo.setDeviceModel(deviceModel);
		baseInfo.setIp(ip);
		
		if(StringTools.isEmpty(uptType)){
			Response resp = LTResponseCode.getCode(LTResponseCode.US03101);
			return resp;
		}
		
		if(StringTools.isNotEmpty(nick_name)){
			baseInfo.setNickName(nick_name);
		}
		
		//if(StringTools.isNotEmpty(personSign)){
		//	baseInfo.setPersonalSign(personSign);
		//}
		if (paraMap.get("personSign")!=null) {
			baseInfo.setPersonalSign((String)paraMap.get("personSign"));
		}
		logger.debug("调用修改用户信息方法");
		baseInfo = userApiService.updateUserNickAndSign(baseInfo, Integer.valueOf(uptType));
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,baseInfo);

	}

}
