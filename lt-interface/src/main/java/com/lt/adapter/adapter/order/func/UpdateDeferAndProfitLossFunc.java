package com.lt.adapter.adapter.order.func;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lt.api.trade.IOrderScoreApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 修改递延止盈止损 
 * @author jingwb
 *
 */
@Service
public class UpdateDeferAndProfitLossFunc extends BaseFunction{
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IOrderApiService orderApiService;
	@Autowired
	private IOrderScoreApiService orderScoreApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null ;
		// 订单id
		String orderIdStr = (String)paraMap.get("orderId");
		// 修改递延操作
		String deferStatuStr = (String)paraMap.get("deferStatu");
		// 止损金额
		String stopLossStr = (String)paraMap.get("stopLossStr");
		// 止盈金额
		String stopProfitStr = (String)paraMap.get("stopProfitStr");
		// 类型：1积分，0现金
		String type = (String)paraMap.get("type");
		
		String trailStopLoss = (String)paraMap.get("trailStopLoss");
		String userId = (String)paraMap.get("userId");//用户id
		String ip = (String)paraMap.get("ip");
		OrderLossProfitDefLog log = new OrderLossProfitDefLog();
		log.setUserId(userId);
		if(type == null || type.equals("") || orderIdStr == null || orderIdStr.equals("")){
			throw new LTException(LTResponseCode.US03101);
		}
			
		Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
		Matcher matcher = null ;
		
		if(stopProfitStr !=null && !stopProfitStr.equals("")){
			matcher = pattern.matcher(stopProfitStr);
			if(!matcher.matches()){
				throw new LTException(LTResponseCode.US03101);
			}
		}else{
			stopProfitStr = "";
		}
			
		if(stopLossStr !=null && !stopLossStr.equals("")){
			matcher = pattern.matcher(stopLossStr);
			if(!matcher.matches()){
				throw new LTException(LTResponseCode.US03101);
			}
		}else{
			stopLossStr = "";
		}
			
		pattern = Pattern.compile("[0-1]");
		if(StringTools.isNotEmpty(type)){
			matcher = pattern.matcher(type);
			if(!matcher.matches()){
				throw new LTException(LTResponseCode.US03101);
			}
		}else{
			type = FundTypeEnum.CASH.getValue()+"";
		}
			
		if(StringTools.isNotEmpty(deferStatuStr)){
			matcher = pattern.matcher(deferStatuStr);
			if(!matcher.matches()){
				throw new LTException(LTResponseCode.US03101);
			}
		}else{
			deferStatuStr = "";
		}
			
		if(StringTools.isNotEmpty(trailStopLoss)){
			matcher = pattern.matcher(trailStopLoss);
			if(!matcher.matches()){
				throw new LTException(LTResponseCode.US03101);
			}
		}else{
			trailStopLoss = "";
		}
			
		if(FundTypeEnum.CASH.getValue() == Integer.parseInt(type)){
				orderApiService.doUpdateCashDefAndLossProfit(log, orderIdStr, deferStatuStr, stopLossStr, stopProfitStr,trailStopLoss);
		}else{
			orderScoreApiService.doUpdateScoreDefAndLossProfit(log, orderIdStr, deferStatuStr, stopLossStr, stopProfitStr, trailStopLoss);
		}
			
		response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		
		return response;
	}
}
