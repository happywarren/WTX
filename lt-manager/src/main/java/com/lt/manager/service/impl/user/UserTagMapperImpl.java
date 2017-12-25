package com.lt.manager.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lt.manager.bean.user.UserTag;
import com.lt.manager.bean.user.UserTagMapper;
import com.lt.manager.dao.user.UserTagMapperDao;
import com.lt.manager.dao.user.UserTagServiceDao;
import com.lt.manager.service.user.IUserTagMapper;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class UserTagMapperImpl implements IUserTagMapper {

	@Autowired
	private UserTagMapperDao userTagMapperDao;
	@Autowired
	private UserTagServiceDao userTagServiceDao;

	@Override
	public List<UserTagMapper> findUserTagById(String userId) {
		return userTagMapperDao.selectUserTagMapperByUserId(userId);
	}
	
	@Override
	public List<UserTagMapper> findUserTag() {
		return userTagMapperDao.selectUserTagMapper();
	}
	
	@Override
	public void addUserTagMapper(UserTagMapper param) {
		 userTagMapperDao.insertUserTagMapper(param);
	}
	
	@Override
	public void removeUserTagMapper(UserTagMapper param) {
		 userTagMapperDao.deleteUserTagMapper(param);
	}

	@Override
	@Transactional
	public void removeUserTag(Integer id) {
		userTagServiceDao.deleteUserTagService(id);
		userTagMapperDao.deleteUserTagMapperByTagId(id);
	}

	@Override
	public void editUserTag(UserTag param) {
		userTagServiceDao.updateTagInfo(param);
	}

}
