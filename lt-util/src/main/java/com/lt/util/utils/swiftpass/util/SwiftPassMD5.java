package com.lt.util.utils.swiftpass.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

/** 
* 功能：MD5签名
* 版本：3.3
* 修改日期：2012-08-17
* */
public class SwiftPassMD5 {
    public static void main(String[] args) {
        String str = "attach=附加信息&bank_type=ICBC_FP&charset=UTF-8&coupon_fee=0&fee_type=1&mch_id=001075562100008&nonce_str=7e158509216bb7c3aa4cf72165af043a&out_trade_no=1409543900454&pay_result=0&result_code=0&sign_type=SwiftPassMD5&status=0&time_end=20140901115747&total_fee=1&trade_type=pay.weixin.scancode&transaction_id=001075562100008201409010000129&version=1.0";
        System.out.println(SwiftPassMD5.sign(str, "&key=e1cf0ddcf6b47b59c351565d8ad717af", "utf-8"));
        
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
    	System.out.println(text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}