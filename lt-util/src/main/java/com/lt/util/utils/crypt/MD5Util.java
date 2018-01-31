/*
 * PROJECT NAME: stock-utils
 * PACKAGE NAME: com.luckin.stock.crypt
 * FILE    NAME: MD5Util.java
 * COPYRIGHT: Copyright(c) 2016 XALOE All Rights Reserved.
 */ 
package com.lt.util.utils.crypt;

import java.security.MessageDigest;

/**
 * TODO（描述类的职责）
 * @author xiezb
 * @date 2016年8月1日 上午9:25:19
 * @version <b>1.0.0</b>
 */
public class MD5Util {
	
	/**
	 * 
	 * TODO（方法详细描述说明、方法参数的具体涵义）
	 * @author xiezb
	 * @date 2016年8月1日 上午9:25:38
	 * @param src
	 * @return
	 */
	public static String md5(String src) {
		// 定义数字签名方法, 可用：MD5, SHA-1
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(src.getBytes("UTF-8"));
			return byte2HexStr(b, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 *
	 * MD5小写
	 * @author lvx
	 * @date 2017年8月24日
	 * @param src
	 * @return
	 */
	public static String md5Low(String src) {
		// 定义数字签名方法, 可用：MD5, SHA-1
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(src.getBytes("UTF-8"));
			return byte2HexStr(b, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 
	 * TODO（方法详细描述说明、方法参数的具体涵义）
	 * @author Administrator
	 * @date 2016年6月15日 上午11:59:26
	 * @param b
	 * @return
	 */
	private static String byte2HexStr(byte[] b, boolean upperFlag) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String s = Integer.toHexString(b[i] & 0xFF);
			if (s.length() == 1) {
				sb.append("0");
			}
			if(upperFlag){
				sb.append(s.toUpperCase());
			}
			else{
				sb.append(s);
			}
		}
		return sb.toString();
	}
	/**
	 * TODO（方法详细描述说明、方法参数的具体涵义）
	 * @author xiezb
	 * @date 2016年8月1日 上午9:25:19
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s1 = "{\"recordCarrierOperator\":\"\",\"recordIP\":\"192.168.125.100\",\"regSource\":\"88888\",\"recordDevice\":\"MI 6\",\"deviceVersion\":\"8.0.0\",\"clientVersion\":\"2.1.2\",\"recordAccessMode\":\"WIFI\",\"recordImei\":\"DEVICE_ID_863254036386844\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"recordVersion\":\"2.1.2\",\"deviceImei\":\"DEVICE_ID_863254036386844\",\"systemName\":\"1\",\"loginName\":\"17789851177\",\"deviceModel\":\"MI 6\",\"carrieroperator\":\"\",\"recordLoginMode\":\"手机号登录\"}LT7";
		
		String md5 = MD5Util.md5(s1);
		System.out.println(md5);
	}

}
