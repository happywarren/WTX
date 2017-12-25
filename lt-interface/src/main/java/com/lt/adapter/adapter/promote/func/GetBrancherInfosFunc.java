package com.lt.adapter.adapter.promote.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.promote.IPromoteApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 获取下线详情数据
 * @author yuanxin
 *
 */
@Service
public class GetBrancherInfosFunc extends BaseFunction{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IPromoteApiService promoteApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		try{
			String userId = String.valueOf(paraMap.get("userId"));
			response = promoteApiService.getBrancherInfos(userId);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("---------获取下线详情数据异常,e={}",e);
		}
		return response;
	}
}
