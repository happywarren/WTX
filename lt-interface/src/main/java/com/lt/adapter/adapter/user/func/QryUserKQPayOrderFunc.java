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
* 类名称：QryUserKQPayOrder   
* 类描述： 查询用户快钱充值记录  
* 创建人：yuanxin   
* 创建时间：2017年4月5日 下午3:00:00      
*/
@Component
public class QryUserKQPayOrderFunc extends BaseFunction {
	@Autowired
	private IUserRechargeService userRechargeServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Map<String,Object> resultMap = userRechargeServiceImpl.qryKQuserPayOrder(paraMap);
		return new Response(LTResponseCode.SUCCESS, "处理成功", resultMap);
	}
}
