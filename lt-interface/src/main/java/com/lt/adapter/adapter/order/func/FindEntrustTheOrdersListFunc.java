package com.lt.adapter.adapter.order.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import com.lt.vo.trade.EntrustVo;

/**
 * 查询用户委托中订单
 * @author jingwb
 *
 */
@Service
public class FindEntrustTheOrdersListFunc extends BaseFunction{
	@Autowired
	private IOrderApiService orderApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String userId = (String)paraMap.get("userId");//用户id
		List<EntrustVo> data = null;
		data = orderApiService.findEntrustTheOrdersList(userId);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
	}
}
