package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：IsUserServiceActivedFunc   
* 类描述： 判断用户服务是否激活  
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:13:54      
*/
@Component
public class IsUserServiceActivedFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String userIdPara = StringTools.formatStr(paraMap.get("userId"), "-1");
		String serviceCode = StringTools.formatStr(paraMap.get("serviceCode"), "");
		boolean flag = userApiBussinessService.isUserServiceActived(userIdPara, serviceCode);
		return new Response(LTResponseCode.SUCCESS, "查询成功",flag);
	}

}
