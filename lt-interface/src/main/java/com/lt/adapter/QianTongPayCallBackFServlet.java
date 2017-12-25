package com.lt.adapter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QianTongPayCallBackFServlet extends HttpServlet{


	private static final long serialVersionUID = 3796124841650428395L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream is = null;
		try {
			//取得通知信息
			logger.info("======钱通充值回调============");
			StringBuffer notifyResultStr = new StringBuffer("");
			is = request.getInputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while((len = is.read(b)) != -1){
				notifyResultStr.append(new String(b,0,len,"utf-8"));
			}
			System.out.println(notifyResultStr);
			MainOperator mainOperator = new MainOperator();
			mainOperator.qianTongPayOperator(notifyResultStr.toString());
			//商户业务处理
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (Exception e) {}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
