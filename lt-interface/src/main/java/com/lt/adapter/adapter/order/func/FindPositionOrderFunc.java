package com.lt.adapter.adapter.order.func;

import java.util.List;
import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.fund.FundTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import com.lt.vo.trade.PositionOrderVo;

/**
 * 用户持仓订单
 * @author jingwb
 *
 */

@Service
public class FindPositionOrderFunc extends BaseFunction{
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IOrderApiService orderApiService;

	@Autowired
	private IOrderScoreApiService orderScoreApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Integer fundType = Integer.parseInt(paraMap.get("fundType").toString());
		String userId = (String)paraMap.get("userId");//用户id
		List<PositionOrderVo> data = null;

		if (fundType == FundTypeEnum.SCORE.getValue()) {
			data = orderScoreApiService.findPositionOrderByUserId(userId, fundType);
		}else if(fundType == FundTypeEnum.CASH.getValue()){
			data = orderApiService.findPositionOrderByUserId(userId, fundType);
		}

		logger.info("=============data={}==========",JSONObject.toJSONString(data));
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
