package com.lt.tms.comm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/8 上午9:40
 * email:cndym@163.com
 */
public class TmsErrCode implements ITmsErrCode {

    private static Logger logger = LoggerFactory.getLogger(TmsErrCode.class);

    private static Map<String, String> errMap = new HashMap<String, String>();

    static {
        forInstance();
    }

    public static void forInstance() {
        //系统级
        errMap.put(ERR_0000, "系统处理成功");
        errMap.put(ERR_0001, "参数错误");
        errMap.put(ERR_9999, "系统处理失败");

        //行情相关
        errMap.put(ERR_1000, "合约id为空");
        errMap.put(ERR_1001, "商品代码为空");
        errMap.put(ERR_1002, "证券类型为空");
        errMap.put(ERR_1003, "币种为空");
        errMap.put(ERR_1004, "交易所代码为空");
        errMap.put(ERR_1005, "symbol为空");
        errMap.put(ERR_1006, "商品行情订阅失败");
        errMap.put(ERR_1007, "取消商品行情订阅失败");

        //订单服务相关
        errMap.put(ERR_2000, "交易id为空");
        errMap.put(ERR_2001, "商品不可交易");
        errMap.put(ERR_2002, "数量不正确");

    }


    public static String getMsg(String errCode) {
        if (errMap.containsKey(errCode)) {
            return errMap.get(errCode);
        } else {
            logger.error("无效的错误码{" + errCode + "}");
            return "";
        }
    }


}
