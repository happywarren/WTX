package com.lt.adapter.adapter.user.func;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.CollectionFactory;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserChargeBankInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetUserBankInfoListFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月28日 上午9:48:09      
*/
@Component
public class GetUserBankInfoListFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		String user_id = StringTools.formatStr(paraMap.get("userId"), "-1");
		
		List<UserChargeBankInfo> list =  userApiBussinessService.getUserChargeBankInfoList(user_id);
		
		if(CollectionUtils.isNotEmpty(list)){
			Response response = new Response(LTResponseCode.SUCCESS, "查询成功", list);
			return response;
		}else{
			throw new  LTException(LTResponseCode.US01105);
		}
	}
}
