package com.lt.adapter.adapter.user.func;

import java.util.List;
import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.TokenTools;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.model.Token;

/**   
* 项目名称：lt-interface   
* 类名称：GetUserHomePageFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:44:39      
*/
@Component
public class GetUserHomePageFunc extends BaseFunction {

	@Autowired
	private IOrderApiService orderApiService;
	@Autowired
	private IOrderScoreApiService orderScoreApiService;
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String user_id = StringTools.formatStr(paraMap.get("userId"), "-1");
		List<UserService> list = userApiBussinessService.getUserHomePageInfo(user_id);
		Map<String,Double> floatAmt =  orderApiService.findFloatAmtByUserId(user_id);
		Map<String,Double> floatAmtScore =  orderScoreApiService.findFloatAmtByUserId(user_id);
		for(UserService service : list){
			if(service.getServiceCode().equals(ServiceContant.CASH_SERVICE_CODE)){
				service.setFloatAmt(floatAmt.get("floatCashAmt"));
				service.setDetailUrl("/m");
			}else if(service.getServiceCode().equals(ServiceContant.SCORE_SERVICE_CODE)){
				service.setFloatAmt(floatAmtScore.get("floatScoreAmt"));
			}
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
	}

}
