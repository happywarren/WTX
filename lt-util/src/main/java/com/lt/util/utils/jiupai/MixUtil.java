package com.lt.util.utils.jiupai;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by woodle on 17/7/10.
 *
 */
public class MixUtil {

    public static Map<String, String> initHeader(String service, String payToken, String merchantId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("charset", "00");
        paramMap.put("version", "1.0");
        paramMap.put("merchantId", merchantId);
        paramMap.put("requestTime", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        paramMap.put("requestId", String.valueOf(System.currentTimeMillis()));
        paramMap.put("service", service);
        paramMap.put("signType", "MD5");
        paramMap.put("payToken", payToken);
        return paramMap;
    }
}
