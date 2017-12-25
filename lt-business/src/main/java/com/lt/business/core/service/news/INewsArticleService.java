package com.lt.business.core.service.news;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.model.news.NewsCmtReplyParam;
import com.lt.model.news.NewsJin10;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.news.NewsArticleVo;

/**
 * 
 * TODO 新闻、策略资讯业务接口
 * @author XieZhibing
 * @date 2017年2月4日 下午4:27:30
 * @version <b>1.0.0</b>
 */
public interface INewsArticleService {

	/**
	 * 
	 * TODO 描述:金10数据列表
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:48:17
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<NewsJin10> pageNewsJin10(Integer page, Integer rows);

	public NewsArticleVo newsArticleDetail(String userId, Integer newsArticleId);

	public boolean newsArticleShare(String userId, Integer newsArticleId, String ip);

	public boolean newsArticleRead(String userId, Integer newsArticleId);

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
	List<NewsArticleVo> selectNewsList(Integer pageNo, Integer pageSize);

	/**
	 * 新闻列表
	 * @param brandCode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<NewsArticleVo> selectNewsList(String brandCode,Integer pageNo, Integer pageSize);

	/**
	 * 
	 * TODO 描述:策略列表
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:49:00
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<NewsArticleVo> selectStrategyList( Integer pageNo, Integer pageSize);

	/**
	 * 根据品牌查询策略列表
	 * @param brandId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<NewsArticleVo> selectStrategyList(String brandId, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * TODO 描述:回复列表
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:49:09
	 * @param userId
	 * @param newsArticleId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Map<String, Object>> selectNewsArticleCmt(String userId, Integer newsArticleId, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * TODO 描述:添加评论回复
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:49:43
	 * @param newsCmtReplyParam
	 * @return
	 */
	Response addNewsCmtReply(NewsCmtReplyParam newsCmtReplyParam);

}
