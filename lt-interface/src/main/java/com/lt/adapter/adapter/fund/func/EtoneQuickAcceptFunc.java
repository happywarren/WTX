package com.lt.adapter.adapter.fund.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiRechargeService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EtoneQuickAcceptFunc extends BaseFunction {

    private final static Logger logger = LoggerFactory.getLogger(EtoneQuickAcceptFunc.class);

    @Autowired
    private IUserApiRechargeService userRechargeService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        try{
          //  userRechargeService.userReviceEtoneQuickPayResponse(paraMap);
        }catch(Exception e){
            logger.error("支付结果返回调用接口--执行失败!"+e.getMessage());
            return LTResponseCode.getCode(LTResponseCode.ER400);
        }
        return LTResponseCode.getCode(LTResponseCode.SUCCESS);
    }
}
