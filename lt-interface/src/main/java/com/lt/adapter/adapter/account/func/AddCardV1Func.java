package com.lt.adapter.adapter.account.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessV1Service;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 新增银行卡
 * @author guodw
 *
 */
@Service
public class AddCardV1Func extends BaseFunction {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserApiBussinessV1Service userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String userId = StringTools.formatStr(paraMap.get("userId"),"");		
			String cardNum = StringTools.formatStr(paraMap.get("cardNum"),"");		
			String bankCode = StringTools.formatStr(paraMap.get("bankCode"),"");		
			String ip = StringTools.formatStr(paraMap.get("ip"), "");
			String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
			if(StringTools.isNotEmpty(bankCode)&&StringTools.isNotEmpty(cardNum)){
				UserUpdateInfoLog updateInfoLog = new UserUpdateInfoLog();
				updateInfoLog.setUserId(userId);
				updateInfoLog.setDevice_model(deviceModel);
				updateInfoLog.setIp(ip);
				if(StringTools.isEmpty(bankCode) ||StringTools.isEmpty(cardNum)){
					return LTResponseCode.getCode(LTResponseCode.US03101);
				}else{
					userApiBussinessService.userActiveAccount3(updateInfoLog, bankCode, cardNum,0);
					Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
					return response;	
				}
			}else{
				throw new LTException(LTResponseCode.US03101);
			}
			
		}catch(Exception e){
			logger.error("获取账户信息--执行失败，e={}",e);
			throw new LTException(e.getMessage());
		}
		
	}
}
