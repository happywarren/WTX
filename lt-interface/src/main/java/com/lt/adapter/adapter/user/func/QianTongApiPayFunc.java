package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import com.lt.api.user.charge.IUserApiRechargeService;

@Service
public class QianTongApiPayFunc extends BaseFunction{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserApiRechargeService userApiRechargeService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		String checkCode = String.valueOf(paraMap.get("checkCode"));	//验证码
		String payOrderId = String.valueOf(paraMap.get("payOrderId"));//支付id
	
		Map<String, Object> map = userApiRechargeService.qianTongPayConfirm(checkCode,payOrderId);
		response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
		return response;
	}
}
