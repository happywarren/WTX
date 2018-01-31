package com.lt.util.utils.crypt;

import java.security.MessageDigest;

public class Md5Encrypter {
	
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String [] args){
		String str = MD5("{\"recordCarrierOperator\":\"\",\"recordIP\":\"192.168.125.100\",\"regSource\":\"88888\",\"recordDevice\":\"MI 6\",\"deviceVersion\":\"8.0.0\",\"clientVersion\":\"2.1.2\",\"recordAccessMode\":\"WIFI\",\"recordImei\":\"DEVICE_ID_863254036386844\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"recordVersion\":\"2.1.2\",\"deviceImei\":\"DEVICE_ID_863254036386844\",\"systemName\":\"1\",\"loginName\":\"17789851177\",\"deviceModel\":\"MI 6\",\"carrieroperator\":\"\",\"recordLoginMode\":\"手机号登录\"}LT7");
		System.out.println(str);
	}
}
