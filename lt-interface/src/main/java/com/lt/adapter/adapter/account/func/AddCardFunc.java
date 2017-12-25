package com.lt.adapter.adapter.account.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
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
public class AddCardFunc extends BaseFunction {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String userId = StringTools.formatStr(paraMap.get("userId"),"");		
			String cardNum = StringTools.formatStr(paraMap.get("cardNum"),"");		
			String bankCode = StringTools.formatStr(paraMap.get("bankCode"),"");		
			String ip = StringTools.formatStr(paraMap.get("ip"), "");
			String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
			String recordVersion = StringTools.formatStr(paraMap.get("recordVersion"), "");
			String recordIP = StringTools.formatStr(paraMap.get("recordIP"), "");
			String recordLoginMode = StringTools.formatStr(paraMap.get("recordLoginMode"), "");
			String recordImei = StringTools.formatStr(paraMap.get("recordImei"), "");
			String recordDevice = StringTools.formatStr(paraMap.get("recordDevice"), "");
			String recordAccessMode = StringTools.formatStr(paraMap.get("recordAccessMode"), "");
			String recordCarrierOperator = StringTools.formatStr(paraMap.get("recordCarrierOperator"), "");
			
			if(StringTools.isNotEmpty(bankCode)&&StringTools.isNotEmpty(cardNum)){
				UserUpdateInfoLog updateInfoLog = new UserUpdateInfoLog();
				updateInfoLog.setUserId(userId);
				updateInfoLog.setDevice_model(deviceModel);
				updateInfoLog.setIp(ip);
				updateInfoLog.setRecordAccessMode(recordAccessMode);
				updateInfoLog.setRecordCarrierOperator(recordCarrierOperator);
//				updateInfoLog.setRecordDevice(recordDevice);
//				updateInfoLog.setRecordImei(recordImei);
//				updateInfoLog.setRecordIP(recordIP);
//				updateInfoLog.setRecordLoginMode(recordLoginMode);
//				updateInfoLog.setRecordVersion(recordVersion);
				if(StringTools.isEmpty(bankCode) ||StringTools.isEmpty(cardNum)){
					return LTResponseCode.getCode(LTResponseCode.US03101);
				}else{
					Map<String, Object> resultMap = userApiBussinessService.userActiveAccount2(updateInfoLog, bankCode, cardNum,0);
					String resultId = (String) resultMap.get("resultId");
					//绑定银行卡不限次数
					String openingCnt = (String)resultMap.get("openingCnt");
					if("0".equals(openingCnt)){
						throw new LTException(LTResponseCode.US01123);
					}
					if("1".equals(resultId)){
						Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS,resultMap);
						return response;
					}else{
						Response response = LTResponseCode.getCode(LTResponseCode.US01121,resultMap);
						return response;
					}
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
