/*
 * PROJECT NAME: stock-utils
 * PACKAGE NAME: com.luckin.stock.crypt
 * FILE    NAME: MD5Util.java
 * COPYRIGHT: Copyright(c) 2016 XALOE All Rights Reserved.
 */
package com.lt.quota.core.utils.aes;

import java.security.MessageDigest;

/**
 * TODO（描述类的职责）
 *
 * @author xiezb
 * @version <b>1.0.0</b>
 * @date 2016年8月1日 上午9:25:19
 */
public class MD5Util {

    /**
     * TODO（方法详细描述说明、方法参数的具体涵义）
     *
     * @param src
     * @return
     * @author xiezb
     * @date 2016年8月1日 上午9:25:38
     */
    public static String md5(String src) {
        // 定义数字签名方法, 可用：MD5, SHA-1
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(src.getBytes("UTF-8"));
            return byte2HexStr(b);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * TODO（方法详细描述说明、方法参数的具体涵义）
     *
     * @param b
     * @return
     * @author Administrator
     * @date 2016年6月15日 上午11:59:26
     */
    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }

            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * TODO（方法详细描述说明、方法参数的具体涵义）
     *
     * @param args
     * @author xiezb
     * @date 2016年8月1日 上午9:25:19
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String s1 = "MerNo=40256&BillNo=1470020263957&Amount=50.00&OrderTime=20160801105743&ReturnURL=http://stock.xjdrxx.com/financy/ecpsspay/returnNotify2&AdviceURL=http://stock.xjdrxx.com/financy/ecpsspay/adviceNotify2&xjdrxxfw18689210389";

        String md5 = MD5Util.md5(s1);
        System.out.println(md5);
    }

}
