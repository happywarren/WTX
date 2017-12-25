package com.lt.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;

public class RzfPayCallBackServlet extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(RzfPayCallBackServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> paramMap = FastMap.newInstance();
        try {
            logger.info("接收融智付支付返回结果====");
            //获取正文内容
            int len =  req.getContentLength();
            ServletInputStream sis = req.getInputStream();
            byte [] buffer = new byte[len];
            sis.read(buffer,0,len);

            String result = new String(buffer,0,len,"utf-8");
            JSONObject jsonObject = JSON.parseObject(result);
            paramMap.put("sign",jsonObject.getString("sign"));
            paramMap.put("rate",jsonObject.getString("rate"));
            paramMap.put("orderidinf",jsonObject.getString("orderidinf"));
            paramMap.put("tradeFee",jsonObject.getString("tradeFee"));
            paramMap.put("appid",jsonObject.getString("appid"));
            paramMap.put("paysucctime",jsonObject.getString("paysucctime"));
            paramMap.put("tradeAmount",jsonObject.getString("tradeAmount"));
            paramMap.put("msg",jsonObject.getString("msg"));
            paramMap.put("success",jsonObject.getString("success"));
            paramMap.put("orderId",jsonObject.getString("orderId"));
            paramMap.put("totalPrice",jsonObject.getString("totalPrice"));
            MainOperator mainOperator = new MainOperator();
          //  mainOperator.rzfOperator(paramMap);
            resp.getWriter().println("SUCCESS");
        } catch (Exception e) {
            logger.error("[融智付支付结果接收处理失败!]" + e.getMessage());
            resp.getWriter().println("FAIL");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
