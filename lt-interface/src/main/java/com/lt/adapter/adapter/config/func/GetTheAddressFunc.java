package com.lt.adapter.adapter.config.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.api.user.IUserApiService;
import com.lt.model.sys.QuotaHostBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.adapter.utils.ConfigUtils;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Service
public class GetTheAddressFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserApiService userApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			Map<String,String> map = new HashMap<String, String>();
			String ip = ConfigUtils.getValue("SERVICE.CONFIG.IP");
			String port = ConfigUtils.getValue("SERVICE.CONFIG.PORT");
			List<QuotaHostBean> list = userApiService.findClientQuotaHost();
			if(StringTools.isNotEmpty(list)&&list.size()>0){
				ip = list.get(0).getHost();
				port = list.get(0).getPort()+"";
			}
			map.put("ip", ip);
			map.put("port", port);
			Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
			response.setIp(ip);
			response.setPort(port);
			return response;

		}catch(Exception e){
			logger.error("获取行情地址接口--异步执行失败，e={}",e);
			throw new LTException(LTResponseCode.US00004);
		}
		
	}

}
