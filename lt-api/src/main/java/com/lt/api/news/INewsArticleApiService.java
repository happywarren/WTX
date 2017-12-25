package com.lt.api.news;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;
import com.lt.model.news.NewsCmtReplyParam;
import com.lt.model.news.NewsNotice;
import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.news.NewsArticleVo;

/**
 * 
 * TODO 新闻、策略资讯远程接口
 * @author XieZhibing
 * @date 2017年2月4日 下午4:27:30
 * @version <b>1.0.0</b>
 */
public interface INewsArticleApiService {

	/**
	 * 
	 * TODO 描述:策略列表
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:49:00
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<NewsArticleVo> queryStrategyList( Integer pageNo, Integer pageSize);

	/**
	 * 策略列表 根据品牌查询 默认为000001
	 * @param brandId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<NewsArticleVo> queryStrategyList(String brandId, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * TODO 描述:新闻列表
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:48:48
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<NewsArticleVo> queryNewsList( Integer pageNo, Integer pageSize);

	/**
	 * 新闻列表
	 * @param brandCode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<NewsArticleVo> queryNewsList(String brandCode, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * TODO 资讯详情
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:55:37
	 * @param brandCode
	 * @param newsArticleId
	 * @return
	 */
	public NewsArticleVo queryNewsArticleDetail(String brandCode, Integer newsArticleId);

	/**
	 * 
	 * TODO 描述:资讯评论列表
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:49:09
	 * @param userId
	 * @param newsArticleId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public String queryNewsArticleCmt(String userId, Integer newsArticleId, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * TODO 描述:添加评论回复
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:49:43
	 * @param newsCmtReplyParam
	 * @return
	 */
	Response addNewsCmtReply(NewsCmtReplyParam newsCmtReplyParam);
	
	/**
	 * 按type查询新闻公告列表
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param type
	 * @param rb
	 * @return
	 * @throws LTException
	 */
	public List<NewsNotice> findNewsNoticeList(int pageNoStr,int pageSizeStr,int type) throws LTException;
}
