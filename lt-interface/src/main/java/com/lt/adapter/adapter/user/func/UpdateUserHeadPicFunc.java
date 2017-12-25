package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserContant;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UpdateUserHeadPic   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午4:15:13      
*/
@Component
public class UpdateUserHeadPicFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		String ip = StringTools.formatStr(paraMap.get("ip"), "");
		String path = StringTools.formatStr(paraMap.get("photoFile"), "");
		String deviceModel = StringTools.formatStr(paraMap.get("deviceModel"), "");
		
		UserBaseInfo baseInfo = new UserBaseInfo();
		baseInfo.setUserId(userId);
		baseInfo.setHeadPic(path);
        baseInfo.setIp(ip);
        baseInfo.setDeviceModel(deviceModel);
		userApiService.updateUserNickAndSign(baseInfo, Integer.valueOf(UserContant.USER_UPDATE_HEAD));
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,baseInfo.getHeadPic());
	}

}
