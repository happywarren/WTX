package com.lt.manager.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.UserService;
import com.lt.manager.bean.user.UserTag;
import com.lt.manager.service.user.IUserTagManageService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.model.Response;

/**
 * 用户管理控制器
 * @author licy
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserTagServiceController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IUserTagManageService iUserTagManageService;
	
	
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/queryUserTag")
	@ResponseBody
	public String queryUserTag(HttpServletRequest request){
		
		Response response = null;
		try {
			Page<UserTag> page = (Page<UserTag>) iUserTagManageService.findUserTagService();
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询用户标签信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/editTag")
	@ResponseBody
	public String editTag(HttpServletRequest request,UserTag param){
		
		Response response = null;
		logger.info("修改用户标签param={}",param);
		try {
			iUserTagManageService.editUserTagService(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改用户标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeTag")
	@ResponseBody
	public String removeTag(HttpServletRequest request){
		
		Response response = null;
		logger.info("删除用户标签param={}",request.getParameter("ids"));
		
		 try {
				if( request.getParameter("ids").contains(",")){
					 String[] ids = request.getParameter("ids").split(",");
				for (String id : ids) { 
					iUserTagManageService.removeUserTagService(Integer.valueOf(id));
					//TODO  调用发短信方法
					//sendShortMsg(uvo.getTele());
				      } 
				}else{
					iUserTagManageService.removeUserTagService(Integer.valueOf(request.getParameter("ids")));
					//TODO  调用发短信方法
					//sendShortMsg(uvo.getTele());
				}
			} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改用户标签，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/insertTag")
	@ResponseBody
	public String insertTag(HttpServletRequest request,UserTag param){
		
		Response response = null;
		logger.info("修改用户标签param={}",param);
		try {
			iUserTagManageService.addUserTagService(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改用户标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
}
