package com.lt.manager.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lt.manager.bean.user.UserServiceMapper;
import com.lt.manager.service.user.IUserServiceMapper;
import com.lt.model.user.ServiceContant;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 用户管理控制器
 * @author licy
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserServiceMapperController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IUserServiceMapper iUserServiceMapper;
	
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findUserService")
	@ResponseBody
	public String findUserService(HttpServletRequest request){
		
		Response response = null;
		List<UserServiceMapper> serviceList = null; 
		if(request.getParameter("userId") == null){
			logger.info("查询用户服务Id为空");
			throw new LTException(LTResponseCode.US20001);
		}
		try {
			serviceList = iUserServiceMapper.findUserServiceById(request.getParameter("userId"));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, serviceList);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询用户服务信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 修改用户角色
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/modifyUserServiceMapper")
	@ResponseBody
	public String modifyUserServiceMapper(HttpServletRequest request){
		Response response = null;
		try{
			String userId = request.getParameter("userId");
			String codes = request.getParameter("codes");//服务串
			String all = request.getParameter("all");//目前支持的服务
			if(StringTools.isEmpty(all) || StringTools.isEmpty(userId)){
				throw new LTException(LTResponseCode.US20001);
			}
			all = ServiceContant.CASH_SERVICE_CODE+","+ServiceContant.SCORE_SERVICE_CODE;
			iUserServiceMapper.modifyUserServiceMapper(userId, codes, all);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("异常，e={}",e);
		}
		return response.toJsonString();
	}
}
