package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiRechargeService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
/**
 * 钱通支付充值
 * @author jingwb
 *
 */
@Service
public class QianTongPayCallbackFunc extends BaseFunction{

Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserApiRechargeService userRechargeService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			userRechargeService.userReviceQTPayResponse(paraMap);
		}catch(Exception e){
			logger.error("钱通支付充值结果返回调用接口--执行异常!"+e.getMessage());
			return LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}
}
