package com.lt.quota.core.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/8 上午9:40
 * email:cndym@163.com
 */
public class ErrCode implements IErrCode {
    private static Map<String, String> errMap = new HashMap<String, String>();

    static {
        forInstance();
    }

    public static void forInstance() {
        //系统级
        errMap.put(ERR_0000, "系统处理成功！");
        errMap.put(ERR_9999, "系统处理失败！");
        //行情相关
        errMap.put(ERR_1000, "行情最后时间超时！");
        errMap.put(ERR_1001, "行情价小于等于0！");
        errMap.put(ERR_1002, "无法获取行情！");
        //订单服务相关
        errMap.put(ERR_2000, "文件存储失败！");

    }


    public static String getMsg(String errCode) {
        if (errMap.containsKey(errCode)) {
            return errMap.get(errCode);
        } else {
            throw new IllegalArgumentException("无效的错误码{" + errCode + "}");
        }
    }


}
