/*
 * PROJECT NAME: lt-business
 * PACKAGE NAME: com.lt.business.core.service.news.impl
 * FILE    NAME: NewsNoticeApiServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.business.core.service.news.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.alibaba.dubbo.config.annotation.Service;
import com.lt.api.news.INewsNoticeApiService;
import com.lt.model.news.NewsNotice;
import com.lt.util.error.LTException;
import com.lt.vo.news.NewsNoticeVo;

/**
 * TODO 新闻公告远程接口
 * @author XieZhibing
 * @date 2017年2月4日 下午5:51:15
 * @version <b>1.0.0</b>
 */
@Service
public class NewsNoticeApiServiceImpl implements INewsNoticeApiService {

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:51:15
	 * @see com.lt.api.news.INewsNoticeApiService#findNewsNoticeById(java.lang.Integer)
	 * @param id
	 * @return
	 * @throws LTException
	 */
	@Override
	public NewsNotice findNewsNoticeById(Integer id) throws LTException {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:51:15
	 * @see com.lt.api.news.INewsNoticeApiService#initNewsNoticeList(java.lang.Integer, org.apache.ibatis.session.RowBounds)
	 * @param type
	 * @param rb
	 * @return
	 * @throws LTException
	 */
	@Override
	public List<NewsNoticeVo> initNewsNoticeList(Integer type, RowBounds rb)
			throws LTException {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:51:15
	 * @see com.lt.api.news.INewsNoticeApiService#findNewsNoticeVoList(java.lang.Integer, org.apache.ibatis.session.RowBounds)
	 * @param type
	 * @param rb
	 * @return
	 * @throws LTException
	 */
	@Override
	public List<NewsNotice> findNewsNoticeVoList(Integer type, RowBounds rb)
			throws LTException {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月4日 下午5:51:15
	 * @see com.lt.api.news.INewsNoticeApiService#findNewsNoticeList(java.lang.Integer, org.apache.ibatis.session.RowBounds)
	 * @param type
	 * @param rb
	 * @return
	 * @throws LTException
	 */
	@Override
	public List<NewsNotice> findNewsNoticeList(Integer type, RowBounds rb)
			throws LTException {
		// TODO Auto-generated method stub
		return null;
	}

}
