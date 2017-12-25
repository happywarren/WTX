package com.lt.adapter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.lt.util.utils.swiftpass.config.SwiftpassConfig;
import com.lt.util.utils.swiftpass.util.SignUtils;
import com.lt.util.utils.swiftpass.util.XmlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 威富通支付宝扫码支付回调处理
 */
public class SwiftPassResultSerlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = XmlUtils.parseRequst(req);
            System.out.println("通知内容：" + resString);
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");

                String json = JSONObject.toJSONString(map);
                Map<String,Object> parseMap = (Map<String, Object>) JSON.parse(json);
                MainOperator mainOperator = new MainOperator();
                mainOperator.swiftPassOperator(parseMap);

            }
            resp.getWriter().write(resString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
