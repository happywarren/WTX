package com.lt.manager.inteceptor;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.lt.manager.bean.log.OperationLog;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.dao.sys.StaffDao;
import com.lt.manager.service.log.OperationLogService;
import com.lt.util.LoggerTools;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.StringTools;

/**
 *
 * 描述:操作日志记录
 *
 * @author  C.C
 * @created 2015年6月17日 上午10:47:00
 * @since   v1.0.0
 */
public class OperationInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private StaffDao dao;
	
	private ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		HttpSession session = request.getSession();
		Staff staff = JSON.parseObject((String) session.getAttribute("staff"), Staff.class);
		final OperationLog ol = new OperationLog();
		ol.setAccessTime(new Date());
		if (staff != null) {
			ol.setStaffId(staff.getId());
			ol.setStaffName(staff.getName());
			ol.setOuterNetIp(IpUtils.getUserIP(request));
			ol.setIntranetIp(request.getRemoteAddr());
		}
		try {
			String str = request.getQueryString();
			if (StringTools.isNotEmpty(str)) {
				str = URLDecoder.decode(str, "UTF-8");
			}
			ol.setParameters(str);
		} catch (UnsupportedEncodingException e) {
			logger.error("OperationInterceptor parameters decode error", e);
		}
		ol.setUrl(request.getRequestURI());
		if (handler instanceof HandlerMethod) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					operationLogService.saveOperationLog(ol);
				}
			});
		}
		return true;
	}
}
