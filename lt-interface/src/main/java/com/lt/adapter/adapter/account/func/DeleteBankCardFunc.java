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
 * 删除银行卡
 * @author guodw
 *
 */
@Service
public class DeleteBankCardFunc extends BaseFunction {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String userId = String.valueOf(paraMap.get("userId"));		
			int id = StringTools.formatInt(paraMap.get("id"),0);		
			String ip = StringTools.formatStr(paraMap.get("ip"), "");
			String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
			if( id > 0 && userId != null ){
				UserUpdateInfoLog updateInfoLog = new UserUpdateInfoLog();
				updateInfoLog.setUserId(userId+"");
				updateInfoLog.setDevice_model(deviceModel);
				updateInfoLog.setIp(ip);
				userApiBussinessService.deleteBankcard(updateInfoLog, id, userId);
				Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
				return response;	
			}else{
				throw new LTException(LTResponseCode.US03101);
			}
			
		}catch(Exception e){
			logger.error("删除银行卡--执行失败，e={}",e);
			throw new LTException(e.getMessage());
		}
		
	}
}
