package com.lt.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.lt.manager.bean.sys.Staff;

/**
 * 获取系统操作人
 * @author jingwb
 *
 */
public class StaffUtil {

	public static Staff getStaff(HttpServletRequest request){
		 HttpSession session = request.getSession();
		 String json = (String)session.getAttribute("staff");
		 return JSONObject.parseObject(json, Staff.class);
	}
}
