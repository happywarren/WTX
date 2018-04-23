package com.lt.adapter.adapter.fund.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.charge.IUserApiRechargeService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Weixinh5AcceptFunc extends BaseFunction{

    @Autowired
    private IUserApiRechargeService userRechargeService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        try {
            paraMap.remove("cmd");
            paraMap.remove("func");
            userRechargeService.userReviceWeixinH5Response(paraMap);
        } catch (Exception e) {
            return LTResponseCode.getCode(LTResponseCode.ER400);
        }
        return LTResponseCode.getCode(LTResponseCode.SUCCESS);
    }
}
