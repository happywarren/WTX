package com.lt.manager.controller.fund;


import com.lt.api.fund.IFundAccountApiService;
import com.lt.manager.service.fund.IFundWithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 九派提现
 */
@Controller
public class FundJiupaiCallBackController {

    Logger logger = LoggerFactory.getLogger(getClass());

    /*
    @Autowired
    private IFundWithdrawService fundWithDrawServiceImpl;
    @Autowired
    private IFundAccountApiService fundAccountApiService;


    @RequestMapping("/callbackForDaddypay")
    @ResponseBody
    public String callbackForDaddypay(HttpServletRequest request) {
        Map<String,String[]> requestPara = request.getParameterMap();
        Map<String,String> paraMap =new HashMap<String,String>();
        Iterator<Map.Entry<String,String[]>> it = requestPara.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,String[]> entry = it.next();
            String key = entry.getKey();
            String [] values = entry.getValue();
            if(values != null && values.length >= 1){
                paraMap.put(key, new String(values[0].getBytes(), Charset.forName("UTF-8")));
            }
        }
        fundWithDrawServiceImpl.withdrawalResultForJiuPai(paraMap,request);
        return "";
    }*/



}
