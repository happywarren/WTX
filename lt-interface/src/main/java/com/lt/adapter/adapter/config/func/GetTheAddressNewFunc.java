package com.lt.adapter.adapter.config.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.adapter.utils.ConfigUtils;
import com.lt.api.user.IUserApiService;
import com.lt.model.sys.QuotaHostBean;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetTheAddressNewFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserApiService userApiService;
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{

			List<QuotaHostBean> list = userApiService.findClientQuotaHost();
			Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
			return response;
		}catch(Exception e){
			logger.error("获取行情地址接口--异步执行失败，e={}",e);
			throw new LTException(LTResponseCode.US00004);
		}
		
	}

}
