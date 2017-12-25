package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

import javolution.util.FastMap;

/**
 * 项目名称：lt-interface 类名称：queryRateFunc 类描述：智付充值业务登记 创建人：喻贝 创建时间：2017年6月2日
 * 下午17：03：22
 */
@Component
public class queryRateFunc extends BaseFunction {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IProductApiService productApiServiceImpl;

	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		try {
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			response= new Response(LTResponseCode.SUCCESS, "查询成功");
			Map<String, Object> resultMap = FastMap.newInstance();
			resultMap.put("rate", rate);
			response.setData(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败！");
			response = new Response(LTResponseCode.ER400, "查询失败！");
		}
		return response;
	}
}
