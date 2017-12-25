package com.lt.business.core.service.news.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.business.core.dao.news.NewsNoticeDao;
import com.lt.business.core.service.news.INewsNoticeService;
import com.lt.model.news.NewsNotice;
import com.lt.util.error.LTException;
import com.lt.util.utils.CalendarTools;
import com.lt.vo.news.NewsNoticeVo;

/**
 * 栏目数据接口实现
 * @author 郭达望
 * @created 2015年5月13日
 */
@Service
public class NewsNoticeServiceImpl implements INewsNoticeService {
	
	@Autowired
	private NewsNoticeDao newsNoticeDao;

	@Override
	public NewsNotice findNewsNoticeById(Integer id) {
		return newsNoticeDao.queryNewsNoticeById(id);
	}
	
	@Override
	public List<NewsNoticeVo> initNewsNoticeList(Integer type, RowBounds rb) throws LTException {
		List<NewsNotice> list = newsNoticeDao.queryNewsNoticeList(Integer.valueOf(type), rb).getResult();
		List<NewsNoticeVo> voList = new ArrayList<NewsNoticeVo>();
		for (int i = 0; i < list.size(); i++) {
			NewsNotice newsNotice = list.get(i);
			if(null != newsNotice && newsNotice.getMiddleBanner()!=null){
				NewsNoticeVo vo =new NewsNoticeVo();
				vo.setMiddleBanner(newsNotice.getMiddleBanner());
				vo.setId(newsNotice.getId());
				vo.setUrl(newsNotice.getUrl());
				vo.setCreateDate(CalendarTools.formatDateTime(newsNotice.getCreateDate(), CalendarTools.DATETIME));
				vo.setTitle(newsNotice.getTitle());
				voList.add(vo);
			}
		}
		return voList;
	}

	@Override
	public List<NewsNotice> findNewsNoticeList(Integer type, RowBounds rb) throws LTException {
		List<NewsNotice> list = newsNoticeDao.queryNewsNoticeList(Integer.valueOf(type), rb).getResult();
		return list;
	}
	
	@Override
	public List<NewsNotice> findNewsNoticeVoList(Integer type, RowBounds rb) throws LTException {
		List<NewsNotice> list = newsNoticeDao.queryNewsNoticeVoList(Integer.valueOf(type), rb).getResult();
		return list;
	}
}
