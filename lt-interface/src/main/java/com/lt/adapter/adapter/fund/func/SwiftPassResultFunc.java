package com.lt.adapter.adapter.fund.func;

import com.alibaba.fastjson.JSON;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class SwiftPassResultFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String str = JSON.toJSONString(paraMap);

			Map<String,String> map = (Map<String, String>) JSON.parse(str);

			fundAccountApiService.swiftPassCallback(map);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("威富通支付结果返回调用接口--执行失败!"+e.getMessage());
			return LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
