package com.lt.manager.service.news;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.news.NewsArticleCmtLikeRlyCount;
import com.lt.manager.bean.news.NewsArticleCmtReplyVo;
import com.lt.manager.bean.news.NewsArticleDetail;
import com.lt.manager.bean.news.NewsArticleVo;
import com.lt.manager.bean.news.NewsUserComment;
import com.lt.manager.bean.news.NewsUserCommentReply;
import com.lt.model.news.NewsCmtReply;
import com.lt.model.news.NewsComment;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleMainService   
* 类描述： 文章评论处理类  
* 创建人：yuanxin   
* 创建时间：2017年2月7日 上午9:51:50      
*/
public interface NewsArticleMainService {
	
	/**
	 * 查询分页的新闻列表
	 * @param newsArticleVo 查询条件
	 * @return    
	 * @return:       Page<NewsArticleVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月7日 上午9:53:52
	 */
	public Page<NewsArticleVo> qryNewsInfoPage(NewsArticleVo newsArticleVo) throws LTException;
	
	/**
	 * 查询分页的新闻列表
	 * @param newsArticleVo 查询条件
	 * @return    
	 * @return:       Page<NewsArticleVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月7日 上午9:53:52
	 */
	public Page<NewsArticleCmtReplyVo> qryNewsInfoCmtRlyPage(NewsArticleVo newsArticleVo) throws LTException;
	
	/**
	 * 根据文章id查询文章详情
	 * @param newsArticleId
	 * @return    
	 * @return:       NewsArticleDetail    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午9:34:44
	 */
	public NewsArticleDetail qryNewsDetailById(Integer newsArticleId) throws LTException;
	
	/**
	 * 新增文章
	 * @param plateIds 标签名称
	 * @param articleDetail
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:20:26
	 */
	public void addNewsArticle(String plateIds,NewsArticleDetail articleDetail) throws LTException;
	
	/**
	 * 删除文章（可批量）
	 * @param plateIds
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午11:48:11
	 */
	public void deleteNewsArticle(String plateIds) throws LTException;
	
	/**
	 * 置顶文章（批量）
	 * @param plateIds
	 * @return
	 * @throws LTException    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:28:43
	 */
	public void topNewsArticle(String plateIds,Integer status) throws LTException;
	
	/**
	 * 复核文章（批量）
	 * @param plateIds
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:39:52
	 */
	public void checkedNewsArticle(String plateIds,String status) throws LTException;
	
	/**
	 * 修改文章内容
	 * @param plateIds 标签
	 * @param articleDetail
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:48:46
	 */
	public void updateNewsArticle(String plateIds,NewsArticleDetail articleDetail) throws LTException;
	
	/**
	 * 统计点赞，评论，阅读数量
	 * @param date
	 * @return
	 * @throws LTException    
	 * @return:       List<NewsArticleCmtLikeRlyCount>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午4:44:00
	 */
	public List<NewsArticleCmtLikeRlyCount> qryCmtLikeRlyCountByDate(Date date)throws LTException;
	
	/**
	 * 查询新闻对应的所有的评论
	 * @param newsId 新闻id
	 * @return
	 * @throws LTException    
	 * @return:       List<NewsUserComment>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午7:21:08
	 */
	public List<NewsUserComment> qryNewsComment(String newsId,Integer status) throws LTException;
	
	/**
	 * 根据评论id查询回复内容
	 * @param commentId
	 * @return
	 * @throws LTException    
	 * @return:       List<NewsUserCommentReply>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午7:48:06
	 */
	public List<NewsUserCommentReply> qryNewsCommentReply(String commentId,Integer status) throws LTException;
	
	/**
	 * 单条评论回复
	 * @param newsComment
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午9:24:34
	 */
	public void addNewsComment(NewsComment newsComment) throws LTException;
	
	/**
	 * 单条回复
	 * @param newsCmtReply
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午9:50:32
	 */
	public void addNewsReply(NewsCmtReply newsCmtReply) throws LTException;
	
	/**
	 * 复核新闻评论（单条）
	 * @param commentId
	 * @param status
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:05:39
	 */
	public void checkNewsComment(String commentId,String status) throws LTException;
	
	/**
	 * 复核评论（单条）
	 * @param commentId
	 * @param status
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:18:57
	 */
	public void checkNewsReply(String commentId,String status) throws LTException;
	
	/**
	 * 编辑单条回复
	 * @param newsCmtReply
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:28:08
	 */
	public void updateNewsReply(NewsCmtReply newsCmtReply) throws LTException;
	
	/**
	 * 编辑单条评论
	 * @param newsCmtReply
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午10:40:15
	 */
	public void updateNewsComment(NewsComment newsComment) throws LTException;
	
	/**
	 * 批量复核评论和回复
	 * @param jsonString
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月14日 上午11:24:00
	 */
	public void chkCommentReply(String commentId,String replyId,String status) throws LTException;

	/**
	 * 初始化品牌方法
	 */
	void init();
}
