package com.lt.adapter.adapter.user.func;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UpdUserLoginPasswordFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午4:10:19      
*/
@Component
public class UpdUserLoginPasswordFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String newPwd = StringTools.formatStr(paraMap.get("newPassword"), "");
		String oldPwd = StringTools.formatStr(paraMap.get("oldPassword"), "");
		String ip = (String) paraMap.get("ip");
		
		if (!StringTools.isNotEmpty(newPwd)) {
			throw new LTException(LTResponseCode.US03107);
		}
		if (!StringTools.isNotEmpty(oldPwd)) {
			throw new LTException(LTResponseCode.US03108);
		}
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,20}");
		boolean flag = pattern.matcher(newPwd).matches();
		if (!flag) {
			throw new LTException(LTResponseCode.US01111);
		}
		
		newPwd = Md5Encrypter.MD5(newPwd);
		String userId = (String) paraMap.get("userId");
		UserBaseInfo baseInfo = new UserBaseInfo();
		baseInfo.setUserId(userId);
		baseInfo.setPasswd(newPwd);
		baseInfo.setIp(ip);
		
		
		try {
			userApiService.updateUserPwd(baseInfo, oldPwd);
		} catch (LTException e) {
			return LTResponseCode.getCode(e.getMessage());
		}
		return  LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

}
