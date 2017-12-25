package com.lt.adapter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.error.LTResponseCode;

/**
 * 项目名称：lt-interface<br>
 * 类名称：IapppayCallBackServlet <br>
 * 类描述： 聚合回调接口接收<br>
 * 创建人：yubei<br>
 * 创建时间：2017年9月20日 上午9:44:35 <br>
 */
public class AggpayCallBackFServlet extends HttpServlet {

	private final static Logger logger = LoggerFactory.getLogger(AggpayCallBackFServlet.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 成功处理返回SUCCESS
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @author
	 * @throws IOException
	 * @hrows Exception
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, Object> paramMap = new TreeMap<>();
		try {
			logger.info("====接收聚合支付返回结果====");
			req.setCharacterEncoding("utf-8");
			Enumeration<String> en = req.getParameterNames();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				paramMap.put(param, req.getParameter(param));
			}
			logger.info("聚合支付结果参数:{}",paramMap);
			MainOperator mainOperator = new MainOperator();
			String code = mainOperator.aggpayOperator(paramMap);
			if (code.equals(LTResponseCode.SUCCESS)) {
				resp.getWriter().println("SUCCESS");
			} else {
				resp.getWriter().println("FAIL");
			}
		} catch (Exception e) {
			logger.error("[聚合支付结果接收处理失败!]" + e.getMessage());
			resp.getWriter().println("FAIL");
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
