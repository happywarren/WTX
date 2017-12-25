package com.lt.adapter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RechargeSuccessServlet extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        OutputStream oStream =  resp.getOutputStream();


        logger.info("返回结果给用户!");
        String respCode= req.getParameter("respCode");
        if(respCode != null && respCode.equals("0000")){
            oStream.write("success!".getBytes("utf-8"));
        }else{
            oStream.write("failure!".getBytes("utf-8"));
        }
        oStream.flush();
        IOUtils.closeQuietly(oStream);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    //返回值类型为Map<String, String>
    public  Map<String, String> getParameterStringMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();//把请求参数封装到Map<String, String[]>中
        Map<String, String> returnMap = new HashMap<String, String>();
        String name = "";
        String value = "";
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            name = entry.getKey();
            String[] values = entry.getValue();
            if (null == values) {
                value = "";
            } else if (values.length>1) {
                for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = values[0];//用于请求参数中请求参数名唯一
            }
            logger.info(name+"="+value);
            returnMap.put(name, value);

        }
        return returnMap;
    }
}
