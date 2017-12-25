package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

@Service
public class CallbackForUnspayFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			fundAccountApiService.callbackForUnspay(paraMap);
		}catch(Exception e){
			logger.error("银生宝充值返回调用接口--异步执行失败，e={}",e);
			throw new LTException(LTResponseCode.UNSPAY_ERROR_00001);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
