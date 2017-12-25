package com.lt.adapter.adapter.advertise.func;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.advertise.AdvertiseApiService;
import com.lt.model.advertise.Advertisement;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetAdvertistmentFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月5日 上午9:53:31      
*/
@Component
public class GetAdvertistmentFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdvertiseApiService advertiseApiServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		String type = StringTools.formatStr(paraMap.get("adverType"), "1");
		
		if(type.equals("1") || type.equals("2")){
			Integer contentType = Integer.parseInt(type);
			List<Advertisement> list = advertiseApiServiceImpl.getAdvertisementByType(contentType);
			return new Response(LTResponseCode.SUCCESS, "查询成功",list);
		}else{
			return new Response(LTResponseCode.ER400, "查询异常");
		}
	}
}
