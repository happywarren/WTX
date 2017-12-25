package com.lt.manager.controller.news;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.manager.bean.user.UserBase;
import com.lt.manager.service.news.NewArticleInfoService;
import com.lt.manager.service.user.IUserManageService;
import com.lt.model.news.NewsPlate;
import com.lt.model.news.NewsSection;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleInfoController   
* 类描述：  资讯信息处理类
* 创建人：yuanxin   
* 创建时间：2017年2月6日 下午7:25:39      
*/
@Controller
@RequestMapping(value="/tagAndOthers")
public class NewsArticleInfoController {

	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private NewArticleInfoService newArticleInfoService;
	@Autowired
	private IUserManageService userMangeService;
	
	/**
	 * 新增栏目
	 * @param name
	 * @return    
	 * @return:       String   栏目名称 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:50:25
	 */
	@RequestMapping("/addNewSection")
	@ResponseBody
	public String addNewSection(String name){
		Response response = null;
		try{
			boolean flag = newArticleInfoService.ifExistSection(name);
			if(flag){
				throw new LTException(LTResponseCode.NAY00001);
			}
			
			newArticleInfoService.addNewsSection(name);
			
			response = new  Response(LTResponseCode.SUCCESS, "添加成功");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 新增栏目
	 * @param name
	 * @return    
	 * @return:       String   栏目名称 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:50:25
	 */
	@RequestMapping("/uptNewSection")
	@ResponseBody
	public String uptNewSection(String name,Integer sectionId){
		Response response = null;
		try{
			boolean flag = newArticleInfoService.ifExistSection(name);
			if(flag){
				throw new LTException(LTResponseCode.NAY00001);
			}
			
			newArticleInfoService.updateNewsSection(name, sectionId);
			
			response = new  Response(LTResponseCode.SUCCESS, "添加成功");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询所有栏目信息
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午8:28:22
	 */
	@RequestMapping("/qryAllNewSection")
	@ResponseBody
	public String qryAllNewSection(){
		Response response = null;
		try{
			List<NewsSection> list = newArticleInfoService.qryAllSection();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 删除栏目
	 * @param name
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午7:23:06
	 */
	@RequestMapping("/deleteNewSection")
	@ResponseBody
	public String deleteNewSection(Integer id){
		Response response = null;
		try{
			newArticleInfoService.deleteNewSection(id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 新增栏目
	 * @param name
	 * @return    
	 * @return:       String   栏目名称 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:50:25
	 */
	@RequestMapping("/addNewsTag")
	@ResponseBody
	public String addNewsTag(String tag){
		Response response = null;
		try{
			boolean flag = newArticleInfoService.ifExistTag(tag);
			if(flag){
				throw new LTException(LTResponseCode.NAY00003);
			}
			
			newArticleInfoService.addNewsTag(tag);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 新增栏目
	 * @param name
	 * @return    
	 * @return:       String   栏目名称 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:50:25
	 */
	@RequestMapping("/uptNewsTag")
	@ResponseBody
	public String uptNewsTag(String tag,Integer tagId){
		Response response = null;
		try{
			boolean flag = newArticleInfoService.ifExistTag(tag);
			if(flag){
				throw new LTException(LTResponseCode.NAY00003);
			}
			
			newArticleInfoService.updateNewsTag(tag,tagId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询所有标签信息
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午8:27:49
	 */
	@RequestMapping("/qryAllNewsTag")
	@ResponseBody
	public String qryAllNewsTag(){
		Response response = null;
		try{
			List<NewsPlate> list = newArticleInfoService.qryAllTag();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 删除标签
	 * @param name
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午7:23:06
	 */
	@RequestMapping("/deleteNewsTag")
	@ResponseBody
	public String deleteNewsTag(Integer id){
		Response response = null;
		try{
			newArticleInfoService.deleteNewTag(id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/checkUserIsExit")
	@ResponseBody
	public String checkUserIsExit(String userId){
		Response response = null;
		try{
			UserBase base = userMangeService.getUserBase(userId);
			response = new Response(LTResponseCode.SUCCESS,"查询成功",base == null ?false:true);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/qryReadUser")
	@ResponseBody
	public String qryReadUser(String newsId){
		Response response = null;
		try{
			List<UserBase> base = newArticleInfoService.qryReadUserInfo(newsId);
			response = new Response(LTResponseCode.SUCCESS,"查询成功",base);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
