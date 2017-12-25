package com.lt.adapter.adapter.user.func;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：FindUserPwdFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午4:57:07      
*/
@Component
public class FindUserPwdFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {

		//用户手机号
		String tele = StringTools.formatStr(paraMap.get("tele"), "");
		//密码
		String password = StringTools.formatStr(paraMap.get("password"), "");
		//软件版本
		String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");
		//设备型号
		String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
		//注册IMEI
		String deviceImei = StringTools.formatStr(paraMap.get("deviceImei"), "");
		//设备版本
		String deviceVersion = StringTools.formatStr(paraMap.get("deviceVersion"), "");
		// 验证码
		String authCode = StringTools.formatStr(paraMap.get("auth_code"), "");

		//注册品牌
		String brandCode = StringTools.formatStr(paraMap.get("brand"),"");

		String ip = StringTools.formatStr(paraMap.get("ip"), "");

		UserBaseInfo userBaseInfo  = new UserBaseInfo();
		userBaseInfo.setTele(tele);
		userBaseInfo.setPasswd(Md5Encrypter.MD5(password));
		userBaseInfo.setVersion(clientVersion);
		userBaseInfo.setDeviceModel(deviceModel);
		userBaseInfo.setDeviceImei(deviceImei);
		userBaseInfo.setDeviceVersion(deviceVersion);
		userBaseInfo.setIp(ip);
		userBaseInfo.setBrandCode(brandCode);
		if (!StringTools.isNotEmpty(password)) {
			return LTResponseCode.getCode(LTResponseCode.US01110);
		}
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,20}");
		boolean flag = pattern.matcher(password).matches();
		if (!flag) {
			return LTResponseCode.getCode(LTResponseCode.US01111);
		}
		
		HashMap<String,Object> map = userApiService.findUserPassword(userBaseInfo,authCode);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
	}

}
