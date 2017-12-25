package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserRechargeService;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：ReviceKQResponse   
* 类描述：接收快钱回调接口   
* 创建人：yuanxin   
* 创建时间：2017年4月20日 下午4:15:36      
*/
@Component
public class ReviceKQResponseFunc extends BaseFunction {
	
	@Autowired
	private IUserRechargeService  userRechargeServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		userRechargeServiceImpl.reviceKQResponse(paraMap);
		return null;
	}
}
