package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.FileUploadAdapterServlet;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserServiceMapper;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.speedycloud.SpeedyCloudUtils;

/**   
* 项目名称：lt-interface   
* 类名称：UserActiveAccountFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:17:34      
*/
@Component
public class UserActiveAccountFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		logger.info("paramMap:{}",JSONObject.toJSONString(paraMap));
		String recordVersion = StringTools.formatStr(paraMap.get("recordVersion"), "");
		String recordIP = StringTools.formatStr(paraMap.get("recordIP"), "");
		String recordLoginMode = StringTools.formatStr(paraMap.get("recordLoginMode"), "");
		String recordImei = StringTools.formatStr(paraMap.get("recordImei"), "");
		String recordDevice = StringTools.formatStr(paraMap.get("recordDevice"), "");
		String recordAccessMode = StringTools.formatStr(paraMap.get("recordAccessMode"), "");
		String recordCarrierOperator = StringTools.formatStr(paraMap.get("recordCarrierOperator"), "");
		String step  = StringTools.formatStr(paraMap.get("step"), "");
		String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
		String operateType = StringTools.formatStr(paraMap.get("operateType"),"");
		Response response = null ;
		if(step==null || ("").equals(step)){
			throw new LTException(LTResponseCode.US01120);
		}else{
			int orderStep = Integer.parseInt(step);
			
			String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
			String ip = StringTools.formatStr(paraMap.get("ip"), "");
			UserUpdateInfoLog updateInfoLog = new UserUpdateInfoLog();
			updateInfoLog.setUserId(userId);
			updateInfoLog.setDevice_model(deviceModel);
			updateInfoLog.setIp(ip);
			updateInfoLog.setRecordAccessMode(recordAccessMode);
			updateInfoLog.setRecordCarrierOperator(recordCarrierOperator);
//			updateInfoLog.setRecordDevice(recordDevice);
//			updateInfoLog.setRecordImei(recordImei);
//			updateInfoLog.setRecordIP(recordIP);
//			updateInfoLog.setRecordLoginMode(recordLoginMode);
//			updateInfoLog.setRecordVersion(recordVersion);
			//operate=0准备验证，后台保存操作日志
			if("0".equals(operateType) && "1".equals(step)){
				userApiBussinessService.saveOpenLog(updateInfoLog, step);
				response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
				return response;
			}
			switch(orderStep){
			case 1:
				String userName = StringTools.formatStr(paraMap.get("userName"), "");
				String idCardNum = StringTools.formatStr(paraMap.get("idCardNum"), "");
				String picPositive = StringTools.formatStr(paraMap.get("picPositive"), "");
				String picReverse = StringTools.formatStr(paraMap.get("PicReverse"), "");
				String facePic = StringTools.formatStr(paraMap.get("facePic"),"");
				
				
				if(StringTools.isEmpty(userName) ||StringTools.isEmpty(idCardNum)){
					return LTResponseCode.getCode(LTResponseCode.US03101);
				}else{
					try {
						synchronized (this) {
							//保存图片
							//String icCardPaht1 = FileUploadAdapterServlet.downLoadFromUrl(picPositive);
							//String icCardPaht2 = FileUploadAdapterServlet.downLoadFromUrl(picReverse);
							//String facePath = FileUploadAdapterServlet.downLoadFromUrl(facePic);
							
							Map<String, Object> resultMap =  SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl(picPositive);
							String resultId = (String)resultMap.get("resultId");
							if("SUCCESS".equals(resultId)){
								picPositive = (String) resultMap.get("filePath");
							}else{
								throw new LTException(LTResponseCode.US01122);
							}
							resultMap =  SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl(picReverse);
							resultId = (String)resultMap.get("resultId");
							if("SUCCESS".equals(resultId)){
								picReverse = (String) resultMap.get("filePath");
							}else{
								throw new LTException(LTResponseCode.US01122);
							}
							resultMap = SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl(facePic);
							resultId = (String)resultMap.get("resultId");
							if("SUCCESS".equals(resultId)){
								facePic = (String) resultMap.get("filePath");
							}else{
								throw new LTException(LTResponseCode.US01122);
							}	
							
							userApiBussinessService.userActiveAccount1(updateInfoLog, userName, idCardNum, picPositive, picReverse, facePic);
							response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.debug(e.getMessage());
						return LTResponseCode.getCode(LTResponseCode.ER400);
					}
				}
				break;
			case 2:
				String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "");
				String bankNum = StringTools.formatStr(paraMap.get("bankNum"), "").replaceAll(" ", "");
				try {
					if (!bankCode.equals("") && !bankNum.equals("")) {
						Map<String,Object> result = userApiBussinessService.userActiveAccount2(updateInfoLog, bankCode, bankNum, 1);
						response = new Response(LTResponseCode.SUCCESS, "成功", result);
					} else {
						return LTResponseCode.getCode(LTResponseCode.US03101);
					}
					
				} catch (Exception e) {
					return LTResponseCode.getCode(e.getMessage());
				}
				break;
			case 3:
				String riskRet = StringTools.formatStr(paraMap.get("riskRet"),"");
				String riskLevel = StringTools.formatStr(paraMap.get("riskLevel"),"");
				try {
					userApiBussinessService.userActiveAccount3(updateInfoLog,riskRet,riskLevel);
					response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					return LTResponseCode.getCode(LTResponseCode.US20000);
				}
				break;
			case 4:
				String signPic = StringTools.formatStr(paraMap.get("signPic"),"");
				try {
					userApiBussinessService.userActiveAccount4(updateInfoLog, signPic);
					response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					return LTResponseCode.getCode(LTResponseCode.US01122);
				}
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
/*			case 5:
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
*/			}
		}/*else{
			return LTResponseCode.getCode(LTResponseCode.US03101);
		}*/
		
		return response;
	}
	public static void main(String[] args) {
		
	}
}
