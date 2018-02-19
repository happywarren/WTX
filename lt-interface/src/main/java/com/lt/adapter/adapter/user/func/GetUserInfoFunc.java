package com.lt.adapter.adapter.user.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component

public class GetUserInfoFunc extends BaseFunction{

    @Autowired
    private IUserApiService userApiService;


    @Override
    public Response response(Map<String, Object> paraMap) {
        String userId = String.valueOf(paraMap.get("userId"));
        UserBaseInfo userBaseInfo =  userApiService.findUserInfo(userId);
        Map<String,String> resultMap = new HashMap<String,String>();


        if(userBaseInfo != null){
            resultMap.put("headPic",userBaseInfo.getHeadPic());
            resultMap.put("nickName",userBaseInfo.getNickName());
        }
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, resultMap);
    }
}
