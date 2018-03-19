package com.lt.adapter;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlipayH5CallBackServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    private static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjd5a7/o+A54D8sP1nGZqzVAeJoHAQzhHv0SBt/iJDNiHSHIFgwYuC3TR3TwLiS5Loemdj1xkDfXabSAi1TZkNP4bLHsCPIDvUSXPXv7KYfKlWptirDwERIcxjKxpXE5mSmwOfPbcpoPf3se//fmHryc0xZ9LxMUqX0dKkq0UBMgfRLPSQZ/NhI/ASW33urxJCQujQdII0MwV1vnOIAqe5x8CC+mS3PFDE6GyvibJDeKi1VFBEeoPIS7N1TOkPrbKQ9GnFfr21TR50mTmFKajPS+bgVORc2MZ8ZDqeJ52mA5dPY86EcrsuJHScMCcoDTjZGZxY3BLZU8KXDi0RVUVDwIDAQAB";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = req.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

            MainOperator mainOperator = new MainOperator();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("alipayParams",params);

            try {
                mainOperator.alipayH5Operator(map);
            } catch (Exception e) {
                logger.error("[结果接收处理失败!]" + e.getMessage());
            }
            resp.getWriter().print("success");


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
