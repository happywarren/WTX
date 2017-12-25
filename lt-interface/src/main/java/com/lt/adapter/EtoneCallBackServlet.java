package com.lt.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EtoneCallBackServlet extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(Logger.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String transCode = req.getParameter("transCode");
        String merchantId = req.getParameter("merchantId");
        String respCode = req.getParameter("respCode");
        String sysTraceNum = req.getParameter("sysTraceNum");
        String merOrderNum = req.getParameter("merOrderNum");
        String orderId = req.getParameter("orderId");
        String bussId = req.getParameter("bussId");
        String tranAmt = req.getParameter("tranAmt");
        String orderAmt = req.getParameter("orderAmt");
        String bankFeeAmt = req.getParameter("bankFeeAmt");
        String integralAmt = req.getParameter("integralAmt");
        String vaAmt = req.getParameter("vaAmt");
        String bankAmt = req.getParameter("bankAmt");
        String bankId = req.getParameter("bankId");
        String integralSeq = req.getParameter("integralSeq");
        String vaSeq = req.getParameter("vaSeq");
        String bankSeq = req.getParameter("bankSeq");
        String tranDateTime = req.getParameter("tranDateTime");
        String payMentTime = req.getParameter("payMentTime");
        String settleDate = req.getParameter("settleDate");
        String currencyType = req.getParameter("currencyType");
        String orderInfo = req.getParameter("orderInfo");
        String userId = req.getParameter("userId");
        String userIp = req.getParameter("userIp");
        String reserver1 = req.getParameter("reserver1");
        String reserver2 = req.getParameter("reserver2");
        String reserver3 = req.getParameter("reserver3");
        String reserver4 = req.getParameter("reserver4");
        String signValue = req.getParameter("signValue");
        String txnString=transCode+"|"+merchantId+"|"+respCode+"|"
                +sysTraceNum+"|"+merOrderNum+"|"+orderId+"|"+bussId+"|"
                +tranAmt+"|"+orderAmt+"|"+bankFeeAmt+"|"+integralAmt+"|"
                +vaAmt+"|"+bankAmt+"|"+bankId+"|"+integralSeq+"|"+vaSeq +"|"+bankSeq+"|"
                +tranDateTime+"|"+payMentTime+"|"
                + settleDate+"|"
                +currencyType+"|"+orderInfo+"|"+userId;
        Map<String,Object> paramsMap =new HashMap<String,Object>();
        paramsMap.put("transCode",transCode);
        paramsMap.put("merchantId",merchantId);
        paramsMap.put("respCode",respCode);
        paramsMap.put("sysTraceNum",sysTraceNum);
        paramsMap.put("merOrderNum",merOrderNum);
        paramsMap.put("orderId",orderId);
        paramsMap.put("bussId",bussId);
        paramsMap.put("tranAmt",tranAmt);
        paramsMap.put("orderAmt",orderAmt);
        paramsMap.put("bankFeeAmt",bankFeeAmt);
        paramsMap.put("integralAmt",integralAmt);
        paramsMap.put("vaAmt",vaAmt);
        paramsMap.put("bankAmt",bankAmt);
        paramsMap.put("bankId",bankId);
        paramsMap.put("integralSeq",integralSeq);
        paramsMap.put("vaSeq",vaSeq);
        paramsMap.put("bankSeq",bankSeq);
        paramsMap.put("tranDateTime",tranDateTime);
        paramsMap.put("payMentTime",payMentTime);
        paramsMap.put("settleDate",settleDate);
        paramsMap.put("currencyType",currencyType);
        paramsMap.put("orderInfo",orderInfo);
        paramsMap.put("userId",userId);
        paramsMap.put("userIp",userIp);
        paramsMap.put("reserver1",reserver1);
        paramsMap.put("reserver2",reserver2);
        paramsMap.put("reserver3",reserver3);
        paramsMap.put("reserver4",reserver4);
        paramsMap.put("signValue",signValue);

        try {
            MainOperator mainOperator = new MainOperator();
           // mainOperator.etoneOperator(paramsMap);
        } catch (Exception e) {
            logger.error("[结果接收处理失败!]" + e.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
