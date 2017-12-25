package com.lt.adapter.adapter.user.func;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：QryUserIdBankPic   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月21日 上午11:39:07      
*/
@Component
public class QryUserIdBankPicFunc extends BaseFunction {
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null ;
		String userIdPara = StringTools.formatStr(paraMap.get("userId"), "-1");
		Map<String,String> map = new HashMap<String,String>();
		UserBaseInfo baseInfo = new UserBaseInfo();
		baseInfo.setUserId(userIdPara);
		UserBussinessInfo bussinessInfo = userApiBussinessService.getUserBussiness(baseInfo);
		if(bussinessInfo == null){
			throw new LTException(LTResponseCode.US01105);
		}else{
			map.put("idPicReverse", bussinessInfo.getIdPicReverse());
			map.put("idPicPositive", bussinessInfo.getIdPicPositive());
			map.put("bankcardPic", bussinessInfo.getBankcardPic());
			response = new Response(LTResponseCode.SUCCESS, "查询成功", map);
		}
		
		return response;
	}
}
