package com.lt.manager.inteceptor;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import sun.util.logging.resources.logging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.manager.bean.PrivilegesManager;
import com.lt.manager.bean.sys.PurView;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.controller.sys.LoginController;
import com.lt.util.ManagerUtils;
import com.lt.util.annotation.LimitLess;
import com.lt.util.error.LTResponseCode;

/**
 * 访问权限拦截器
 * @author Administrator
 *
 */
public class PrivilegesInteceptor extends HandlerInterceptorAdapter {
	
	private final String AUTH = "authid"; 
	
	private final String SESSION = "userSession";
	
	private final String STAFF = "staff";
	
	private final String PURVIEW = "userPurView";
	
	private final String TOKEN = "token";
	
	private String excuUrls;
	
	public String getExcuUrls() {
		return excuUrls;
	}

	public void setExcuUrls(String excuUrls) {
		this.excuUrls = excuUrls;
	}

	@SuppressWarnings({ "unchecked" })
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		if(handler instanceof HandlerMethod){
			HandlerMethod method=(HandlerMethod) handler;
			if(method.getMethod().getAnnotation(LimitLess.class)!=null){
				return true;
			}
		}
		//过滤第三方支付异步通知请求, 提现失败异步通知等 在app_mvc.xml中配置
		String[] execs = excuUrls.split(",");
		for(String url : execs){
			if(request.getRequestURI().contains(url))
				return true;
		}
		if (handler instanceof HandlerMethod) {
			HttpSession session = request.getSession();
			Integer userId = (Integer) session.getAttribute(SESSION);
			String token =  (String) session.getAttribute(TOKEN);
			System.out.println("userId:"+userId);
			if (userId == null) {
				response.getWriter().print(LTResponseCode.getCode(LTResponseCode.MA00001).toJsonString());
				return false ;
			}
			String uuid = LoginController.map_token.get(userId+"");
			if(!token.equals(uuid)){
				response.getWriter().print(LTResponseCode.getCode(LTResponseCode.MA00022).toJsonString());
				return false;
			}
			if (userId == 999) {
				return true;
			}
			/* 检查用户是否登录  */
			session.setMaxInactiveInterval(30 * 60);
			Staff staff = JSON.parseObject((String) session.getAttribute(STAFF), Staff.class);
			System.out.println("staff:"+JSONObject.toJSONString(null));
			if (staff == null) {
				response.getWriter().print(LTResponseCode.getCode(LTResponseCode.MA00001).toJsonString());
				return false;
			}
			/* 获取请求的最佳匹配路径 */
			String urlTpl = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			System.out.println("本次请求匹配的路径为：" + urlTpl);
//			/* 获取@PathVariable的值 */
//			Map<String, Object> attribute = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//			if (attribute == null || attribute.size() <= 0) {
//				return true;
//			}
//			Object value = attribute.get(AUTH);
//			if (value == null) {
//				return true;
//			}
			int authId = 0;
			/* 权限校验 */
			PurView purView = JSON.parseObject((String) session.getAttribute(PURVIEW), PurView.class);
			if (purView == null) {
				response.getWriter().print(LTResponseCode.getCode(LTResponseCode.MA00002).toJsonString());
				return false;
			}
			boolean match = PrivilegesManager.getInstance().authValid(authId, purView, urlTpl);
			if (match) {
				return true;
			}
			response.getWriter().print(LTResponseCode.getCode(LTResponseCode.MA00002).toJsonString());
			return false;
		}
		return true;
	}
}
