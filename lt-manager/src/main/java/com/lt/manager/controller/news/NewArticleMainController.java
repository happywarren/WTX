package com.lt.manager.controller.news;


import java.util.Date;
import java.util.List;

import com.lt.manager.bean.sys.Staff;
import com.lt.util.StaffUtil;
import com.lt.util.annotation.LimitLess;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.enums.news.NewsArticleEnum;
import com.lt.manager.bean.news.NewsArticleCmtLikeRlyCount;
import com.lt.manager.bean.news.NewsArticleCmtReplyVo;
import com.lt.manager.bean.news.NewsArticleDetail;
import com.lt.manager.bean.news.NewsArticleVo;
import com.lt.manager.bean.news.NewsUserComment;
import com.lt.manager.bean.news.NewsUserCommentReply;
import com.lt.manager.service.news.NewsArticleMainService;
import com.lt.model.news.NewsCmtReply;
import com.lt.model.news.NewsComment;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.model.Response;

import javax.servlet.http.HttpServletRequest;

/**   
* 项目名称：lt-manager   
* 类名称：NewsMainController   
* 类描述： 资讯管理类 
* 创建人：yuanxin   
* 创建时间：2017年2月6日 下午3:50:39      
*/

@Controller
@RequestMapping(value="/NewsArticle")
public class NewArticleMainController {
	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private NewsArticleMainService newsArticleMainServiceImpl;
	
