package com.lt.adapter.adapter.version.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.version.SysVersionApiService;
import com.lt.model.version.SysAppVersion;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class CheckVersionFunc extends BaseFunction {

    @Autowired
    private SysVersionApiService sysVersionServiceImpl;

    private static final Set<String> channelSet = new HashSet<>();

    static {
        channelSet.add("qhwh");
        channelSet.add("silver");
        channelSet.add("h_");
        channelSet.add("o_");
        channelSet.add("qh_");
    }

    @Override
    public Response response(Map<String, Object> paraMap) {
        String brand = StringTools.formatStr(paraMap.get("brand"), "");
        String channel = StringTools.formatStr(paraMap.get("channel"), "");
        String platform = StringTools.formatStr(paraMap.get("deviceType"), "");
        String version = StringTools.formatStr(paraMap.get("version"), "");

        if (platform.equals("Android")) {
            boolean userBrand = false;
            for (String prefix : channelSet) {
                if (channel.startsWith(prefix)) {
                    userBrand = true;
                    break;
                }
            }

            if (userBrand) {
                channel = brand;
            } else {
                channel = "10000";
            }
        }

        SysAppVersion sysAppVersion = new SysAppVersion();
        sysAppVersion.setChannel(channel);
        sysAppVersion.setPlatform(platform);
        sysAppVersion.setVersion(version);

        SysAppVersion returnVersion = sysVersionServiceImpl.checkVersion(sysAppVersion);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("status", 1);
        dataMap.put("version", version);
        dataMap.put("url", "");
        dataMap.put("updateInfo", "");
        if (StringTools.isNotEmpty(returnVersion)) {
            dataMap.put("status", returnVersion.getStatus());
            dataMap.put("version", returnVersion.getVersion());
            dataMap.put("url", returnVersion.getUrl());
            dataMap.put("updateInfo", returnVersion.getUpdateInfo());
        }
        Response response = new Response(LTResponseCode.SUCCESS, "查询成功", dataMap);
        return response;
    }
}
