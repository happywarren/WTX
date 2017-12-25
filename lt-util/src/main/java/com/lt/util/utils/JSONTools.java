/*
 * PROJECT NAME: lt-util
 * PACKAGE NAME: com.lt.util.utils
 * FILE    NAME: JSONTools.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.util.utils;

import com.alibaba.fastjson.JSON;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年12月28日 下午9:17:47
 * @version <b>1.0.0</b>
 */
public class JSONTools {
	
	public static String toJSON(Object obj){
		return JSON.toJSONString(obj);
	}
	
	public static Object parse(String json){
		return JSON.parse(json);
	}
}
