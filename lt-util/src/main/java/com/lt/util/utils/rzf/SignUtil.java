package com.lt.util.utils.rzf;

public class SignUtil {

    /**
     * 方法描述: 数据加签<br>
     *
     * @param id 商户ID
     * @param appid 应用ID
     * @param orderidinf 商户订单号
     * @param totalPrice 订单金额
     * @param key 密钥
     * @return boolean类型验签结果
     *
     */
    public static String verifyToSign(String id, String appid, String orderidinf, String totalPrice, String key) {
        StringBuffer sbf = new StringBuffer("");
        sbf.append(id).append(appid).append(orderidinf).append(totalPrice).append(key);
        String str = sbf.toString();// 加密原串
        System.out.println("加签原串："+str);
        String sign = RZFMD5.encode(str);// 生成签名
        System.out.println("生成签名："+sign);
        return sign;
    }

    /**
     * 方法描述: 数据验签<br>
     *
     * @param id 商户ID
     * @param appid 应用ID
     * @param orderidinf 商户订单号
     * @param totalPrice 订单金额
     * @param sign 签名
     * @param key 密钥
     * @return boolean类型验签结果
     */
    public static boolean verifySign(String id, String appid, String orderidinf, String totalPrice, String sign, String key) {
        boolean vSign = false;
        StringBuffer sbf = new StringBuffer("");
        sbf.append("payreturn").append(id).append(appid).append(orderidinf).append(totalPrice).append(key);
        String str = sbf.toString();// 加密原串
        System.out.println("加签原串："+str);
        String md5Si = RZFMD5.encode(str);// 生成签名
        System.out.println("生成签名："+sign);
        if (sign != null && sign.equals(md5Si)) {// 验签通过，返回true
            System.out.println("验证签名成功!");
            vSign = true;
        }
        return vSign;
    }

}
