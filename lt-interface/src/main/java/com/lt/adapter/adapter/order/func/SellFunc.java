package com.lt.adapter.adapter.order.func;

import java.util.Date;
import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.api.user.IUserApiLogService;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.OrderLossProfitDefLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.SellTriggerTypeEnum;
import com.lt.model.user.log.UserOperateLog;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.trade.OrderVo;
/**
 * 用户卖
 * @author jingwb
 *
 */
@Service
public class SellFunc extends BaseFunction{
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IOrderApiService orderApiService;
	@Autowired
	private IOrderScoreApiService orderScoreApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		//开始时间
		long startTime = System.currentTimeMillis();
		
		String displayId = (String)paraMap.get("orderId");
		String orderTime = (String)paraMap.get("orderTime");//用户下单时间
		Integer fundType = Integer.valueOf(paraMap.get("fundType").toString());
		Double userSalePrice = Double.valueOf(paraMap.get("userSalePrice").toString()) ;
		String userId = (String)paraMap.get("userId");//用户id
		String ip = (String)paraMap.get("ip");

		logger.info("=====用户平仓开始 displayId={},userId={}========", displayId,userId);

		if(StringTools.isEmpty(userId)){
			return LTResponseCode.getCode(LTResponseCode.US01105);
		}
		
		//订单不存在 
		logger.info("校验订单displayId:{}", displayId);
		if(displayId == null) {
			throw new LTException(LTResponseCode.TD00004);
		}
					
		//资金类型: 0 现金; 1 积分
		logger.info("校验资金类型:{}", fundType);
		if(FundTypeEnum.CASH.getValue() != fundType && FundTypeEnum.SCORE.getValue() != fundType) {
			return LTResponseCode.getCode(LTResponseCode.TD00003);
		}
		
		// 订单卖出参数
		OrderVo orderVo = new OrderVo(displayId, fundType, SellTriggerTypeEnum.CUSTOMER.getValue(), 
				userId, userSalePrice, new Date(startTime));
		orderVo.setOrderTime(orderTime);
		
		// 调用订单服务接口卖出
		logger.info("调用订单服务接口平仓");

		//现金订单 和 积分订单分发
		if (FundTypeEnum.CASH.getValue() == fundType) {
			orderApiService.sell(orderVo);
		} else if (FundTypeEnum.SCORE.getValue() == fundType) {
			orderScoreApiService.sell(orderVo);
		} else {
			throw new LTException(LTResponseCode.TD00003);
		}

		logger.info("平仓用时:{}ms", (System.currentTimeMillis() - startTime));

		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}
}
