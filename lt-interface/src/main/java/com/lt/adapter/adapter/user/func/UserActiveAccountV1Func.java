package com.lt.adapter.adapter.user.func;

import java.util.HashMap;
import java.util.Map;

import com.lt.constant.redis.RedisUtil;
import com.lt.model.brand.BrandInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiBussinessV1Service;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserServiceMapper;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* @项目名称：lt-interface   
* @类名称：UserActiveAccountFunc   
* @类描述：用户开户入口
* @创建人：yubei   
* @创建时间：2017年6月3日 下午18:28：00      
*/
@Component
public class UserActiveAccountV1Func extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessV1Service userApiBussinessService;
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		logger.info("paramMap:{}",JSONObject.toJSONString(paraMap));
		
		String step  = StringTools.formatStr(paraMap.get("step"), "");
		String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
		String brandCode = StringTools.formatStr(paraMap.get("brand"), "");

		Response response = null ;
		if(step!=null && !step.equals("")){
			int orderStep = Integer.parseInt(step);
			
			String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
			String ip = StringTools.formatStr(paraMap.get("ip"), "");
			UserUpdateInfoLog updateInfoLog = new UserUpdateInfoLog();
			updateInfoLog.setUserId(userId);
			updateInfoLog.setDevice_model(deviceModel);
			updateInfoLog.setIp(ip);
			
			switch(orderStep){
			case 0:
				HashMap<String,Object> map = userApiBussinessService.userActiveAccount0(userId);
				response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
				break ;
			case 1:
				String userName = StringTools.formatStr(paraMap.get("userName"), "");
				String idCardNum = StringTools.formatStr(paraMap.get("idCardNum"), "");
				if(StringTools.isEmpty(userName) ||StringTools.isEmpty(idCardNum)){
					return LTResponseCode.getCode(LTResponseCode.US03101);
				}else{
					userApiBussinessService.userActiveAccount(updateInfoLog, userName, idCardNum, step,brandCode);
					response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
				}
				break;
			case 2:
				try {
					String url1 = StringTools.formatStr(paraMap.get("picPositive"), "");
					String url2 = StringTools.formatStr(paraMap.get("picReverse"), "");
					
					if (!url1.equals("") && !url2.equals("")) {
						userApiBussinessService.userActiveAccount(updateInfoLog, url1, url2, step,brandCode);
						response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
					} else {
						return LTResponseCode.getCode(LTResponseCode.US03101);
					}
					
				} catch (Exception e) {
					return LTResponseCode.getCode(e.getMessage());
				}
				break;
			case 3:
				String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "");
				String bankNum = StringTools.formatStr(paraMap.get("bankNum"), "");
				
				if(StringTools.isEmpty(bankCode) ||StringTools.isEmpty(bankNum)){
					return LTResponseCode.getCode(LTResponseCode.US03101);
				}else{
					try {
						userApiBussinessService.userActiveAccount3(updateInfoLog, bankCode, bankNum,1);
						response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
					} catch (Exception e) {
						e.printStackTrace();
						return LTResponseCode.getCode(LTResponseCode.US20000);
					}
				}
				break;
			case 4:
				boolean flag = fundAccountApiService.doInitFundCashAccount(userId);
				if(flag){
					UserServiceMapper usServiceMapper = new UserServiceMapper();
					usServiceMapper.setUserId(userId);
					usServiceMapper.setServiceCode(ServiceContant.CASH_SERVICE_CODE);
					userApiBussinessService.activeUserService(usServiceMapper);
					userApiBussinessService.activeUserAccountStatus(userId);
					response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
				}else{
					return LTResponseCode.getCode(LTResponseCode.US03101);
				}
				break;
			}
		}else{
			return LTResponseCode.getCode(LTResponseCode.US03101);
		}
		
		return response;
	}

}
