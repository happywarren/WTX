package com.lt.feedback.utils;

import java.util.Date;

public class OrderIdBuildUtils {

    private static String[] numberString = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String buildRandomString(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int a = (int) (Math.random() * 10);
            sb.append(numberString[a]);
        }
        return sb.toString();
    }


    private static int currI = 0;

    private static String SYSTEM_NO = "0";

    public static String buildId(String begin) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new Date().getTime());
        StringBuffer orderId = new StringBuffer();
        orderId.append(begin);
        orderId.append(SYSTEM_NO);
        orderId.append(stringBuffer.toString());
        orderId.append(getCurrI(4));
        return orderId.toString();

    }

    //16进制
    private static String getCurrI(int length) {
        String t = Integer.toHexString(++currI);
        int len = t.length();
        StringBuffer sb = new StringBuffer();
        if (len > length) {
            currI = 0;
            len = 1;
        }
        if (len < length) {
            sb = new StringBuffer();
            for (int i = 0; i < length - len; i++) {
                sb.append("0");
            }
        }
        sb.append(Integer.toHexString(currI));
        return sb.toString();
    }

    // 票号
    public static String buildSubjectId() {
        return buildId("1");
    }
}
