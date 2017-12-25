package com.lt.manager.dao.news;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.user.UserBase;
import com.lt.model.news.NewsActInfo;
import com.lt.model.news.NewsActInfoLog;
import com.lt.model.news.NewsCmtReply;
import com.lt.model.news.NewsComment;
import com.lt.model.news.NewsPlate;
import com.lt.model.news.NewsPlateRelation;
import com.lt.model.news.NewsSection;

/**   
* 项目名称：lt-manager   
* 类名称：NewArticle   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年2月6日 下午4:35:38      
*/
public interface NewArticleInfoDao {
	
	/**
	 * 根据名称查询栏目信息
	 * @param sectionName
	 * @return    
	 * @return:       NewsSection    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:42:18
	 */
	public NewsSection getNewsSectionByName(String name);
	
	/**
	 * 新增栏目
	 * @param name    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:40:24
	 */
	public void addNewsSection(String name);
	
	/**
	 * 修改栏目
	 * @param newsSection    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午3:59:59
	 */
	public void uptNewsSection(NewsSection newsSection);
	
	/**
	 * 查询所有的栏目
	 * @return    
	 * @return:       List<NewsSection>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:55:45
	 */
	public List<NewsSection> qryAllSection();
	
	/**
	 * 根据名称删除栏目
	 * @param name 栏目名称
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午7:31:21
	 */
	public void deleteSectionById(Integer id);
	
	/**
	 * 根据名称查询标签信息
	 * @param tag
	 * @return    
	 * @return:       NewsSection    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午4:42:18
	 */
	public NewsPlate getNewstagByName(String tag);
	
	/**
	 * 新增标签
	 * @param tag    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:40:24
	 */
	public void addNewsTag(String tag);
	
	/**
	 * 修改标签
	 * @param newsPlate    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午3:59:18
	 */
	public void uptNewsTag(NewsPlate newsPlate);
	
	/**
	 * 查询所有的标签
	 * @return    
	 * @return:       List<NewsPlate>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午5:55:45
	 */
	public List<NewsPlate> qryAllTag();
	
	/**
	 * 根据名称删除标签
	 * @param name 栏目名称
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月6日 下午7:31:21
	 */
	public void deleteTagByName(Integer id);
	
	/**
	 * 批量插入文章标签关联表
	 * @param list    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:46:17
	 */
	public void insetPlateRalation(List<NewsPlateRelation> list);
	
	/**
	 * 批量删除文章标签关联表
	 * @param list    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午11:58:41
	 */
	public void deletePlateRelation(List<String> list);
	
	/**
	 * 根据新闻查询阅读用户的信息
	 * @param newsId
	 * @return    
	 * @return:       List<UserBase>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午8:38:29
	 */
	public List<UserBase> qryAllReadUser(String newsId);
	
	/**
	 * 单条新闻评论
	 * @param newsComment    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午9:26:08
	 */
	public void addComment(NewsComment newsComment);
	
	/**
	 * 增加用户操作日志
	 * @param newsActInfoLog    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午9:31:09
	 */
	public void addNewsActLog(NewsActInfoLog newsActInfoLog);
	
	/**
	 * 统计数据修改
	 * @param newsActInfo    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午9:37:03
	 */
	public void addNewsActInfo(NewsActInfo newsActInfo);
	
	/**
	 * 新增回复
	 * @param cmtReply    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午9:53:18
	 */
	public void addNewCmtReply(NewsCmtReply cmtReply);
	
	/**
	 * 校验新闻评论
	 * @param id
	 * @param status    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:07:04
	 */
	public void checkNewsComment(@Param("id")String id,@Param("status")String status);
	
	/**
	 * 校验评论回复
	 * @param id
	 * @param status    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:11:54
	 */
	public void checkNewsReply(@Param("id")String id,@Param("status")String status);
	
	/**
	 * 修改回复内容
	 * @param cmtReply    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:35:52
	 */
	public void updateNewsReply(NewsCmtReply cmtReply);
	
	/**
	 * 修改评论内容
	 * @param newsComment    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:39:19
	 */
	public void updateNewsComment(NewsComment newsComment);
	
	/**
	 * 批量复核新闻评论
	 * @param list
	 * @param status    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午11:40:41
	 */
	public void checkCommentMutil(@Param("list")List<String> list,@Param("status")Integer status); 
	
	/**
	 * 批量复核新闻回复
	 * @param list
	 * @param status    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午11:42:02
	 */
	public void checkReplyMutil(@Param("list")List<String> list,@Param("status")Integer status); 
	
	/**
	 * 插入新闻统计表
	 * @param articleInfo    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午2:00:08
	 */
	public void insertArticleInfo(NewsActInfo articleInfo);
}
