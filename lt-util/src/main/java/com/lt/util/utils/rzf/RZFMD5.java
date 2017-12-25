package com.lt.util.utils.rzf;

import java.security.MessageDigest;

public class RZFMD5 {

    /**
     * 方法描述:利用java自带的MD5加密，生成32个字符的16进制(utf-8编码) <br>
     */
    public final static String encode(String s) {
        // 16进制字符
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            if (s == null) {
                return null;
            }
            byte[] btInput = s.getBytes("utf-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String jm = new String(str);
            return jm;
        } catch (Exception e) {
            System.out.println("md5加密失败"+ e);
            return "";
        }
    }

}
