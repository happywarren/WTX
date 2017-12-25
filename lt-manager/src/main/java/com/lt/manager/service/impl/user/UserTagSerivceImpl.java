package com.lt.manager.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.manager.bean.user.UserService;
import com.lt.manager.bean.user.UserTag;
import com.lt.manager.dao.user.UserTagServiceDao;
import com.lt.manager.service.user.IUserTagManageService;
import com.lt.util.utils.model.Response;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class UserTagSerivceImpl implements IUserTagManageService {

	@Autowired
	private UserTagServiceDao userTagServiceDao;
	
	@Override
	public Integer addUserTagService(UserTag param) throws Exception {
		return userTagServiceDao.insertUserTagService(param);
	}

	@Override
	public void editUserTagService(UserTag param) throws Exception {
		userTagServiceDao.updateUserTagService(param);
	}

	@Override
	public void removeUserTagService(Integer tagId) throws Exception {
		userTagServiceDao.deleteUserTagService(tagId);
	}

	@Override
	public List<UserTag> findUserTagService() {
		
		return userTagServiceDao.selectUserTagService();
	}

}
