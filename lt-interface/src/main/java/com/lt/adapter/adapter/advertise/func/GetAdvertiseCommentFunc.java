package com.lt.adapter.adapter.advertise.func;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.advertise.AdvertiseApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetAdvertiseCommentFunc   
* 类描述：返回广告图内容   
* 创建人：yuanxin   
* 创建时间：2017年7月5日 下午2:34:30      
*/
@Service
public class GetAdvertiseCommentFunc extends BaseFunction{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdvertiseApiService advertiseApiServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null ;
		
		String adverId = StringTools.formatStr(paraMap.get("adverId"), "");
		
		if(adverId.equals("")){
			response = new Response(LTResponseCode.ER400, "查询无内容");
		}else{
			String comment = advertiseApiServiceImpl.getAdvertistContent(adverId);
			response = new Response(LTResponseCode.SUCCESS, "查询成功",comment);
		}
		return response;
	}
}
