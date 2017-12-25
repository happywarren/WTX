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

import javolution.util.FastMap;

/**
 * 项目名称：lt-interface<br>
 * 类名称：IapppayCallBackServlet <br>
 * 类描述： 爱贝回调接口接收<br>
 * 创建人：yubei<br>
 * 创建时间：2017年9月6日 上午9:44:35 <br>
 */
public class IapppayCallBackServlet extends HttpServlet {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;

	/**
	 * @param req
	 * @param resp
	 * @return
	 * @author
	 * @hrows Exception
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> paramMap = FastMap.newInstance();
		try {
			logger.info("接收爱贝支付返回结果====");
			Enumeration<String> en = req.getParameterNames();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				paramMap.put(param, req.getParameter(param));
			}
			MainOperator mainOperator = new MainOperator();
			mainOperator.iapppayOperator(paramMap);
			resp.getWriter().println("SUCCESS");
		} catch (Exception e) {
			logger.error("[爱贝支付结果接收处理失败!]" + e.getMessage());
			resp.getWriter().println("FAIL");
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
