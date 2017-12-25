package com.lt.business.core.service.news.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.business.core.dao.brand.BrandDao;
import com.lt.business.core.dao.news.NewsArticleBrandDao;
import com.lt.constant.redis.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.business.core.dao.news.NewsArticleDao;
import com.lt.business.core.service.news.INewsArticleService;
import com.lt.enums.news.NewsArticleEnum;
import com.lt.model.news.NewsActInfoLog;
import com.lt.model.news.NewsArticle;
import com.lt.model.news.NewsCmtReply;
import com.lt.model.news.NewsCmtReplyParam;
import com.lt.model.news.NewsComment;
import com.lt.model.news.NewsJin10;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.news.NewsArticleVo;
/**
 * 
 * TODO 新闻、策略资讯业务接口
 * @author XieZhibing
 * @date 2017年2月4日 下午4:28:15
 * @version <b>1.0.0</b>
 */
@Service
public class NewsArticleServiceImpl implements INewsArticleService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	final String BRANDID = "20170905020140725";

	@Autowired
	private NewsArticleDao newsArticleDao;
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	@Autowired
	private NewsArticleBrandDao newsArticleBrandDao;

	@Override
	public Page<NewsJin10> pageNewsJin10(Integer page, Integer rows) {
		RowBounds rb = new RowBounds(page, rows);
		return newsArticleDao.getNewsJin10Page(rb);
	}

	@Override
	public NewsArticleVo newsArticleDetail(String brandCode, Integer newsArticleId) {
		String brandId = brandDao.findBrandIdByBrandCode(brandCode);
		if(!StringTools.isNotEmpty(brandId)){
			brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);
			if(!StringTools.isNotEmpty(brandId)){
				brandId = BRANDID;
			}
		}
		// 查询该新闻详细
		NewsArticleVo vo = newsArticleBrandDao.getNewsArticleVoById(newsArticleId,brandId);
		return vo;
	}

	@Override
	@Transactional
	public boolean newsArticleRead(String userId, Integer newsArticleId) {
		// 新增阅读数
		updateNewsAticleInfoCount(newsArticleId, 0);
		return true;
	}

	@Override
	@Transactional
	public boolean newsArticleShare(String userId, Integer newsArticleId, String ip) {
		NewsActInfoLog newsLog = new NewsActInfoLog();
		newsLog.setLogType(2);
		newsLog.setNewsArticleId(newsArticleId);
		newsLog.setUserId(userId);
		newsLog.setIp(ip);
		// 分享数据插入数据库
		newsArticleDao.insertNewsActInfoLog(newsLog);
		// 添加分享次数
		updateNewsAticleInfoCount(newsArticleId, 3);
		return true;
	}

	@Override
	public List<NewsArticleVo> selectNewsList( Integer pageNo, Integer pageSize) {
		Integer index = (pageNo - 1) * pageSize;
		List<NewsArticleVo> result = new ArrayList<>();
		Integer topId = null;
		if (pageNo == 1) {// 第一页，去获取置顶数据
			NewsArticleVo vo = newsArticleDao.getLastTopNews();
			if (vo != null) {
				topId = vo.getId();
				result.add(vo);
			}
		}
		List<NewsArticleVo> res = newsArticleDao.getNewsBySection(NewsArticleEnum.NEWS_SECTION_NEWS.getValue(), index, pageSize, topId);
		result.addAll(res);
		return result;
	}
	@Override
	public List<NewsArticleVo> selectNewsList(String brandCode, Integer pageNo, Integer pageSize) {
		String brandId = brandDao.findBrandIdByBrandCode(brandCode);
		if(!StringTools.isNotEmpty(brandId)){
			brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);
			if(!StringTools.isNotEmpty(brandId)){
				brandId = BRANDID;
			}
		}
		Integer index = (pageNo - 1) * pageSize;
		List<NewsArticleVo> result = new ArrayList<>();
		Integer topId = null;
		if (pageNo == 1) {// 第一页，去获取置顶数据
			NewsArticleVo vo = newsArticleBrandDao.getLastTopNews(brandId);
			if (vo != null) {
				topId = vo.getId();
				result.add(vo);
			}
		}
		List<NewsArticleVo> res = newsArticleBrandDao.getNewsBySection(brandId,NewsArticleEnum.NEWS_SECTION_NEWS.getValue(), index, pageSize, topId);
		result.addAll(res);
		return result;
	}

	@Override
	public List<NewsArticleVo> selectStrategyList( Integer pageNo, Integer pageSize) {
		Integer index = (pageNo - 1) * pageSize;
		List<NewsArticleVo> result = newsArticleDao.getNewsBySection(NewsArticleEnum.NEWS_SECTION_STAGY.getValue(), index, pageSize, null);
		return result;
	}

	@Override
	public List<NewsArticleVo> selectStrategyList(String brandCode, Integer pageNo, Integer pageSize) {
		String brandId = brandDao.findBrandIdByBrandCode(brandCode);
		if(!StringTools.isNotEmpty(brandId)){
			brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);
			if(!StringTools.isNotEmpty(brandId)){
				brandId = BRANDID;
			}
		}
		logger.info(" brandId = {},brandCode = {}",brandId,brandCode);
		Integer index = (pageNo - 1) * pageSize;
		List<NewsArticleVo> result = newsArticleBrandDao.getNewsByBrandIdSection(brandId,NewsArticleEnum.NEWS_SECTION_STAGY.getValue(), index, pageSize, null);
		return result;
	}

	/**
	 * 新闻评论列表
	 */
	@Override
	public Page<Map<String, Object>> selectNewsArticleCmt(String userId, Integer newsArticleId, Integer pageNo, Integer pageSize) {
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		Integer pageStart = (pageNo - 1) * pageSize;
		Integer pageEnd = pageSize;
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("pageNo", pageStart);
		paraMap.put("pageSize", pageEnd);
		paraMap.put("userId", userId);
		paraMap.put("newsId", newsArticleId);
		Page<NewsComment> newsCmtList = newsArticleDao.selectNewsArticleCmtByUserId(paraMap);
		Integer total = newsArticleDao.selectNewsArticleCmtCountByUserId(userId, newsArticleId, pageNo,pageSize);
		logger.info("total = {}",total);
		if (CollectionUtils.isEmpty(newsCmtList)) {
			return null;
		}
		// 当前评论下的所有回复
		for (NewsComment cmt : newsCmtList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cmt", cmt);
			List<NewsCmtReply> replyList = newsArticleDao.selectNewsArticleCmtReplyByCmtId(userId, cmt.getId());
			if (CollectionUtils.isNotEmpty(replyList)) {
				for (NewsCmtReply newsCmtReply : replyList) {
					//添加被回复人昵称
					if(StringTools.isBlank(newsCmtReply.getUpUserNick())){
						//被回复人为空时，设置被回复人为评论人
						newsCmtReply.setUpUserNick(cmt.getUserNick());	
					}
				}
				map.put("reply", replyList);
			} else {
				map.put("reply", null);
			}
			page.add(map);
		}
		page.setTotal(total);
		logger.info("page = {} ",JSONObject.toJSONString(page));
		return page;
	}

	@Override
	@Transactional
	public Response addNewsCmtReply(NewsCmtReplyParam newsCmtReplyParam) {
		try {
			logger.info("IOS评论回复数据========" + JSON.toJSONString(newsCmtReplyParam));
			if (newsCmtReplyParam==null || newsCmtReplyParam.getNewsId() == null ) {
				//新闻标识错误
				return LTResponseCode.getCode(LTResponseCode.NA00001);
			}
			NewsArticle article = newsArticleDao.selectNewsArticleById(newsCmtReplyParam.getNewsId());
			if (!StringTools.isNotEmpty(article)) {
				//新闻标识错误
				return LTResponseCode.getCode(LTResponseCode.NA00001);
			}
			if (1 != article.getPermitComment()) {
				//评论失败，请稍后再试
				return LTResponseCode.getCode(LTResponseCode.NA00002);
			}
			
			if (!StringTools.isNotEmpty(newsCmtReplyParam.getUserId())) {
				//评论失败，用户标识丢失
				return LTResponseCode.getCode(LTResponseCode.NA00003);
			}
			// 如果评论内容不为空，则当做评论处理
			if (StringTools.isNotBlank(newsCmtReplyParam.getCmtContent())) {
				// 首先检测该用户先后两次评论的时间是否间隔超过5秒
				// 如果不超过5秒，则本次请求被过滤掉
				NewsComment newsComment = newsArticleDao.selectLastCmtByUserId(newsCmtReplyParam.getUserId());
				if (StringTools.isNotEmpty(newsComment)) {
					long len = (new Date().getTime() - DateTools.parseDate(newsComment.getCreateDate(), DateTools.FORMAT_FULL).getTime());
					if ( len <= 3000) {
						//评论过于频繁，请稍后再试
						return LTResponseCode.getCode(LTResponseCode.NA00004);
					}
				}
				
				if (500 < newsCmtReplyParam.getCmtContent().length()) {
					//评论内容不可超过500字
					return LTResponseCode.getCode(LTResponseCode.NA00005);
				}
				newsComment = new NewsComment();
				newsComment.setContent(newsCmtReplyParam.getCmtContent());
				newsComment.setNewsId(newsCmtReplyParam.getNewsId());
				if (!StringTools.isNotEmpty(article) || StringTools.isBlank(article.getTitle())) {
					newsComment.setNewsName("无标题");
				} else {
					newsComment.setNewsName(article.getTitle());
				}
				// 默认评论带待审核通过
				newsComment.setStatus(0);
				newsComment.setUserHead("");
				newsComment.setUserId(newsCmtReplyParam.getUserId());
				newsComment.setUserNick(newsCmtReplyParam.getUserNick());
				newsArticleDao.insertNewsCmt(newsComment);
				// 新增评论数
				updateNewsAticleInfoCount(newsCmtReplyParam.getNewsId(), 1);
				//评论成功
				return LTResponseCode.getCode(LTResponseCode.SUCCESS);
			} else { // 否则当做回复处理
				NewsCmtReply newsCmtReply = newsArticleDao.selectLastReplyByUserId(newsCmtReplyParam.getUserId());
				if (StringTools.isNotEmpty(newsCmtReply)) {
					long len = (new Date().getTime() -  DateTools.parseDate(newsCmtReply.getCreateDate(), DateTools.FORMAT_FULL).getTime());
					if ( len <= 3000) {
						//回复过于频繁，请稍后再试
						return LTResponseCode.getCode(LTResponseCode.NA00007);
					}
				}
				newsCmtReply = new NewsCmtReply();
				if (StringTools.isBlank(newsCmtReplyParam.getReplyContent())) {
					//评论或回复内容不可为空
					return LTResponseCode.getCode(LTResponseCode.NA00008);
				}
				if (500 < newsCmtReplyParam.getReplyContent().length()) {
					//回复内容不可超过500字
					return LTResponseCode.getCode(LTResponseCode.NA00009);
				}
				if (!StringTools.isNotEmpty(newsCmtReplyParam.getCmtId())) {
					//评论标识错误
					return LTResponseCode.getCode(LTResponseCode.NA00010);
				}
				newsCmtReply.setReplyContent(newsCmtReplyParam.getReplyContent());
				newsCmtReply.setCmtId(newsCmtReplyParam.getCmtId());
				newsCmtReply.setNewsId(newsCmtReplyParam.getNewsId());
				if (!StringTools.isNotEmpty(article) || StringTools.isBlank(article.getTitle())) {
					newsCmtReply.setNewsName("无标题");
				} else {
					newsCmtReply.setNewsName(article.getTitle());
				}
				newsCmtReply.setReplyContent(newsCmtReplyParam.getReplyContent());
				newsCmtReply.setReplyId(newsCmtReplyParam.getReplyId());
				newsCmtReply.setReplyUserId(newsCmtReplyParam.getUserId());
				newsCmtReply.setReplyUserNick(newsCmtReplyParam.getUserNick());
				// 默认回复待审核
				newsCmtReply.setStatus(0);
				newsCmtReply.setReplyUserHead("");

				// 如果是自己回复自己，则阻止回复
				if (!StringTools.isNotEmpty(newsCmtReplyParam.getReplyId())) {
					List<Integer> cmtIdList = new ArrayList<Integer>();
					cmtIdList.add(newsCmtReplyParam.getCmtId());
					List<NewsComment> cmtList = newsArticleDao.selectNewsArticleCmtByIdList(cmtIdList);
					if (CollectionUtils.isNotEmpty(cmtList)) {
						if (cmtList.get(0).getUserId().equals(newsCmtReplyParam.getUserId())) {
							//不可回复自己的评论
							return LTResponseCode.getCode(LTResponseCode.NA00011);
						}
					}
				} else {
					List<Integer> idList = new ArrayList<Integer>();
					idList.add(newsCmtReplyParam.getReplyId());
					List<NewsCmtReply> replyList = newsArticleDao.selectNewsCmtReplyByIdList(idList);
					if (CollectionUtils.isNotEmpty(replyList)) {
						if (replyList.get(0).getReplyUserId().equals(newsCmtReplyParam.getUserId())) {
							//不可对自己回复
							return LTResponseCode.getCode(LTResponseCode.NA00012);
						}
					}
				}
				newsArticleDao.insertNewsCmtReply(newsCmtReply);
				// 添加回复数
				updateNewsAticleInfoCount(newsCmtReplyParam.getNewsId(), 2);
				//回复成功
				return LTResponseCode.getCode(LTResponseCode.SUCCESS);
			}
		} catch (Exception e) {
			logger.error("客户端新增评论或回复出错", e);
			//服务异常，评论失败
			return LTResponseCode.getCode(LTResponseCode.NA00014);
		}
	}

	/**
	 * 
	 * TODO 描述:更新新闻阅读评论回复数量信息
	 * @author XieZhibing
	 * @date 2017年2月3日 下午4:53:03
	 * @param newsId
	 * @param type 0阅读 1评论 2回复 3分享
	 */
	private void updateNewsAticleInfoCount(Integer newsId, Integer type) {
		String column = "";

		switch (type) {
			default :// 默认为0
			case 0 :// 阅读
				column = "real_read_count";
				break;
			case 1 :// 评论
				column = "real_comment_count";
				break;
			case 2 :// 回复
				column = "real_reply_count";
				break;
			case 3 :// 分享
				column = "real_share_count";
				break;
		}
		// 指定字段+1
		newsArticleDao.newsInfoPlusIncrement(newsId, column);
	}

}
