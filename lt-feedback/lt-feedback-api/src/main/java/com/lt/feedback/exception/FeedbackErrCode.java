package com.lt.feedback.exception;

import java.util.HashMap;
import java.util.Map;

public class FeedbackErrCode {

    private static Map<String, String> errMap = new HashMap<String, String>();

    //系统级
    static String ERR_0000 = "200";//成功
    static String ERR_9999 = "9999";//系统错误

    //用户id为空
    public static String ERR_FB00001 = "FB00001";
    //反馈内容为空
    public static String ERR_FB00002 = "FB00002";
    //反馈id为空
    public static String ERR_FB00003 = "FB00003";
    //反馈内容超过1000个字符限制
    public static String ERR_FB00004 = "FB00004";
    //图片个数超过限制
    public static String ERR_FB00005 = "FB00005";
    static {
        forInstance();
    }

    public static void forInstance() {
        //系统级
        errMap.put(ERR_0000, "系统处理成功！");
        errMap.put(ERR_9999, "系统处理失败！");

        errMap.put(ERR_FB00001, "用户id为空");
        errMap.put(ERR_FB00002, "反馈内容为空");
        errMap.put(ERR_FB00003, "反馈id为空");
        errMap.put(ERR_FB00004, "反馈内容超过1000个字符限制");
        errMap.put(ERR_FB00005, "图片个数超过限制");
    }


    public static String getMsg(String errCode) {
        if (errMap.containsKey(errCode)) {
            return errMap.get(errCode);
        } else {
            throw new IllegalArgumentException("无效的错误码{" + errCode + "}");
        }
    }


}
