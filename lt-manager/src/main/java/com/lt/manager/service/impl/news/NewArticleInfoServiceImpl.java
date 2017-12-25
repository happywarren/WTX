package com.lt.manager.service.impl.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.manager.bean.user.UserBase;
import com.lt.manager.dao.news.NewArticleInfoDao;
import com.lt.manager.dao.product.ExchangeHolidayManageDao;
import com.lt.manager.service.news.NewArticleInfoService;
import com.lt.model.news.NewsPlate;
import com.lt.model.news.NewsPlateRelation;
import com.lt.model.news.NewsSection;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

/**   
* 项目名称：lt-manager   
* 类名称：NewArticleServiceImpl   
* 类描述：   栏目标签信息处理类
* 创建人：yuanxin   
* 创建时间：2017年2月6日 下午3:56:04      
*/
@Service
public class NewArticleInfoServiceImpl implements NewArticleInfoService {

	@Autowired
	private NewArticleInfoDao newArticleDao;
	
	@Override
	public boolean ifExistSection(String name) throws LTException{
		NewsSection Section = newArticleDao.getNewsSectionByName(name.trim());
		return Section == null ?false:true;
	}

	@Override
	public void addNewsSection(String name) throws LTException {
		newArticleDao.addNewsSection(name);
	}

	@Override
	public List<NewsSection> qryAllSection() throws LTException {
		return newArticleDao.qryAllSection();
	}

	@Override
	public void deleteNewSection(Integer name) throws LTException {
		newArticleDao.deleteSectionById(name);
	}

	@Override
	public boolean ifExistTag(String tag) throws LTException {
		NewsPlate Section = newArticleDao.getNewstagByName(tag.trim());
		return Section == null ?false:true;
	}

	@Override
	public void addNewsTag(String tag) throws LTException {
		newArticleDao.addNewsTag(tag);
	}

	@Override
	public List<NewsPlate> qryAllTag() throws LTException {
		return newArticleDao.qryAllTag();
	}

	@Override
	public void deleteNewTag(Integer name) throws LTException {
		newArticleDao.deleteTagByName(name);
	}

	@Override
	public void insertPlateRelation(List<NewsPlateRelation> list) throws LTException {
		newArticleDao.insetPlateRalation(list);
	}

	@Override
	public List<UserBase> qryReadUserInfo(String newsId) throws LTException {
		return newArticleDao.qryAllReadUser(newsId);
	}

	
	@Override
	public void updateNewsSection(String name, Integer id) throws LTException {
		NewsSection newsSection = new NewsSection();
		newsSection.setId(id);
		newsSection.setName(name);
		newArticleDao.uptNewsSection(newsSection);
	}
	
	@Override
	public void updateNewsTag(String name, Integer id) throws LTException {
		NewsPlate newsSection = new NewsPlate();
		newsSection.setId(id);
		newsSection.setName(name);
		newArticleDao.uptNewsTag(newsSection);
	}

}
