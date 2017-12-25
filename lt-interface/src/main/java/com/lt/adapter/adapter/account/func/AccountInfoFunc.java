package com.lt.adapter.adapter.account.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.UserAccountInfoVo;

/**
 * 获取账户信息
 * @author guodw
 *
 */
@Service
public class AccountInfoFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String userId = String.valueOf(paraMap.get("userId"));		
			Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			UserAccountInfoVo info = userApiBussinessService.getUserAccountInfo(userId);
			response.setData(info);
			return response;	
		}catch(Exception e){
			logger.error("获取账户信息--执行失败，e={}",e);
			throw new LTException(e.getMessage());
		}
		
	}
}