package com.lt.adapter.adapter.user.func;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：sendRegisterMessageFunc   
* 类描述：   用户发送短信验证码
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午3:34:28      
*/
@Component
public class SendRegisterMessageFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String sign =  StringTools.formatStr(paraMap.get("sign"), "");
		String tele = StringTools.formatStr(paraMap.get("tele"), "");
		String type = StringTools.formatStr(paraMap.get("sms_type"), "");
		String ip = StringTools.formatStr(paraMap.get("ip"), "");
		String brandCode = StringTools.formatStr(paraMap.get("brand"), "");

		logger.debug("regCode:" + new Date() + "----" + tele);
		logger.debug("brand:" + new Date() + "----" + brandCode);
		if(!StringTools.isTele(tele)){
			return LTResponseCode.getCode(LTResponseCode.US01102);
		}
		
		if(!Md5Encrypter.MD5(tele+"luckin").equalsIgnoreCase(sign)){
			Response resp = LTResponseCode.getCode(LTResponseCode.US01103);
			logger.debug("返回结果--------:{}",resp.toJsonString());
			return resp;
		}
		
		userApiService.sendRegisterMessage(tele,ip,type,brandCode);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
