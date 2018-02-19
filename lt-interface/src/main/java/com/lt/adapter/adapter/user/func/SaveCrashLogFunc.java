package com.lt.adapter.adapter.user.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SaveCrashLogFunc extends BaseFunction {

    @Autowired
    private IUserApiService userApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        //String userId = String.valueOf(paraMap.get("userId"));
        String device_model = String.valueOf(paraMap.get("deviceType"));
        //软件版本
        String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");
        String crash_reason =  StringTools.formatStr(paraMap.get("crash_reason"), "");

        Map<String,Object> paramMap = new HashMap<String,Object>();
        //UserBaseInfo userBaseInfo =  userApiService.findUserInfo(userId);
        paramMap.put("tele","");
        paramMap.put("device_model",device_model);
        paramMap.put("version",clientVersion);
        paramMap.put("crash_time",new Date());
        paramMap.put("crash_reason",crash_reason);
        userApiService.saveCrashLog(paramMap);
        return LTResponseCode.getCode(LTResponseCode.SUCCESS);
    }
}
