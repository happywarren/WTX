package com.lt.adapter.adapter.user.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserRechargeService;
import com.lt.model.sys.SysConfig;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端充值回调配置
 */
@Component
public class RechargeConfigFunc extends BaseFunction {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IUserRechargeService userRechargeServiceImpl;

    @Override
    public Response response(Map<String, Object> paraMap) {
        Response response = null;
        try {
            List<SysConfig> sysConfigList = userRechargeServiceImpl.rechargeConfigList();
            response = new Response(LTResponseCode.SUCCESS, "查询成功");
            Map<String, Object> resultMap = FastMap.newInstance();
            resultMap.put("configList", convert(sysConfigList));
            response.setData(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            response = new Response(LTResponseCode.ER400, "查询失败！");
        }
        return response;
    }

    private List<Map<String, String>> convert(List<SysConfig> sysConfigList) {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        if (StringTools.isNotEmpty(sysConfigList)) {
            for (SysConfig sysConfig : sysConfigList) {
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("tagCode", sysConfig.getCfgCode());
                dataMap.put("tagTitle", sysConfig.getReleaseName());
                dataMap.put("tagUrl", sysConfig.getCfgValue());
                mapList.add(dataMap);
            }
        }
        return mapList;
    }
}
