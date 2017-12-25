package com.lt.manager.service.news;

import java.util.List;

import com.lt.manager.bean.user.UserBase;
import com.lt.model.news.NewsPlate;
import com.lt.model.news.NewsPlateRelation;
import com.lt.model.news.NewsSection;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-manager   
* 类名称：NewArticleService   
* 类描述：栏目标签信息处理类   
* 创建人：yuanxin   
* 创建时间：2017年2月6日 下午3:54:55      
*/
public interface NewArticleInfoService {
	
	/**
	 * 判断是否存在该栏目 true 存在false 不存在
	 * @param name 栏目名称
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:43:28
	 */
	public boolean ifExistSection(String name) throws LTException;
	
	/**
	 * 新增栏目
	 * @param name 栏目名称
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:39:08
	 */
	public void addNewsSection(String name) throws LTException;
	
	/**
	 * 编辑栏目
	 * @param name
	 * @param id
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午4:00:24
	 */
	public void updateNewsSection(String name,Integer id) throws LTException;
	
	/**
	 * 编辑标签
	 * @param name
	 * @param id
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午4:00:52
	 */
	public void updateNewsTag(String name,Integer id) throws LTException;
	
	/**
	 * 查询所有的栏目信息
	 * @return
	 * @throws LTException    
	 * @return:       List<NewsSection>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:54:31
	 */
	public List<NewsSection> qryAllSection() throws LTException;
	
	/**
	 * 删除栏目信息
	 * @param name 栏目名称
	 * @return
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午7:39:56
	 */
	public void deleteNewSection(Integer name) throws LTException;
	
	/**
	 * 判断是否存在该标签 true 存在false 不存在
	 * @param tag 标签名称
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:43:28
	 */
	public boolean ifExistTag(String tag) throws LTException;
	
	/**
	 * 新增栏目
	 * @param tag 栏目名称
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:39:08
	 */
	public void addNewsTag(String tag) throws LTException;
	
	/**
	 * 查询所有的标签信息
	 * @return
	 * @throws LTException    
	 * @return:       List<NewsSection>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:54:31
	 */
	public List<NewsPlate> qryAllTag() throws LTException;
	
	/**
	 * 删除标签信息
	 * @param name 标签名称
	 * @return
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午7:39:56
	 */
	public void deleteNewTag(Integer name) throws LTException;
	
	/**
	 * 批量插入标签和文章关联信息
	 * @param list
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:45:17
	 */
	public void insertPlateRelation(List<NewsPlateRelation> list) throws LTException;
	
	/**
	 * 查询阅读用户的基本信息
	 * @param newsId
	 * @return
	 * @throws LTException    
	 * @return:       List<UserBase>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午8:35:14
	 */
	public List<UserBase> qryReadUserInfo(String newsId) throws LTException;
}
