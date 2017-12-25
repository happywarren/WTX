package com.lt.adapter.adapter.order.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.fund.FundTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 查询用户持仓订单数
 * @author jingwb
 *
 */
@Service
public class FindPosiOrderCountFunc extends BaseFunction{
	@Autowired
	private IOrderApiService orderApiService;

	@Autowired
	private IOrderScoreApiService orderScoreApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Integer fundType = Integer.valueOf(paraMap.get("fundType").toString());
		String userId = (String)paraMap.get("userId");//用户id
		Map<String,Map<String,Object>> data = null;

		if (fundType == FundTypeEnum.SCORE.getValue()) {
			data = orderScoreApiService.findPosiOrderCount(userId, fundType);
		}else if(fundType == FundTypeEnum.CASH.getValue()){
			data = orderApiService.findPosiOrderCount(userId, fundType);
		}

		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(data.values());
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,list);		
	}
}
