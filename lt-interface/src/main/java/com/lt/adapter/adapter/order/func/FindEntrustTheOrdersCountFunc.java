package com.lt.adapter.adapter.order.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 查询用户委托中订单个数
 * @author jingwb
 *
 */
@Service
public class FindEntrustTheOrdersCountFunc extends BaseFunction{
	@Autowired
	private IOrderApiService orderApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		int data = 0;
		String userId = (String)paraMap.get("userId");//用户id
		data = orderApiService.findEntrustTheOrdersCount(userId);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
	}
}
