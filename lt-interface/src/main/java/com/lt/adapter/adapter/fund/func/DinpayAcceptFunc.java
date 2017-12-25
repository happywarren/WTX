package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

@Service
public class DinpayAcceptFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			fundAccountApiService.dinPayAccept(paraMap);
		}catch(Exception e){
			logger.error("智付支付结果返回调用接口--执行失败!"+e.getMessage());
			return LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
