package com.lt.adapter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

/**   
* 项目名称：lt-interface   
* 类名称：UnspayCallBackServlet   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月26日 下午2:46:25      
*/
public class UnspayCallBackServlet extends HttpServlet {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("进入到银生宝回调接口");
		MainOperator mainOperator = new MainOperator();
		req.setAttribute("callbackForUnspay", "callbackForUnspay");;
        String jsonStr;
        try {
        	jsonStr = mainOperator.operator(req);
        } catch (LTException e) {
        	e.printStackTrace();
            jsonStr = LTResponseCode.getCode(e.getMessage()).toJsonString();
        } catch (Exception e) {
        	e.printStackTrace();
            jsonStr = LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
        }
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
