package com.lt.adapter.adapter.order.func;

import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.util.error.LTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.model.user.log.UserOperateLog;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 批量平仓
 * @author jingwb
 *
 */
@Service
public class SellAllFunc extends BaseFunction{
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IOrderApiService orderApiService;
	@Autowired
	private IOrderScoreApiService orderScoreApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		//开始时间
		long startTime = System.currentTimeMillis();
		Integer fundType = Integer.valueOf(paraMap.get("fundType").toString());
		String productCode = (String)paraMap.get("productCode");
		String userId = (String)paraMap.get("userId");//用户id
		String ip = (String)paraMap.get("ip");
		
		logger.info("===批量平仓开始 productCode={},userId={}=====", productCode,userId);

		if(StringTools.isEmpty(userId)){
			return LTResponseCode.getCode(LTResponseCode.US01105);
		}			

		
		//资金类型: 0 现金; 1 积分 
		logger.info("校验资金类型:{}", fundType);
		if(FundTypeEnum.CASH.getValue() != fundType && FundTypeEnum.SCORE.getValue() != fundType) {
			return LTResponseCode.getCode(LTResponseCode.TD00003);
		}
		
		// 调用订单服务接口卖出
		logger.info("调用订单服务接口批量平仓");

		//现金订单 和 积分订单分发
		if (FundTypeEnum.CASH.getValue() == fundType) {
			orderApiService.sellAll(fundType, userId, productCode);
		} else if (FundTypeEnum.SCORE.getValue() == fundType) {
			orderScoreApiService.sellAll(fundType, userId, productCode);
		} else {
			throw new LTException(LTResponseCode.TD00003);
		}


		logger.info("批量平仓用时:{}ms", (System.currentTimeMillis() - startTime));
		
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}
}
