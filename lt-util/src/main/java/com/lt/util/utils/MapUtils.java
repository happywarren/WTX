package com.lt.util.utils;

import java.util.Map;
import java.util.Map.Entry;

/**
 * map工具类
 * 
 * @author yubei
 *
 */
public class MapUtils {

	/**
	 * url参数拼接
	 * 
	 * @param args
	 * @return
	 */
	public static String convertUrlParam(Map<String, Object> param) {
		StringBuilder buf = new StringBuilder();
		if (param != null) {
			for (Entry<String, Object> entry : param.entrySet()) {
				String name = entry.getKey();
				Object value = entry.getValue();
				String valueStr = null;
				if (name != null && value != null) {
					if (value instanceof String) {
						valueStr = (String) value;
					} else {
						valueStr = value.toString();
					}
					if (valueStr != null) {
						if (buf.length() > 0) {
							buf.append("&");
						} else {
							buf.append("");
						}
						buf.append(name);
						buf.append("=");
						buf.append(valueStr);
					}
				}

			}
		}
		return buf.toString();
	}

}
