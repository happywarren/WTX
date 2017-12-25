package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xpath.operations.Lte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 项目名称：lt-interface 类名称：RechargeByDinpayFunc 类描述：智付充值业务登记 创建人：喻贝 创建时间：2017年6月2日
 * 下午17：03：22
 */
@Component
public class RechargeByDinpayFunc extends BaseFunction {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountServiceImpl;

	@Autowired
	private IProductApiService productApiServiceImpl;

	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = new Response(LTResponseCode.SUCCESS, "登记成功");
		
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		//Double rate = 1.11;
		String userId = StringTools.formatStr(paraMap.get("userId"),"");
		String clientType = StringTools.formatStr(paraMap.get("clientType"),"");
		String usdAmt = StringTools.formatStr(paraMap.get("usdAmt"),"");
		String cnyAmt = StringTools.formatStr(paraMap.get("cnyAmt"),"");
		String bankId = StringTools.formatStr(paraMap.get("bankId"),"");
		if(StringTools.isEmpty(clientType)){
			logger.error("【智付充值，请求端类型为空！】");
			throw new LTException(LTResponseCode.FU00000);
		}
		if(rate == null ){
			logger.error("【智付充值，查询汇率为空！】");
			throw new LTException(LTResponseCode.FU00000);
		}
		if (StringTools.isEmpty(userId)) {
			logger.error("【智付充值，用户为空！】");
			throw new LTException(LTResponseCode.FU00000);
		}
		if (StringTools.isEmpty(cnyAmt) || StringTools.isEmpty(usdAmt)) {
			logger.error("【智付充值，金额为空！】");
			return new Response(LTResponseCode.ER400, "入金金额异常,请重试");
		}
		if(Double.parseDouble(usdAmt)<=0){
			logger.info("【智付充值，金额为:{}！】"+usdAmt);
			return new Response(LTResponseCode.ER400, "入金金额异常,请重试");
		}
		
		paraMap.put("rate", rate+"");
		paraMap.put("usdAmt", usdAmt);
		paraMap.put("cnyAmt", cnyAmt);
		paraMap.put("userId", userId);
		paraMap.put("clientType", clientType);
		paraMap.put("bankId", bankId);
		try {
			Map<String, Object> resultMap = fundAccountServiceImpl.dinPayCreate(paraMap);
			response.setData(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【智付充值，登记服务异常！】"+e.getMessage());
			response = new Response(LTResponseCode.ER400, "登记失败");
		}
		return response;
	}
}
