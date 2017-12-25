package com.lt.tms.tcp.constants;


import com.lt.tms.comm.constant.IConstants;

import java.util.HashMap;
import java.util.Map;

public class TcpConstants {

    private static Map<String, String> SERVICE_MAP = new HashMap<String, String>();

    static {
        SERVICE_MAP.put(IConstants.REQ_PING, IConstants.RES_PING);
        SERVICE_MAP.put(IConstants.REQ_QUOTA_DATA, IConstants.RES_QUOTA_DATA);
        SERVICE_MAP.put(IConstants.REQ_CANCEL_MKT_DATA, IConstants.RES_CANCEL_MKT_DATA);
        SERVICE_MAP.put(IConstants.REQ_TRADE, IConstants.RES_TRADE);
    }

    public static String getResService(String reqService){
        if (SERVICE_MAP.containsKey(reqService)){
            return SERVICE_MAP.get(reqService);
        }
        return "";
    }
}
