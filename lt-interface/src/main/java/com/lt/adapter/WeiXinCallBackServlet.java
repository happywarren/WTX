package com.lt.adapter;

import com.lt.util.utils.JSONTools;
import com.lt.util.utils.jiupai.RequestUtil;
import com.lt.util.utils.swiftpass.util.XmlUtils;
import com.lt.util.utils.wxpay.WXPayUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WeiXinCallBackServlet extends HttpServlet{

    private Logger logger = Logger.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String xml = "";
        try{
            logger.info("进入到微信支付回调！");
            InputStream iStream = req.getInputStream();
            int size = 1024;
            if(iStream != null){
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes =new byte[size];
                int count = -1;
                while ((count = iStream.read(tempBytes, 0, size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                String result = new String(outStream.toByteArray(), "UTF-8");
                Document doc = DocumentHelper.parseText(result);
                Map<String, String> resultMap = XmlUtils.toMap(doc.getRootElement());
                String resulxt = JSONTools.toJSON(resultMap);
                MainOperator mainOperator = new MainOperator();
                Map<String,Object> map = new HashMap<String,Object>();
                for(Map.Entry<String,String> entry : resultMap.entrySet()){
                    map.put(entry.getKey(),entry.getValue());
                }
                mainOperator.weixinh5Operator(map);
                xml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                resp.getWriter().write(xml);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
