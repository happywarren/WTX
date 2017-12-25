package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 查询用户出入金限额信息
 * @author yubei
 *
 */
@Component
public class GetUserFundInOutLimitInfoFunc extends BaseFunction {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			logger.info("***************** 查询用户出入金限额信息**********************");
			Map<String, Object>  result = userApiBussinessService.getUserFundInOutLimitInfo();
			Response response = new Response(LTResponseCode.SUCCESS, "查询成功", result);
			logger.info("result:{}"+result.toString());
			return response;
		}catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage());
		}
	}

}
