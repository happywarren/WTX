package com.lt.manager.controller.user;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.UserService;
import com.lt.manager.service.user.IUserServiceManage;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理控制器
 * @author licy
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserServiceController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IUserServiceManage iUserService;
	
	
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/queryUserService")
	@ResponseBody
	public String queryUserService(HttpServletRequest request){
		
		Response response = null;
		logger.info("查询用户服务信息param={}",request.getParameter("userId"));
		try {
			if(request.getParameter("userId") == null){
				throw new LTException(LTResponseCode.US03101);
			}
			Page<UserService> page = (Page<UserService>)iUserService.findUserServiceById(request.getParameter("userId"));
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询用户用户服务异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/editUserService")
	@ResponseBody
	public String editUserInfo(HttpServletRequest request,UserService param){
		
		Response response = null;
		logger.info("修改用户服务信息param={}",param);
		try {
			iUserService.editUserService(param);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改用户服务信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeUserService")
	@ResponseBody
	public String removeUserInfo(HttpServletRequest request){
		
		Response response = null;
		logger.info("删除用户服务信息param={}",request.getParameter("ids"));
		
		 try {
				if( request.getParameter("ids").contains(",")){
					 String[] ids = request.getParameter("ids").split(",");
				for (String id : ids) { 
					iUserService.removeUserService(Integer.valueOf(id));
					//TODO  调用发短信方法
					//sendShortMsg(uvo.getTele());
				      } 
				}else{
					iUserService.removeUserService(Integer.valueOf(request.getParameter("ids")));
					//TODO  调用发短信方法
					//sendShortMsg(uvo.getTele());
				}
			} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("删除用户服务异常，e={}",e);
		}
		return response.toJsonString();
	}
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/insertUserService")
	@ResponseBody
	public String insertUserService(HttpServletRequest request,UserService param){
		
		Response response = null;
		logger.info("添加用户服务param={}",param);
		try {
			iUserService.addUserService(param);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("添加用户服务异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	@RequestMapping(value="/user/updateUserDailyTopAmt")
	@ResponseBody
	public String updateUserDailyTopAmt(String userId,Double amt){
		Response response = new Response(LTResponseCode.SUCCESS, "操作成功");
		logger.info("用户为：{},修改的金额为：{}",userId,amt);
		iUserService.updateUserDailyTopAmt(userId, amt);
		return response.toJsonString();
	}
	
	@RequestMapping(value="/user/getUserDailyTopAmt")
	@ResponseBody
	public String getUserDailyTopAmt(String userId){
		Response response = new Response(LTResponseCode.SUCCESS, "操作成功");
		String user_id = StringTools.formatStr(userId, "-1");
		Double amt = iUserService.getUserDailyTopAmt(user_id);
		response.setData(amt == null ? 0.0: amt);
		return response.toJsonString();
	}

	@RequestMapping(value="/withdrawApply")
	@ResponseBody
	public String withdrawApply(String userId, Double outAmount){
		Response response;
		try{
			if(outAmount == null || StringTools.isEmpty(userId)){
				throw new LTException(LTResponseCode.US03101);
			}
			response = iUserService.withdrawApply(userId, outAmount);
		}
		catch (Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
}
