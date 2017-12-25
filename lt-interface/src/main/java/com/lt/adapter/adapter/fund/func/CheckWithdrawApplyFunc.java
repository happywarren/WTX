package com.lt.adapter.adapter.fund.func;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Service
public class CheckWithdrawApplyFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IOrderApiService orderApiService;
	
	@Autowired
	private IUserApiService userApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {

		String userId = String.valueOf(paraMap.get("userId"));
		Response  resp = null;
		try{
			//获取用户是否已经交易过
			Integer isTrade = orderApiService.queryOrdersCounts(userId);
			//获取用户是否已实名认证
			Integer useStatus = userApiService.getCertificationStatus(userId);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("isTrade", isTrade);
			map.put("useStatus", useStatus);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS, map);
		}catch(Exception e){
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		return resp;

	}
}