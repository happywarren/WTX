package com.lt.adapter.adapter.advertise;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseAdapter;
import com.lt.adapter.adapter.IFunction;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：AdvertiseShow   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月5日 下午3:02:16      
*/
@Service
public class AdvertiseShowAdapter extends BaseAdapter {
	
	@Override
	public Response execute(Map<String, Object> paraMap) {
		String func = StringTools.formatStr(paraMap.get("func"), "");
		IFunction function = (IFunction) SpringUtils.getBean(func + FUNC);
		if (null == function) {
			return funcErr();
		}
		return function.response(paraMap);
	}
}
