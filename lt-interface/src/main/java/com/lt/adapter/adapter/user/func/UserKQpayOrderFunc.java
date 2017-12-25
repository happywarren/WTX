package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserRechargeService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UserKQpayOrder   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月5日 下午1:34:52      
*/
@Component
public class UserKQpayOrderFunc extends BaseFunction {
	
	@Autowired
	private IUserRechargeService userRechargeServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		Map<String,String> map = userRechargeServiceImpl.userKQPayOrder(paraMap);
		if(map == null ){
			return new Response(LTResponseCode.SUCCESS, "充值成功");
		}
		
		Response response = new Response(LTResponseCode.FUY00008, map.get("msg") == null || map.get("msg").equals("") ?"快钱程序异常":map.get("msg"),map);
		return response;
	}
}
