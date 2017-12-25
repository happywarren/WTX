package com.lt.quota.core.quota.rf.operator;

import com.alibaba.fastjson.JSON;
import com.lt.quota.core.comm.spring.SpringUtils;
import com.lt.quota.core.service.IQuotaCoreConfigService;
import com.lt.quota.core.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OuterProductOperator {

    private static OuterProductOperator instance;

    private IQuotaCoreConfigService quotaCoreConfigService;

    private OuterProductOperator() {
        quotaCoreConfigService = SpringUtils.getBean("quotaCoreConfigServiceImpl", IQuotaCoreConfigService.class);
    }


    public static synchronized OuterProductOperator getInstance() {
        if (instance == null) {
            instance = new OuterProductOperator();
        }
        return instance;
    }

    public String preProductPacket() {
        List<Map<String, Object>> productList = quotaCoreConfigService.preProductList(1);
        if (!Utils.isNotEmpty(productList)) {
            return "";
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("contractArray", productList);
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("CMDID", 1002);
        requestData.put("DATA", data);
        return JSON.toJSONString(requestData);
    }

    public String productPacket() {
        List<Map<String, Object>> productList = quotaCoreConfigService.productList(1);
        if (!Utils.isNotEmpty(productList)) {
            return "";
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("contractArray", productList);
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("CMDID", 1002);
        requestData.put("DATA", data);
        return JSON.toJSONString(requestData);
    }

    public String expireProductPacket() {
        List<Map<String, Object>> productList = quotaCoreConfigService.expireProductList(1);
        if (!Utils.isNotEmpty(productList)) {
            return "";
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("contractArray", productList);
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("CMDID", 1003);
        requestData.put("DATA", data);
        return JSON.toJSONString(requestData);
    }
}
