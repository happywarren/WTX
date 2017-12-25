package com.lt.adapter.adapter.user.func;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.lt.model.user.Channel;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserContant;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：RegisterFunc   
* 类描述：用户注册   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午3:36:53      
*/
@Component
public class RegisterFunc extends BaseFunction {
	
	public Map<String,Date> currentMap = new ConcurrentHashMap<String, Date>();
	
	@Autowired
	private IUserApiService userApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		/** 手机号码*/
		String tele = StringTools.formatStr(paraMap.get("tele"), "");
		/** 注册验证码*/
		String regCode = StringTools.formatStr(paraMap.get("reg_code"), "");
		/* 用户关键信息 */
		String password = StringTools.formatStr(paraMap.get("password"), "");
		//注册品牌
		String brandCode = StringTools.formatStr(paraMap.get("brand"),"");
		//注册渠道
		String regSource = StringTools.formatStr(paraMap.get("regSource"), "");
		//软件版本
		String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");
		//设备型号
		String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
		//注册IMEI
		String deviceImei = StringTools.formatStr(paraMap.get("deviceImei"), "");
		//设备版本
		String deviceVersion = StringTools.formatStr(paraMap.get("deviceVersion"), "");
		//运营商
		String carrieroperator = StringTools.formatStr(paraMap.get("carrieroperator"), "");
		// 系统名称
		String systemName = StringTools.formatStr(paraMap.get("systemName"), "-1");
		
		String ip = StringTools.formatStr(paraMap.get("ip"), "");
		
		String recordVersion = StringTools.formatStr(paraMap.get("recordVersion"), "");
		String recordIP = StringTools.formatStr(paraMap.get("recordIP"), "");
		String recordLoginMode = StringTools.formatStr(paraMap.get("recordLoginMode"), "");
		String recordImei = StringTools.formatStr(paraMap.get("recordImei"), "");
		String recordDevice = StringTools.formatStr(paraMap.get("recordDevice"), "");
		String recordAccessMode = StringTools.formatStr(paraMap.get("recordAccessMode"), "");
		String recordCarrierOperator = StringTools.formatStr(paraMap.get("recordCarrierOperator"), "");
		String deviceType = StringTools.formatStr(paraMap.get("deviceType"), "");
		Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS) ;

//		//品牌
//		if (StringTools.isEmpty(brandCode)){
//			return LTResponseCode.getCode(LTResponseCode.MA00028);
//		}
		
		if (!StringTools.isNotEmpty(password)) {
			return LTResponseCode.getCode(LTResponseCode.US01110);
		}
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,20}");
		boolean flag = pattern.matcher(password).matches();
		if (!flag) {
			return LTResponseCode.getCode(LTResponseCode.US01111);
		}
		
		if(currentMap.containsKey(tele+brandCode)){
			Date date = new Date();
			Date operateDate = currentMap.get(tele+brandCode);
			// 设定五分钟账号处理时间，防止系统异常需要重启服务
			if(DateTools.addMinute(operateDate, 5).before(date)){
				currentMap.remove(tele+brandCode);
			}else{
				return LTResponseCode.getCode(LTResponseCode.US01112);
			}
		}
		
		currentMap.put(tele+brandCode, new Date());
		password = Md5Encrypter.MD5(password);

		
		UserBaseInfo baseUser = new UserBaseInfo();
		baseUser.setTeleStatus(1);
		baseUser.setTele(tele);
		baseUser.setPasswd(password);
		//baseUser.setNickName(StringTools.randomNickName(Integer.parseInt(tele.substring(6)), 0));
		//baseUser.setNickName(CodeCreate.createNickName(6));
		baseUser.setIp(ip);
		baseUser.setLastLoginImei(deviceImei);
		baseUser.setDeviceModel(deviceModel);
		baseUser.setDeviceImei(deviceImei);
		baseUser.setDeviceVersion(deviceVersion);
		baseUser.setRecordAccessMode(recordAccessMode);
		baseUser.setRecordCarrierOperator(recordCarrierOperator);
		baseUser.setRecordDevice(recordDevice);
		baseUser.setRecordImei(recordImei);
		baseUser.setRecordIP(recordIP);
		baseUser.setRecordLoginMode(recordLoginMode);
		baseUser.setRecordVersion(recordVersion);
		if (StringTools.isNumeric(regSource)) {
			baseUser.setRegSource(Integer.valueOf(regSource));
		}
		baseUser.setVersion(clientVersion);
		baseUser.setRegMode(UserContant.USER_REGMODE_PHONE);
		baseUser.setDeviceModel(deviceModel);
		//baseUser.setPromoteId(promoteCode ==null|| promoteCode.equals("") ? null :Integer.parseInt(promoteCode));
//		baseUser.setNickName("用户"+tele.substring(tele.length() - 4, tele.length()));
		baseUser.setRegCarrieroperator(recordCarrierOperator);
		baseUser.setSystemName(Integer.parseInt(systemName));
		baseUser.setBrandCode(brandCode);
		String channelCode = userApiService.getChannelCode(regSource,deviceType);
		if (StringTools.isNotEmpty(channelCode)){
			baseUser.setRegSource(Integer.valueOf(channelCode));
		}
		HashMap<String,Object> map = userApiService.userRegister(baseUser,regCode);
		response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
	
		currentMap.remove(tele+brandCode);
		return response;
	}

}
