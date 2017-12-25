package com.lt.adapter.adapter.log.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.log.IUserLogApiService;
import com.lt.model.logs.LogInfo;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 */
@Component
public class LogFunc extends BaseFunction {

    @Autowired
    private IUserLogApiService userLogApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        try {
            String cmd = StringTools.formatStr(paraMap.get("cmd"), "");
            String func = StringTools.formatStr(paraMap.get("func"), "");
            String data = StringTools.formatStr(paraMap.get("data"), "");
            String ip = StringTools.formatStr(paraMap.get("ip"), "");
            String msg = StringTools.formatStr(paraMap.get("msg"), "");
            String deviceType = StringTools.formatStr(paraMap.get("deviceType"), "");
            String deviceNum = StringTools.formatStr(paraMap.get("deviceNum"), "");
            String userId = StringTools.formatStr(paraMap.get("userId"), "");

            LogInfo logInfo = new LogInfo(null, userId, cmd, func, msg, data, ip, deviceType, deviceNum);
            userLogApiService.save(logInfo);
        } catch (Exception e) {
        }
        return LTResponseCode.getCode(LTResponseCode.SUCCESS);
    }
}
