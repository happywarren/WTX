package com.lt.adapter.adapter.config.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Service
public class RulesOfTransactionsFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IProductApiService productApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String shortCode = StringTools.formatStr(paraMap.get("shortCode"), "");
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,productApiService.select(shortCode));
		}catch(Exception e){
			logger.error("获取交易规则接口--异步执行失败，e={}",e);
			throw new LTException(LTResponseCode.US00004);
		}
		
	}

}