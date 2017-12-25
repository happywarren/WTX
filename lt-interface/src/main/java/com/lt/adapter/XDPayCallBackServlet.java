package com.lt.adapter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.utils.StringTools;

import javolution.util.FastMap;

/**
 * 项目名称：lt-interface 类
 * 名称：XDPayCallBackServlet 
 * 类描述： 熙大支付宝回调接口 创建人：yubei
 * 创建时间：2017年8月7日
 */
public class XDPayCallBackServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			logger.info("接收熙大支付宝返回结果====");
			Map<String, Object> params = FastMap.newInstance();
			Map<String, String[]> requestParams = req.getParameterMap();
			String requestUrl = req.getRequestURL() + "?";
			Enumeration en = req.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				
				params.put(param, req.getParameter(param));
				if(stringBuffer.toString().length()>0){
					stringBuffer.append("&");
				}
				if(StringTools.isNotEmpty(req.getParameter(param))){
					stringBuffer.append(param + "=" + req.getParameter(param));
				}
			}
			params.put("params", requestUrl+stringBuffer.toString());
			params.put("signParam", stringBuffer.toString());
			logger.info("【服务输入】" + stringBuffer.toString());
			MainOperator mainOperator = new MainOperator();
			mainOperator.xdpayOperator(params);
			resp.getWriter().print("SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("接收熙大支付宝结果处理异常");
			resp.getWriter().print("");
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
