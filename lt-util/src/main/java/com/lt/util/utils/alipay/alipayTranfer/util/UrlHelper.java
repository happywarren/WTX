package com.lt.util.utils.alipay.alipayTranfer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UrlHelper
{
//  public static String sortParamers(Map<String, String[]> parameterMap)
//  {
//    StringBuilder sb = new StringBuilder();
//    
//	@SuppressWarnings("rawtypes")
//	List keys = new ArrayList(parameterMap.keySet());
//    Collections.sort(keys);
//    for (int i = 0; 
//      i < keys.size(); i++) {
//      String parameterName = (String)keys.get(i);
//      String[] paramValueArray = (String[])parameterMap.get(parameterName);
//      buildStr(parameterName, paramValueArray, sb);
//    }
//
//    String sortedUrl = sb.toString();
//    return sortedUrl.substring(1);
//  }
//  
  public static String sortParamers(Map<String, String> parameterMap)
  {
    StringBuilder sb = new StringBuilder();
    
	@SuppressWarnings("rawtypes")
	List keys = new ArrayList(parameterMap.keySet());
    Collections.sort(keys);
    for (int i = 0; 
      i < keys.size(); i++) {
      String parameterName = (String)keys.get(i);
      String paramValue = parameterMap.get(parameterName);
      buildStr(parameterName, new String[]{paramValue}, sb);
    }

    String sortedUrl = sb.toString();
    return sortedUrl.substring(1);
  }

  public static String encode(Map<String, String[]> parameterMap, String enc)
    throws UnsupportedEncodingException
  {
    if (parameterMap.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    List<String> keys = new ArrayList<String>(parameterMap.keySet());
    for (String key : keys) {
      String[] paramValue = (String[])parameterMap.get(key);
      for (String val : paramValue) {
        sb.append("&").append(key).append("=").append(URLEncoder.encode(val, enc));
      }
    }
    return sb.toString().substring(1);
  }

  private static void buildStr(String paramName, String[] paramValueArray, StringBuilder sb) {
    if ((paramValueArray != null) && (paramValueArray.length > 0)) {
      Arrays.sort(paramValueArray);
      for (int i = 0; 
        i < paramValueArray.length; i++) {
        sb = sb.append("&").append(paramName).append("=");
        sb.append(paramValueArray[i] != null ? paramValueArray[i] : "");
      }
    } else {
      sb = sb.append("&").append(paramName).append("=");
    }
  }

  public static void main(String[] args) {
    Map param = new HashMap();
    param.put("ab", "dd");
    param.put("acb",  "dc");
    param.put("c",  "" );
    param.put("d",  "a");
    param.put("abbd",  "a");
    System.out.println(sortParamers(param));
  }
}
