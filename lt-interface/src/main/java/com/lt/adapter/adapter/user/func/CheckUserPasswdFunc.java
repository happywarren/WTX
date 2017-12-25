package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：CheckUserPasswdFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:53:20      
*/
@Component
public class CheckUserPasswdFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null ;
		String passwd = StringTools.formatStr(paraMap.get("passwd"), "");// 密码
		String token = StringTools.formatStr(paraMap.get("token"), "");// 密码
		if (StringTools.isEmpty(passwd)) {
			return LTResponseCode.getCode(LTResponseCode.US03101);
		}

//		boolean b = userApiService.checkUserPasswd(token, passwd);
		int count = userApiService.checkUserPasswdCount(token, passwd);
		if (count == 0 ) {
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} else {
			response = LTResponseCode.getCode(LTResponseCode.US02102, 3-count);
		}

		return response;
	}

}
