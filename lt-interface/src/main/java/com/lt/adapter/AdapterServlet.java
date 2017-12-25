package com.lt.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.feedback.exception.FeedbackErrCode;
import com.lt.feedback.exception.FeedbackException;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 作者: 邓玉明
 * 时间: 2017/2/24 上午11:35
 * email:cndym@163.com
 */
public class AdapterServlet extends HttpServlet {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = request.getParameter("msg");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        long time = new Date().getTime();
        PrintWriter out = response.getWriter();

        msg = request.getParameter("msg");
        msg = StringTools.formatStr(msg, "");
        msg = new String(request.getParameter("msg").getBytes("ISO-8859-1"), "UTF-8");
        logger.info("ip: " + IpUtils.getUserIP(request));
        logger.info("method:" + request.getMethod());
        MainOperator mainOperator = new MainOperator();
        String jsonStr;
        try {
            jsonStr = mainOperator.operator(request);
        } catch (LTException e) {
            e.printStackTrace();
            jsonStr = LTResponseCode.getCode(e.getMessage()).toJsonString();
        } catch (FeedbackException e) {
            e.printStackTrace();
            String message = FeedbackErrCode.getMsg(e.getMessage());
            Response errorRes = new Response(e.getMessage(), message);
            jsonStr = errorRes.toJsonString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
        }
        long subTime = new Date().getTime() - time;
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            String cmd = jsonObject.getString("cmd");
            String func = jsonObject.getString("func");
            if (subTime > 1000) {
                logger.error("cmd :" + cmd + "; func :" + (func) + "执行时间（" + (subTime) + "）毫秒");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.PR00001);
        }

        out.print(jsonStr);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
