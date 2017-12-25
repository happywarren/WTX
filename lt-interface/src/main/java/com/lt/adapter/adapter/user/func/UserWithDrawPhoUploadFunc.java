package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：UserWithDrawPhoUploadFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月21日 上午10:46:07      
*/
@Component
public class UserWithDrawPhoUploadFunc extends BaseFunction {
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = new Response(LTResponseCode.SUCCESS,"图片上传成功") ;
		String userIdPara = StringTools.formatStr(paraMap.get("userId"), "-1");
		String bankcardPic = StringTools.formatStr(paraMap.get("bankcardPic"), "");
		String idPicPositive = StringTools.formatStr(paraMap.get("idPicPositive"), "");
		String idPicReverse = StringTools.formatStr(paraMap.get("idPicReverse"), "");
		String deviceModel =  StringTools.formatStr(paraMap.get("deviceModel"), "");
		String ip = StringTools.formatStr(paraMap.get("ip"), "");
		
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		bussinessInfo.setBankcardPic(bankcardPic);
		bussinessInfo.setDeviceModel(deviceModel);
		bussinessInfo.setIdPicPositive(idPicPositive);
		bussinessInfo.setIdPicReverse(idPicReverse);
		bussinessInfo.setUserId(userIdPara);
		bussinessInfo.setIp(ip);
		
		userApiBussinessService.UpdateUserIdPicAndBankPic(bussinessInfo);
		
		return response;
	}
}
