package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.fund.FundVo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetUserAmtFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:47:45      
*/
@Component
public class GetUserAmtFunc extends BaseFunction {

	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null ;
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		try{
			FundVo fund = userApiBussinessService.getUserAmt(userId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, fund);
		}catch (LTException e) {
			// TODO: handle exception
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response;
	}

}
