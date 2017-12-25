package com.lt.manager.controller.user;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserBlacklist;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.model.Response;
import com.lt.manager.service.user.IUserBlacklistService;

/**
 * 
 * <br>
 * <b></b>UserBlacklistController<br>
 *   <br>
 */ 
@Controller
@RequestMapping(value="user")
public class UserBlacklistController {
	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IUserBlacklistService iUserBlacklistService; 
	
	
	/**
	 * 
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/queryBlacklist") 
	@ResponseBody
	public String  queryBlacklist(HttpServletRequest request,UserBlacklist param) throws Exception{
	    Response response = null;
		logger.info("查询用户黑名单信息param={}",param);
		try {
			Page<UserBlacklist> page = iUserBlacklistService.queryUserBlacklistPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询用户信息异常，e={}",e);
			
		}
		return response.toJsonString();
	}
	
	/**
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/addUserBlacklist")
	@ResponseBody
	public String addUserBlacklist(HttpServletRequest request,UserBlacklist param){
		
		Response response = null;
		logger.info("修改用户黑名单信息param={}",param);
		try {
			iUserBlacklistService.addUserBlacklist(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改用户黑名单信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	
		/**
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/editUserBlacklist")
	@ResponseBody
	public String editUserBlacklist(HttpServletRequest request,UserBlacklist param){
		
		Response response = null;
		logger.info("修改用户黑名单信息param={}",param);
		try {
			iUserBlacklistService.editUserBlacklist(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改用户黑名单信息异常，e={}",e);
		}
		return response.toJsonString();
	}
		
	/**
	 * 用户删除
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeUserBlacklist")
	@ResponseBody
	public String removeUserBlacklist(HttpServletRequest request,UserBlacklist param){
		
		Response response = null;
		String ids = request.getParameter("ids");
		String isCheckAll = request.getParameter("isCheckAll");
		logger.info("删除用户黑名单信息param={}",ids);
		if(ids == null && isCheckAll == null){
			 response = LTResponseCode.getCode(LTResponseCode.US20001);
			 return response.toJsonString();
		}
		try {
			//全選被選中根據條件查詢所有符合條件的記錄
			if(isCheckAll != null && ids == null ){
				UserBlacklist blackvo = new UserBlacklist();
				List<UserBlacklist> userBlacklist = iUserBlacklistService.queryUserBlacklistPage(param);
				 for (Iterator iter = userBlacklist.iterator(); iter.hasNext();) {
					 blackvo = (UserBlacklist)iter.next();
					 iUserBlacklistService.removeUserBlacklist(blackvo.getId());
					 }
				 response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			}
			      //單個刪除
			if(isCheckAll == null && !ids.contains(","))  {
				 iUserBlacklistService.removeUserBlacklist(ids);
				 response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			}
			if(isCheckAll == null && ids.contains(",")){
				  //多個刪除
				  String[] userBlacklistIDs = ids.split(",");
				  for (String id : userBlacklistIDs) { 
					  iUserBlacklistService.removeUserBlacklist(id);
			      }
				  response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改用户黑名单信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
		/**
	 * 用户删除
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/getUserBlacklist")
	@ResponseBody
	public String getUserBlacklist(Integer id){
		
		Response response = null;
		logger.info("获取用户黑名单信息param={}",id);
		
		try {
				UserBlacklist userBlacklist =  iUserBlacklistService.getUserBlacklist(id);
				response = LTResponseCode.getCode(LTResponseCode.SUCCESS,  userBlacklist);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取用户黑名单信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	
}
