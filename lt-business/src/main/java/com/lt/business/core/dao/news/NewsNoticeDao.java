package com.lt.business.core.dao.news;

import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;
import com.lt.model.news.NewsNotice;

/**
 * 新闻公告Dao
 * @author 郭达望
 * @created 2015年5月13日
 */
public interface NewsNoticeDao {
	
	
	/**
	 * 根据id查询新闻公告
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param id
	 * @return
	 */
	public NewsNotice queryNewsNoticeById(Integer id);
	
	/**
	 * 多条件查询新闻公告列表
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param type
	 * @param rb
	 * @return
	 */
	public Page<NewsNotice> queryNewsNoticeList(Integer type, RowBounds rb);
	
	/**
	 * 多条件查询新闻公告图片列表
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param type
	 * @param rb
	 * @return
	 */
	public Page<NewsNotice> queryNewsNoticeVoList(Integer type, RowBounds rb);

}
