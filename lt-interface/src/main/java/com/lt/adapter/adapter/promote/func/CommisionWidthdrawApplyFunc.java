package com.lt.adapter.adapter.promote.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.promote.IPromoteApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 佣金提现申请
 * @author jingwb
 *
 */
@Service
public class CommisionWidthdrawApplyFunc extends BaseFunction{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IPromoteApiService promoteApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		try{
			String amount = String.valueOf(paraMap.get("amount"));
			if(StringTools.isEmpty(amount)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002);
			}
			String userId = String.valueOf(paraMap.get("userId"));
			promoteApiService.commisionWidthdrawApply(userId, Double.valueOf(amount));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("---------佣金提现申请异常,e={}",e);
		}
		return response;
	}
}
