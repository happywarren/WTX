/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.controller.log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.lt.manager.bean.log.LtManagerLog;
import com.lt.manager.bean.log.OperationLog;
import com.lt.manager.bean.log.OperationLogQuery;
import com.lt.manager.bean.log.OperationLogVo;
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.dao.sys.AuthDao;
import com.lt.manager.dao.sys.MenusDao;
import com.lt.manager.dao.sys.StaffDao;
import com.lt.manager.service.log.LtManagerLogService;
import com.lt.manager.service.log.OperationLogService;
import com.lt.model.logs.LogInfo;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;

/**
 *
 * 描述:访问记录日志
 *
 */
@Controller
@RequestMapping("/operationLog")
public class OperationLogController {

	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private LtManagerLogService ltManagerLogService;
	
	@Autowired
	private StaffDao staffDao;

	@Autowired
	private AuthDao authDao;

	@Autowired
	private MenusDao menusDao;
	

	/**
	 * 后台所有访问日志
	 * @param request
	 * @param query
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/loadOperationLogList")
	@ResponseBody
	public String loadOperationLogList(HttpServletRequest request, OperationLogQuery query, Integer page, Integer rows) {
		Page<OperationLog> pageView = null;
		Page<OperationLogVo> pageViews = new Page<OperationLogVo>();
		Staff info = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		try {
			if (info.getId() != 999) {
				query.setStaffId(info.getId());
			}
			pageView = operationLogService.findStockList(query, page, rows);
			List<OperationLog> list = pageView.getResult();
			List<OperationLogVo> operationLogList = new ArrayList<OperationLogVo>();
			for (int i = 0; i < list.size(); i++) {
				OperationLogVo vo = new OperationLogVo();
				OperationLog str = list.get(i);
				BeanUtils.copyProperties(vo, str);
				if (StringTools.isNotEmpty(str.getUrl())) {
					String st = filterUnNumber(str.getUrl());
					if (st.contains("/lt-manager/")) {
						st = st.replace("/lt-manager/", "");
					}
					Auth auth = authDao.getAuthByUrl(st);
					if (auth != null) {
						Menus menus = menusDao.selectById(auth.getMenusId());
						vo.setOperaType(auth.getOperaType() + "");
						vo.setName(auth.getName());
						vo.setMenusId(auth.getMenusId() + "");
						if(menus != null){
							vo.setMenusName(menus.getName());
						}
					}
				}
				Staff staff = staffDao.queryStaffInfoById(str.getStaffId());
				if (staff != null) {
					vo.setStaffName(staff.getName());
				}
				operationLogList.add(vo);
			}
			//			pageView.clear();
			pageViews.setPageNum(pageView.getPageNum());
			pageViews.setPageSize(pageView.getPageSize());
			pageViews.setTotal(pageView.getTotal());
			pageViews.addAll(operationLogList);
		} catch (Exception e) {
			logger.error("查询数据失败",e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return JqueryEasyUIData.init(pageViews);
	}


	/**
	*
	* @param str
	*         需要过滤的字符串
	* @return
	* @Description:过滤数字以外的字符
	*/
	public static String filterUnNumber(String str) {
		String regEx = "\\d+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	
	
	/**
	 * 所有接口访问日志  用户操作日志
	 * @param request
	 * @param logInfo
	 * @param page
	 * @param rows
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/selectLog")
	@ResponseBody
	public String selectLog(HttpServletRequest request, LogInfo logInfo, Integer page, Integer rows,String startDate,String endDate) {
		if(!StringTools.isNotEmpty(rows)){
			rows = 20;
		}
		if(!StringTools.isNotEmpty(page)){
			page = 1;
		}
		Page<LogInfo> pageView = operationLogService.findList(logInfo, page, rows, startDate, endDate);
		return JqueryEasyUIData.init(pageView);
	}
	
	/**
	 * 查询用户登录日志
	 * @param request
	 * @param logInfo
	 * @param page
	 * @param rows
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/selectUserOperateLog")
	@ResponseBody
	public String selectUserOperateLog(HttpServletRequest request, UserOperateLog logInfo, Integer page, Integer rows,String startDate,String endDate) {
		if(!StringTools.isNotEmpty(rows)){
			rows = 20;
		}
		if(!StringTools.isNotEmpty(page)){
			page = 1;
		}
		Page<UserOperateLog> pageView = operationLogService.findUserOperateLogList(logInfo, page, rows, startDate, endDate);
		return JqueryEasyUIData.init(pageView);
	}
	
	/**
	 * 管理系统日志
	 * @param request
	 * @param log
	 * @param page
	 * @param rows
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/selectManagerLog")
	@ResponseBody
	public String selectManagerLog(HttpServletRequest request, LtManagerLog log, Integer page, Integer rows,String startDate,String endDate) {
		if(!StringTools.isNotEmpty(rows)){
			rows = 20;
		}
		if(!StringTools.isNotEmpty(page)){
			page = 1;
		}
		Page<LtManagerLog> pageView = ltManagerLogService.selectAllByParm(log.getIsSuccessed(), log.getUserName(), 
				log.getLastLoginName(), log.getStaffId(), log.getLastLoginId(), log.getIp(), startDate, endDate, rows, page);				
		return JqueryEasyUIData.init(pageView);
	}
	
	/**
	 * 用户变更日志
	 * @param request
	 * @param userId
	 * @param updateType
	 * @param page
	 * @param rows
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/selectUserUpdateLog")
	@ResponseBody
	public String selectUserUpdateLog(HttpServletRequest request, String userId,Integer updateType, Integer page, Integer rows,String startDate,String endDate) {
		if(!StringTools.isNotEmpty(rows)){
			rows = 20;
		}
		if(!StringTools.isNotEmpty(page)){
			page = 1;
		}
		Page<UserUpdateInfoLog> pageView = operationLogService.findUserUpdateInfoLogList(userId, updateType,page, rows, startDate, endDate);
		return JqueryEasyUIData.init(pageView);
	}
}
