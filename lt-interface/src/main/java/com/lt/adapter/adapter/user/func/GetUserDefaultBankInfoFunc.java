package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetUserDefaultBankInfo   
* 类描述：  获取用户上次充值银行卡或默认银行卡 
* 创建人：yuanxin   
* 创建时间：2017年3月27日 下午3:34:01      
*/
@Component
public class GetUserDefaultBankInfoFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	@Autowired
	private IProductApiService productApiServiceImpl ;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		String user_id = StringTools.formatStr(paraMap.get("userId"), "-1");
		UserchargeBankDetailInfo userchargeBankInfo = userApiBussinessService.getUserChargeBankInfo(user_id);
		
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		if(rate == null ){
			throw new LTException(LTResponseCode.FU00000);
		}
		paraMap.put("rate", rate);
		
		if(userchargeBankInfo == null){
			throw new  LTException(LTResponseCode.US01105);
		}
		
		userchargeBankInfo.setRate(rate); 
		Response response = new Response(LTResponseCode.SUCCESS, "查询成功",userchargeBankInfo);
		return response;
	}
}
