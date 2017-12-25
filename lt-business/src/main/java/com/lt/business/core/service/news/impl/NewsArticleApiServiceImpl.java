/*
 * PROJECT NAME: lt-business
 * PACKAGE NAME: com.lt.business.core.service.news.impl
 * FILE    NAME: NewsArticleApiServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.business.core.service.news.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.constant.redis.RedisUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.news.INewsArticleApiService;
import com.lt.business.core.service.news.INewsArticleService;
import com.lt.business.core.service.news.INewsNoticeService;
import com.lt.model.news.NewsCmtReplyParam;
import com.lt.model.news.NewsNotice;
import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.news.NewsArticleVo;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * TODO 新闻、策略资讯远程接口
 * @author XieZhibing
 * @date 2017年2月4日 下午5:49:50
 * @version <b>1.0.0</b>
 */
@Service
public class NewsArticleApiServiceImpl implements INewsArticleApiService {

	@Autowired
	private INewsArticleService newsArticleServiceImpl;
	@Autowired
	private INewsNoticeService newsNoticeService;
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	/**
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:58:30
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<NewsArticleVo> queryStrategyList( Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return newsArticleServiceImpl.selectStrategyList(pageNo, pageSize);
	}

	@Override
	public List<NewsArticleVo> queryStrategyList(String brandId, Integer pageNo, Integer pageSize) {
		return newsArticleServiceImpl.selectStrategyList(brandId,pageNo, pageSize);
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:58:30
	 * @see com.lt.api.news.INewsArticleApiService#queryNewsList(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<NewsArticleVo> queryNewsList(Integer pageNo, Integer pageSize) {
		return newsArticleServiceImpl.selectNewsList(pageNo, pageSize);
	}

	@Override
	public List<NewsArticleVo> queryNewsList(String brandCode,Integer pageNo, Integer pageSize) {
		return newsArticleServiceImpl.selectNewsList(brandCode,pageNo, pageSize);
	}
		/**
         *
         * @author XieZhibing
         * @date 2017年2月4日 下午5:58:30
         * @param brandCode
         * @param newsArticleId
         * @return
         */
	@Override
	public NewsArticleVo queryNewsArticleDetail(String brandCode, Integer newsArticleId) {
		NewsArticleVo newsArticleDetail = newsArticleServiceImpl.newsArticleDetail(brandCode, newsArticleId);
		if(StringTools.isNotEmpty(newsArticleDetail)){
			// 获取详情页时，直接标示阅读量
			newsArticleServiceImpl.newsArticleRead(null, newsArticleId);
		}
		
		return newsArticleDetail;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:58:30
	 * @see com.lt.api.news.INewsArticleApiService#queryNewsArticleCmt(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 * @param userId
	 * @param newsArticleId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public String queryNewsArticleCmt(String userId,
			Integer newsArticleId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Page<Map<String, Object>> page = newsArticleServiceImpl.selectNewsArticleCmt(userId, newsArticleId, pageNo, pageSize);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("totalCmt", 0);
		data.put("cmtReplyList", null);
		if (null != page) {
			data.put("totalCmt", page.getTotal());
			data.put("cmtReplyList", page.getResult());
		}
		return JSONObject.toJSONString(data);
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:58:30
	 * @see com.lt.api.news.INewsArticleApiService#addNewsCmtReply(com.lt.model.news.NewsCmtReplyParam)
	 * @param newsCmtReplyParam
	 * @return
	 */
	@Override
	public Response addNewsCmtReply(NewsCmtReplyParam newsCmtReplyParam) {
		// TODO Auto-generated method stub
		return newsArticleServiceImpl.addNewsCmtReply(newsCmtReplyParam);
	}

	@Override
	public List<NewsNotice> findNewsNoticeList(int pageNoStr,int pageSizeStr,int type)
			throws LTException {
		RowBounds rb = new RowBounds(Integer.valueOf(pageNoStr), Integer.valueOf(pageSizeStr));
		return newsNoticeService.findNewsNoticeList(type, rb);
	}
	
}
