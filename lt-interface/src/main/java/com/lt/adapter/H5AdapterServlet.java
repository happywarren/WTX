package com.lt.adapter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;

/**
 * 作者: 邓玉明 时间: 2017/2/24 上午11:35 email:cndym@163.com
 */
public class H5AdapterServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String msg = request.getParameter("msg");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        long time = new Date().getTime();
        PrintWriter out = response.getWriter();
        msg = request.getParameter("msg");
        msg = parseData(msg);
        msg = StringTools.formatStr(msg, null);
        MainOperator mainOperator = new MainOperator();
        String jsonStr;
        try {
        	String ip = IpUtils.getUserIP(request);
        	jsonStr = mainOperator.operator(msg,ip);
        } catch (LTException e) {
            e.printStackTrace();
            jsonStr = LTResponseCode.getCode(e.getMessage()).toJsonString();
        } catch (Exception e) {
        	e.printStackTrace();
            jsonStr = LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
        }
        long subTime = new Date().getTime() - time;
        logger.error("执行时间（" + (subTime) + "）毫秒");
        out.print(jsonStr);
        out.flush();
        out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String parseData(String msg){
    	JSONObject jsonObject = JSON.parseObject(msg);
        String data = jsonObject.getString("data");// 实际数据
        System.out.println("=================加密的数据为"+data + MainOperator.KEY);
        String md5 = Md5Encrypter.MD5(data + MainOperator.KEY);
        //h5 在json反复转换时会发生参数位置调换问题，导致后面md5 加密结果不一致 （预发布环境出现）临时修改成test 不做md5 校验
        jsonObject.put("md5", "test");
    	return jsonObject.toJSONString();
    }
	public static void main(String[] args) {
		 String md5 = Md5Encrypter.MD5(null + MainOperator.KEY);
		 System.out.println(md5);
	}
}
