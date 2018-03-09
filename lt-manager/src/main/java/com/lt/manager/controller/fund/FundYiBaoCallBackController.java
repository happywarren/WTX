package com.lt.manager.controller.fund;


import com.lt.api.fund.IFundAccountApiService;
import com.lt.manager.service.fund.IFundWithdrawService;
import com.lt.util.utils.JSONTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * 易宝提现回调接口
 *
 */
@Controller
@RequestMapping("/callback")
public class FundYiBaoCallBackController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFundWithdrawService fundWithDrawServiceImpl;


    @RequestMapping("/callbackForYiBao")
    @ResponseBody
    public String callbackForDaddypay(HttpServletRequest request, HttpServletResponse response){
        Enumeration en = request.getParameterNames();
        Map<String,String> map = new HashMap<>();
        Map<String,String> result = new HashMap<>();
        while (en.hasMoreElements()) {
            String param = en.nextElement().toString();
            map.put(param, request.getParameter(param));
        }
        logger.info("易宝回调开始,入参{}",map);

        Map<String,String> rmap =  fundWithDrawServiceImpl.withdrawalResultForYiBao(map,request,response);
        return JSONTools.toJSON(rmap);
    }


}
