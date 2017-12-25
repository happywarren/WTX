package com.lt.manager.controller.user;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserTag;
import com.lt.manager.bean.user.UserTagMapper;
import com.lt.manager.service.user.IUserTagManageService;
import com.lt.manager.service.user.IUserTagMapper;
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
public class UserTagMapperController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IUserTagMapper iUserTagMapper;
	
	@Autowired
	private IUserTagManageService iUserTagManageService;
	
	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findUserTag")
	@ResponseBody
	public String findUserTag(HttpServletRequest request){
		
		Response response = null;
		List<UserTagMapper> tagList = null; 
		List<UserTag> userTagList = null; 
		if(request.getParameter("userId") == null){
			logger.info("查询用户标签Id为空");
			throw new LTException(LTResponseCode.US20001);
		}
		try {
			String userId =  request.getParameter("userId");
			if(userId != null){
				tagList = iUserTagMapper.findUserTagById(userId);
			
				userTagList = iUserTagManageService.findUserTagService();
				
				if(tagList.size() == 0){
					return LTResponseCode.getCode(LTResponseCode.SUCCESS, userTagList).toJsonString();
				}
				Set tagSet = new HashSet();
				
				UserTagMapper utm = new UserTagMapper();
				UserTag ut = new UserTag();
				
					 for (Iterator iter2 = userTagList.iterator(); iter2.hasNext();){
						 ut = (UserTag)iter2.next();
						 for (Iterator iter = tagList.iterator(); iter.hasNext();)
						 {
							 utm =  (UserTagMapper)iter.next();
							 if(utm.getTagId().equals(ut.getId()) ){
								 ut.setIsuse(1);
							 }
							 tagSet.add(ut);
						 }
				 }
				response = LTResponseCode.getCode(LTResponseCode.SUCCESS, tagSet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询用户标签异常，e={}",e);
		}
		return response.toJsonString();
	}

	/**
	 * 用户列表查询
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/addUserTag")
	@ResponseBody
	public String addUserTag(HttpServletRequest request,UserTagMapper param){
		
		Response response = null;
		logger.info("添加用户标签信息param={}",param);
		try {
			//先判断标签库里是否含有该标签
			List<UserTag>	userTagList = iUserTagManageService.findUserTagService();
			UserTag ut = new UserTag();
			for (Iterator iter = userTagList.iterator(); iter.hasNext();){
				 ut = (UserTag)iter.next();
				 if(ut.getName().equals(param.getName())){
					return LTResponseCode.getCode(LTResponseCode.USL2007).toJsonString();
				 }
			 }
			
			//如果不存在向标签库加入该标签
			UserTag utagvo = new UserTag();
			utagvo.setName(param.getName());
			iUserTagManageService.addUserTagService(utagvo);
			
			param.setTagId(utagvo.getId());
			//如果不存在向用户标签管理表插入数据建立关联关系
			iUserTagMapper.addUserTagMapper(param);
			utagvo.setTagId(utagvo.getId());
			utagvo.setIsuse(1);
			//返回刚刚添加的标签
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,utagvo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("添加用户标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 *给用户设置标签
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/setUserTag")
	@ResponseBody
	public String setUserTag(HttpServletRequest request,UserTagMapper param){
		
		Response response = null;
		logger.info("设置用户标签信息param={}",param);
		try {
			iUserTagMapper.addUserTagMapper(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("设置用户标签异常，e={}",e);
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
	@RequestMapping(value="/user/removeUserTag")
	@ResponseBody
	public String removeUserTag(HttpServletRequest request,UserTagMapper param){
		
		Response response = null;
		logger.info("删除用户标签信息param={}",param);
		try {
			iUserTagMapper.removeUserTagMapper(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改用户信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除标签
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/deleteTag")
	@ResponseBody
	public String deleteTag(HttpServletRequest request){
		
		Response response = null;
		try {
			String id = request.getParameter("id");
			if(StringTools.isEmpty(id)){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			iUserTagMapper.removeUserTag(Integer.valueOf(id));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("删除标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑标签
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/modifyTag")
	@ResponseBody
	public String modifyTag(HttpServletRequest request,UserTag param){
		
		Response response = null;
		try {
			if(param.getId() == null){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			iUserTagMapper.editUserTag(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("编辑标签异常，e={}",e);
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
	@RequestMapping(value="/user/deleteUserTag")
	@ResponseBody
	public String deleteUserTag(HttpServletRequest request){
		
		Response response = null;
		try {
			Integer tagId =  Integer.valueOf(request.getParameter("tagId"));
			UserTagMapper utmVO = new UserTagMapper();
			if(tagId != null){
				List<UserTagMapper> tagList = iUserTagMapper.findUserTag();
				for(Iterator lter = tagList.iterator();lter.hasNext();){
					utmVO = (UserTagMapper)lter.next();
					if(utmVO.getTagId() == tagId){
						return LTResponseCode.getCode(LTResponseCode.USL2006).toJsonString();
					}
				}
						iUserTagManageService.removeUserTagService(tagId);
			}
			
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("删除用户标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
