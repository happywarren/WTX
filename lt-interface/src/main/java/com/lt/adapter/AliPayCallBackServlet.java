package com.lt.adapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.utils.alipay.alipayTranfer.util.AlipayNotify;

/**   
* 项目名称：lt-interface   
* 类名称：AliPayCallBackServlet   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月30日 上午9:49:22      
*/
public class AliPayCallBackServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("接收支付宝返回结果====");
    	Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = req.getParameterMap();
		String requestUrl = req.getRequestURL()+"?";
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			requestUrl += name + "=" + valueStr + "&";
			params.put(name, valueStr);
			logger.info("{}:{}",name,valueStr);
		}
		
		logger.info("FULL REQUEST URL::{}",requestUrl);
//		logger.info("验签结果为：{}",AlipayNotify.verify(params));
		MainOperator mainOperator = new MainOperator();
		if(AlipayNotify.verify(params)){
			mainOperator.aliPayoperator(req);
		}
		
		resp.getWriter().print("success");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
