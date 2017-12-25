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
 * 熙大支付宝接收结果入口
 * @author yubei
 * @Date 2017年8月8日
 *
 */
@Service
public class XdPayAcceptFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserApiRechargeService userRechargeService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			userRechargeService.userReviceXDPayResponse(paraMap);
		}catch(Exception e){
			logger.error("熙大支付宝支付结果返回调用接口--执行异常!"+e.getMessage());
			return LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
