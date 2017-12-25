package com.lt.business.core.dao.news;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;
import com.lt.model.news.NewsActInfoLog;
import com.lt.model.news.NewsArticle;
import com.lt.model.news.NewsCmtReply;
import com.lt.model.news.NewsComment;
import com.lt.model.news.NewsJin10;
import com.lt.vo.news.NewsArticleVo;

public interface NewsArticleDao {

	public void insertNewsCmt(NewsComment newsComment);

	public void insertNewsCmtReply(NewsCmtReply newsCmtReply);

	public void newsInfoPlusIncrement(@Param("newsId") Integer newsId, @Param("columnName") String columnName);

	public void insertNewsActInfoLog(NewsActInfoLog infoLog);

	public NewsArticle selectNewsArticleById(@Param("id") Integer id);

	/**
	 * 
	 * TODO 描述:获取评论列表，用户ID存在时，展示当前用户的评论
	 * @author XieZhibing
	 * @date 2017年2月3日 下午3:37:50
	 * @param userId
	 * @param newsId
	 * @param rb
	 * @return
	 */
	public Page<NewsComment> selectNewsArticleCmtByUserId(Map<String,Object> paraMap);
	
	public Integer selectNewsArticleCmtCountByUserId(@Param("userId") String userId,
			@Param("newsId") Integer newsId, @Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

	public List<NewsCmtReply> selectNewsCmtReplyByIdList(@Param("idList") List<Integer> idList);
	/**
	 * 根据新闻ID查询新闻评论
	 * 
	 * @param newsIdList
	 * @return
	 */
	public List<NewsComment> selectNewsArticleCmtByNewsIdList(List<Integer> newsIdList);

	/**
	 * 根据主评论ID查询该评论下的所有回复
	 * 
	 * @param cmtId
	 * @return
	 */
	public List<NewsCmtReply> selectNewsArticleCmtReplyByCmtId(@Param("userId") String userId, @Param("cmtId") Integer cmtId);

	public List<NewsComment> selectNewsArticleCmtByIdList(List<Integer> idList);

	public NewsComment selectLastCmtByUserId(@Param("userId") String userId);

	public NewsCmtReply selectLastReplyByUserId(@Param("userId") String userId);

	public NewsArticleVo getLastTopNews();

	public List<NewsArticleVo> getNewsBySection(@Param("section") Integer section, @Param("index") Integer index,
			@Param("size") Integer size, @Param("topId") Integer topId);

	public List<NewsArticleVo> getNewsByBrandIdSection(@Param("brandId") String brandId,@Param("section") Integer section, @Param("index") Integer index,
												@Param("size") Integer size, @Param("topId") Integer topId);


	public NewsArticleVo getNewsArticleVoById(@Param("newsId") Integer newsId);

	public Page<NewsJin10> getNewsJin10Page(RowBounds rb);

}
