package com.lt.adapter.adapter.fund.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：ReviceZfbResponseFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月30日 上午10:29:01      
*/
@Component
public class ReviceZfbResponseFunc extends BaseFunction {
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		fundAccountApiService.reviceZfbResponse(paraMap);
		return null;
	}
}
