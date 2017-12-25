package com.lt.business.core.service.news;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.lt.model.news.NewsNotice;
import com.lt.util.error.LTException;
import com.lt.vo.news.NewsNoticeVo;

/**
 * 新闻公告接口
 * @author 郭达望
 * @created 2015年5月13日
 */
public interface INewsNoticeService {
	
	/**
	 * 根据id查询新闻公告
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param id
	 * @return
	 */
	public NewsNotice findNewsNoticeById(Integer id)throws LTException;

	/**
	 * 组装返回对象内容（公告列表 ：id 中图 title createDate）
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param valueOf
	 * @param rb
	 * @return
	 * @throws LTException 
	 */
	public List<NewsNoticeVo> initNewsNoticeList(Integer type, RowBounds rb) throws LTException;

	/**
	 * 按type查询新闻公告图片列表
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param type
	 * @param rb
	 * @return
	 * @throws LTException
	 */
	public List<NewsNotice> findNewsNoticeVoList(Integer type, RowBounds rb) throws LTException;

	/**
	 * 按type查询新闻公告列表
	 * @author 郭达望
	 * @created 2015年5月13日
	 * @param type
	 * @param rb
	 * @return
	 * @throws LTException
	 */
	public List<NewsNotice> findNewsNoticeList(Integer type, RowBounds rb) throws LTException;

}
