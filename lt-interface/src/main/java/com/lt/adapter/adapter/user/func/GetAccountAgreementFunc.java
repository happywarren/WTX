package com.lt.adapter.adapter.user.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：GetAccountAgreementFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:50:51      
*/
@Component
public class GetAccountAgreementFunc extends BaseFunction {

	@Override
	public Response response(Map<String, Object> paraMap) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> service = new HashMap<String,String>();
		service.put("title", "服务条款");
		service.put("url", "/m/rule/service");
		list.add(service);
		Map<String,String> statement = new HashMap<String,String>();
		statement.put("title", "免责声明");
		statement.put("url", "/m/rule/statement");
		list.add(statement);
		Map<String,String> secret = new HashMap<String,String>();
		secret.put("title", "隐私政策");
		secret.put("url", "/m/rule/secret");
		list.add(secret);
		Map<String,String> riskShow = new HashMap<String,String>();
		riskShow.put("title", "风险披露");
		riskShow.put("url", "/m/rule/riskShow");
		list.add(riskShow);
		Map<String,String> supervise = new HashMap<String,String>();
		supervise.put("title", "监管及牌照");
		supervise.put("url", "/m/rule/supervise");
		//list.add(supervise);
		Map<String,String> customer = new HashMap<String,String>();
		customer.put("title", "League Trade客户协议");
		customer.put("url", "/m/rule/customer");
		//list.add(customer);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
	}

}
