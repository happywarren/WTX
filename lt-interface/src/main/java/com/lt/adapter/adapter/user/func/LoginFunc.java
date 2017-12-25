package com.lt.adapter.adapter.user.func;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UserLoginFunc   
* 类描述：用户登录  
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午3:09:11      
*/
@Component
public class LoginFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		String loginName = StringTools.formatStr(paraMap.get("loginName"), "");
		/* 用户密码需要在客户端MD5加密  */
		String password = StringTools.formatStr(paraMap.get("password"), "");
		//软件版本
		String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");
		//设备型号
		String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
		//注册IMEI
		String deviceImei = StringTools.formatStr(paraMap.get("deviceImei"), "");
		//设备版本
		String deviceVersion = StringTools.formatStr(paraMap.get("deviceVersion"), "");

		//用户所属-品牌编码
		String brandCode = StringTools.formatStr(paraMap.get("brand"),"");

		String ip = StringTools.formatStr(paraMap.get("ip"), "");
		Response response = null ;
		
		if (!StringTools.isNotEmpty(loginName) || !StringTools.isTele(loginName)) {
			throw new LTException(LTResponseCode.US01102);
		}
		if (!StringTools.isNotEmpty(password)) {
			throw new LTException(LTResponseCode.US01110);
		}

//		//品牌校验
//		if (StringTools.isEmpty(brandCode)){
//			return LTResponseCode.getCode(LTResponseCode.MA00028);
//		}

		UserBaseInfo baseUser = new UserBaseInfo();
		baseUser.setTeleStatus(1);
		baseUser.setTele(loginName);
		baseUser.setPasswd(password);
		baseUser.setVersion(clientVersion);
		baseUser.setDeviceModel(deviceModel);
		baseUser.setDeviceImei(deviceImei);
		baseUser.setDeviceVersion(deviceVersion);
		baseUser.setIp(ip);
		baseUser.setBrandCode(brandCode);

		HashMap<String,Object> map = null ;
		try{
			map = userApiService.userLogin(baseUser);
			if(map.containsKey("count")){
				response = LTResponseCode.getCode(LTResponseCode.US01109, map);
			}else{
				response = LTResponseCode.getCode(LTResponseCode.SUCCESS, map);
			}
		}catch(Exception e){
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage(), map);
		}
		return response;
	}

}
