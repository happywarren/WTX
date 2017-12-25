package com.lt.manager.dao.news;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.news.NewsArticleCmtLikeRlyCount;
import com.lt.manager.bean.news.NewsArticleCmtReplyVo;
import com.lt.manager.bean.news.NewsArticleDetail;
import com.lt.manager.bean.news.NewsArticleVo;
import com.lt.manager.bean.news.NewsUserComment;
import com.lt.manager.bean.news.NewsUserCommentReply;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleMainDao   
* 类描述：   文章信息主查询类
* 创建人：yuanxin   
* 创建时间：2017年2月7日 上午9:55:40      
*/
public interface NewsArticleMainDao {
	
	/**
	 * 根据条件查询文章内容
	 * @param newsArticle
	 * @return    
	 * @return:       List<NewsArticleVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月7日 上午10:23:02
	 */
	public List<NewsArticleVo> qryNewsPageList(NewsArticleVo newsArticle);
	
	/**
	 * 根据条件查询文章内容(包含简介)
	 * @param newsArticle
	 * @return    
	 * @return:       List<NewsArticleVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月7日 上午10:23:02
	 */
	public List<NewsArticleCmtReplyVo> qryNewsCmtRlyPageList(NewsArticleVo newsArticle);
	
	/**
	 * 查询条件文章内容（汇总）
	 * @param newsArticle
	 * @return    
	 * @return:       List<NewsArticleVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月7日 下午2:10:49
	 */
	public Long qryNewsPageListCount(NewsArticleVo newsArticle);
	
	/**
	 * 根据文章id查询文章具体信息
	 * @param NewsId
	 * @return    
	 * @return:       NewsArticleDetail    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午9:35:53
	 */
	public NewsArticleDetail qryNewsDetailById(Integer newsId);
	
	/**
	 * 插入文章
	 * @param articleDetail
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:27:38
	 */
	public void insertNewsArticle(NewsArticleDetail articleDetail);
	
	/**
	 * 批量删除文章
	 * @param ids    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午11:49:58
	 */
	public void deleteNewsArticle(List<String> ids);
	
	/**
	 * 批量置顶文章
	 * @param ids
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:31:08
	 */
	public Integer topNewsArticle(Map<String,Object> map);
	
	/**
	 * 批量复核文章
	 * @param ids
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:40:41
	 */
	public Integer checkedNewsArticle(Map<String,Object> map);
	
	/**
	 * 批量复核文章
	 * @param ids
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:40:41
	 */
	public Integer checkedNewsArticleShow(Map<String,Object> map);
	
	/**
	 * 修改文章
	 * @param articleDetail
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午2:16:50
	 */
	public void updateNewsArticle(NewsArticleDetail articleDetail);
	
	/**
	 * 查询统计数据
	 * @param date
	 * @return    
	 * @return:       List<NewsArticleCmtLikeRlyCount>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午4:48:48
	 */
	public List<NewsArticleCmtLikeRlyCount> qryRlyCmtLikeCount(String date);
	
	/**
	 * 根据新闻id查询评论内容
	 * @param newsId 新闻id
	 * @return    
	 * @return:       List<NewsUserComment>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午7:32:41
	 */
	public List<NewsUserComment> qryNewsCommentByNewsId(@Param("newsId")String newsId,@Param("status")Integer status);
	
	/**
	 * 根据评论ID查询关联的回复内容
	 * @param commentId
	 * @return    
	 * @return:       List<NewsUserCommentReply>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午8:02:42
	 */
	public List<NewsUserCommentReply> qryNewsCommentReplyByComment(@Param("commentId")String commentId,@Param("status")Integer status);
}
