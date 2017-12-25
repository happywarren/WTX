package com.lt.adapter.adapter.version.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.version.SysVersionApiService;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetVersionFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月21日 下午3:51:16      
*/
@Component
public class GetVersionFunc extends BaseFunction {
	
	@Autowired
	private SysVersionApiService sysVersionServiceImpl;
	
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String platform = StringTools.formatStr(paraMap.get("platform"), "");
		if(platform.equalsIgnoreCase("IOS")){
			return sysVersionServiceImpl.getSysVersionIOS();
		}else{
			return sysVersionServiceImpl.getSysVersion();
		}
	}
}
