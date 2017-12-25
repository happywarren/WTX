package com.lt.adapter.adapter.promote.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.promote.IPromoteApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.InvitecodeTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 将用户ip，tele，和推广员码添加到mongo记录下来，注册用
 * @author jingwb
 *
 */
@Service
public class AddIPTeleFunc extends BaseFunction{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IPromoteApiService promoteApiService;
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		logger.info("=================paraMap={}================",paraMap);
		Response response = null;
		try{
			String c = String.valueOf(paraMap.get("c"));
			if(StringTools.isEmpty(c)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002);
			}
			//用户ip
			String ip = String.valueOf(paraMap.get("ip"));
			String tele = String.valueOf(paraMap.get("tele"));
			//获取推广人user_id
			Integer promote_user_id = InvitecodeTools.getUserIdByCode(c);
			String userId = userApiBussinessService.queryUserId(String.valueOf(promote_user_id));
			logger.info("========promote_user_id="+userId+", ip="+ip+", tele="+tele+"========");
			// 统计所有点击个数
			int i = InvitecodeTools.increase();
			promoteApiService.visitCount(i + 1, "-999");
			promoteApiService.addPromoteLibrary(userId, ip, tele);
			int count = promoteApiService.getVisitCount(userId);
			promoteApiService.visitCount(count + 1,userId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("将用户ip，tele，和推广员码添加到mongo记录下来异常,e={}",e);
		}
		return response;
	}
}
