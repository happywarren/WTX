package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.UserServiceMapper;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：ActiveUserService   
* 类描述： 用户激活服务 
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:10:54      
*/
@Component
public class ActiveUserServiceFunc extends BaseFunction {

	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String serviceCode = StringTools.formatStr(paraMap.get("serviceCode"), "");
		String userIdPara = StringTools.formatStr(paraMap.get("userId"), "-1");
		
		UserServiceMapper usServiceMapper = new UserServiceMapper();
		usServiceMapper.setUserId(userIdPara);
		usServiceMapper.setServiceCode(serviceCode);
		userApiBussinessService.activeUserService(usServiceMapper);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
