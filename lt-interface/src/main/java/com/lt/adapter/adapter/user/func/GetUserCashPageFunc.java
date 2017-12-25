package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.fund.FundVo;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetUserCashPageFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:51:32      
*/
@Component
public class GetUserCashPageFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String user_id = StringTools.formatStr(paraMap.get("userId"), "-1");
		FundVo fund = userApiBussinessService.getUserAmt(user_id);
		UserBaseInfo baseInfo = new UserBaseInfo();
		baseInfo.setUserId(user_id);
		UserBussinessInfo baBussinessInfo = userApiBussinessService.getUserBussiness(baseInfo);
		baBussinessInfo.setUsedAmt(fund.getCashAmt());
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, baBussinessInfo);
	}

}
