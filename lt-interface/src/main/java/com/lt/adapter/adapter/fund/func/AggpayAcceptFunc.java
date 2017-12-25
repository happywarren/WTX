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
 * 聚合接收结果入口
 * 
 * @author yubei
 * @Date 2017年9月6日
 *
 */
@Service
public class AggpayAcceptFunc extends BaseFunction {

	private final static Logger logger = LoggerFactory.getLogger(AggpayAcceptFunc.class);

	@Autowired
	private IUserApiRechargeService userRechargeService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		try {
			paraMap.remove("cmd");
			paraMap.remove("func");
			userRechargeService.userReviceAggpayResponse(paraMap);
		} catch (Exception e) {
			logger.error("聚合支付结果返回调用接口--执行异常!" + e.getMessage());
			return LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
