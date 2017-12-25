package com.lt.adapter.adapter.user.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.BankInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetBankInfoFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:49:46      
*/
@Component
public class GetBankInfoFunc extends BaseFunction {

	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		List<BankInfo> bankInfo = userApiBussinessService.getBankInfo();
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, bankInfo);
	}

}
