package com.lt.manager.controller.fund;

import com.lt.api.fund.IFundAccountApiService;
import com.lt.manager.service.fund.IFundWithdrawService;
import com.lt.util.utils.JSONTools;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 爸爸付充值/提现回调接口
 *
 * @author lvx
 * @created 2017/8/23
 */
@Controller
public class FundDaddypayCallBackController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFundWithdrawService fundWithDrawServiceImpl;
    @Autowired
    private IFundAccountApiService fundAccountApiService;

    @RequestMapping("/callbackForDaddypay")
    @ResponseBody
    public String callbackForDaddypay(HttpServletRequest request) {
        Enumeration en = request.getParameterNames();
        Map<String,String> map = new HashMap<>();
        Map<String,String> result = new HashMap<>();
        while (en.hasMoreElements()) {
            String param = en.nextElement().toString();
            map.put(param, request.getParameter(param));
        }
        logger.info("daddyPay回调开始,入参{}",map);

        String type = map.get("type");
        String orderId = map.get("company_order_num");
        String mownecunOrderNum = map.get("mownecum_order_num");

        if(StringTools.isEmpty(orderId)){
            logger.info("参数缺失");
            result.put("status", "0");
            result.put("error_msg", "company_order_num参数缺失");
            return JSONTools.toJSON(result);

        }
        //提现信息确认回调接口
        if("requestWithdrawApproveInformation".equals(type)){
            Map<String,String> rmap = new HashMap<>();
            rmap.put("company_order_num", orderId);
            rmap.put("mownecum_order_num",mownecunOrderNum);
            rmap.put("status","4");
            rmap.put("error_msg","");
            return JSONTools.toJSON(rmap);
        }
        //提现结果确认回调接口
        else if("withdrawalResult".equals(type)){
            Map<String,String> rmap = fundWithDrawServiceImpl.withdrawalResultForDaddypay(map);
            rmap.put("company_order_num", orderId);
            rmap.put("mownecum_order_num", mownecunOrderNum);
            return JSONTools.toJSON(rmap);
        }
        //充值确认回调接口
        else if("addTransfer".equals(type)){
            Map<String,String> rmap = fundAccountApiService.callbackForDaddyPay(map);
            rmap.put("company_order_num", orderId);
            rmap.put("mownecum_order_num", mownecunOrderNum);
            return JSONTools.toJSON(rmap);
        }

        return JSONTools.toJSON(result);

    }
}