	/***
	 * 查询新闻（分页）
	 * @param newsArticle
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:21:57
	 */
	@RequestMapping("/qryArticle")
	@ResponseBody
	public String qryArticle(NewsArticleVo newsArticle){
		Response response = null ;
		try{
			Page<NewsArticleVo>  page = newsArticleMainServiceImpl.qryNewsInfoPage(newsArticle);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 查询新闻（详细）
	 * @param articleId
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:22:14
	 */
	@RequestMapping("/qryArticleDetail")
	@ResponseBody
	public String qryArticleDetail(String articleId){
		Response response = null ;
		try{
			Integer id = Integer.parseInt(articleId);
			NewsArticleDetail articleDetail = newsArticleMainServiceImpl.qryNewsDetailById(id);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", articleDetail);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	/**
	 * 新增新闻
	 * @param plateIds 多个标签id 以逗号分隔
	 * @param articleDetail
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 上午10:22:31
	 */
	@RequestMapping("/addNewsArticle")
	@ResponseBody
	public String addNewsArticle(HttpServletRequest request,String plateIds, NewsArticleDetail articleDetail){
		Response response = null ;
		try{
			Staff staff = StaffUtil.getStaff(request);
			articleDetail.setCreateStaffId(staff.getId());
			articleDetail.setCreateStaffName(staff.getName());
			articleDetail.setStatus(NewsArticleEnum.NEWS_STATUS_INIT.getValue());
			newsArticleMainServiceImpl.addNewsArticle(plateIds, articleDetail);
			response = new Response(LTResponseCode.SUCCESS, "新增成功");
		}catch(Exception e){
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 批量删除文章
	 * @return
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午12:01:03
	 */
	@RequestMapping("/deleteNewsArticle")
	@ResponseBody
	public String deleteNewsArticle(String newsIds){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.deleteNewsArticle(newsIds);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 置顶文章
	 * @param newsIds 文章id（多个）
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:22:51
	 */
	@RequestMapping("/topNewsArticle")
	@ResponseBody
	public String topNewsArticle(String newsIds,Integer status){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.topNewsArticle(newsIds,status);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 复核文章（批量）
	 * @param newsIds
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:39:22
	 */
	@RequestMapping("/checkedArticle")
	@ResponseBody
	public String checkedArticle(String newsIds,Integer status){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.checkedNewsArticle(newsIds, status.toString());
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 修改文章
	 * @param plateIds
	 * @param articleDetail
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午1:47:51
	 */
	@RequestMapping("/updateNewsArticle")
	@ResponseBody
	public String updateNewsArticle(HttpServletRequest request,String plateIds,NewsArticleDetail articleDetail){
		Response response = null ;
		try{
			Staff staff = StaffUtil.getStaff(request);
			articleDetail.setModifyStaffId(staff.getId());
			articleDetail.setModifyStaffName(staff.getName());
			newsArticleMainServiceImpl.updateNewsArticle(plateIds, articleDetail);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询评论功能中的所有文章
	 * @param newsArticle
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午3:33:55
	 */
	@RequestMapping("/qryNewsArticleCmtRly")
	@ResponseBody
	public String qryNewsArticleCmtRly(NewsArticleVo newsArticle){
		Response response = null ;
		try{
			Page<NewsArticleCmtReplyVo>  page = newsArticleMainServiceImpl.qryNewsInfoCmtRlyPage(newsArticle);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 统计所有的评论，点赞和阅读数量
	 * @param type week ， year， month
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午3:34:16
	 */
	@RequestMapping("/qryRlyCmtLikeCount")
	@ResponseBody
	public String qryRlyCmtLikeCount(String type){
		Response response = null ;
		try{
			Date date = new Date();
			if(type.equals("month")){
				date = DateTools.addMonth(date, -1);
			}else if(type.equals("year")){
				date = DateTools.addYear(date, -1);
			}else{
				date = DateTools.addDay(date, -7);
			}
			List<NewsArticleCmtLikeRlyCount>  page = newsArticleMainServiceImpl.qryCmtLikeRlyCountByDate(date);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", page);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 查询评论内容
	 * @param newsId
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午7:14:25
	 */
	@RequestMapping("/qryNewsComment")
	@ResponseBody
	public String qryNewsComment(String newsId,String status) {
		Response response = null ;
		try{
			Integer statu = null;
			if(status != null ){
				statu = Integer.parseInt(status);
			}
			List<NewsUserComment>  page = newsArticleMainServiceImpl.qryNewsComment(newsId,statu);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", page);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 根据评论id查询回复id
	 * @param commentId
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月13日 下午7:47:05
	 */
	@RequestMapping("/qryNewsCmtReply")
	@ResponseBody
	public String qryNewsCmtReply(String commentId,String status){
		Response response = null ;
		try{
			Integer statu = null;
			if(status != null ){
				statu = Integer.parseInt(status);
			}
			List<NewsUserCommentReply>  page = newsArticleMainServiceImpl.qryNewsCommentReply(commentId,statu);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", page);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/addCommentSingle")
	@ResponseBody
	public String addCommentSingle(NewsComment comment){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.addNewsComment(comment);
			response = new Response(LTResponseCode.SUCCESS, "新增成功");
		}catch(Exception e){
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/addReplySingle")
	@ResponseBody
	public String addReplySingle(NewsCmtReply newsCmtReply){
		Response response = null ;
		try{
			newsCmtReply.setStatus(NewsArticleEnum.NEWS_STATUS_INIT.getValue());
			newsArticleMainServiceImpl.addNewsReply(newsCmtReply);
			response = new Response(LTResponseCode.SUCCESS, "新增成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/checkCommentSingle")
	@ResponseBody
	public String checkCommentSingle(String commmentId,String status){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.checkNewsComment(commmentId,status);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/checkReplySingle")
	@ResponseBody
	public String checkReplySingle(String replyId,String status){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.checkNewsReply(replyId,status);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/updateReplySingle")
	@ResponseBody
	public String updateReplySingle(NewsCmtReply newsCmtReply){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.updateNewsReply(newsCmtReply);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/updateCommentSingle")
	@ResponseBody
	public String updateCommentSingle(NewsComment newsComment){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.updateNewsComment(newsComment);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	@RequestMapping("/checkCommentReply")
	@ResponseBody
	public String checkCommentReply(String commentIds,String replyIds,String status){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.chkCommentReply(commentIds, replyIds, status);;
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	@RequestMapping("/init")
	@ResponseBody
	@LimitLess
	public String init(HttpServletRequest request){
		Response response = null ;
		try{
			newsArticleMainServiceImpl.init();
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
