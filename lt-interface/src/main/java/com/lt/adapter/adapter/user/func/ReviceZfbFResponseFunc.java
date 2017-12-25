package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiRechargeService;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：ReviceZfbFResponseFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月18日 上午9:47:41      
*/
@Component
public class ReviceZfbFResponseFunc extends BaseFunction {
	
	@Autowired
	private IUserApiRechargeService userRechargeServiceImpl ;

	@Override
	public Response response(Map<String, Object> paraMap) {
		userRechargeServiceImpl.userReviceZfbFResponse(paraMap);
		return null;
	}
}
