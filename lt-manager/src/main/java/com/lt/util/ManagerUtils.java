package com.lt.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManagerUtils {
	/**
	 * 短信手机对应验证码
	 */
	public static Map<String,String> teleCodeMap = new ConcurrentHashMap<String, String>();
}
