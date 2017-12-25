package com.lt.manager.service.impl.user;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.UserBlacklist;
import com.lt.manager.dao.user.UserBlacklistDao;
import com.lt.manager.service.user.IUserBlacklistService;

/**
 * 
 * <br>
 * <b>功能：</b>UserBlacklistService<br>
 */
@Service("userBlacklistService")
public class  UserBlacklistServiceImpl  implements IUserBlacklistService {
  private final static Logger log= Logger.getLogger(UserBlacklistServiceImpl.class);
	
		
	@Autowired
	private UserBlacklistDao userBlacklistDao;
	
	
	@Override
	public Page<UserBlacklist> queryUserBlacklistPage(UserBlacklist param)
			throws Exception {
		
		Page<UserBlacklist> page = new Page<UserBlacklist>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.addAll(userBlacklistDao.selectUserBlacklistPage(param));
		page.setTotal(userBlacklistDao.selectUserBlacklistCount(param));
		
		return page;
	}

	@Override
	public Integer queryUserBlacklistCount(UserBlacklist param) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addUserBlacklist(UserBlacklist param) throws Exception {
		userBlacklistDao.insertUserBlacklist(param);
	}

	@Override
	public void editUserBlacklist(UserBlacklist param) throws Exception {
		userBlacklistDao.updateUserBlacklist(param);
	}

	@Override
	public void removeUserBlacklist(String id) throws Exception {
		userBlacklistDao.deleteUserBlacklist(id);
	}

	@Override
	public List<UserBlacklist> findUserBlacklist(UserBlacklist param) {
		return userBlacklistDao.selectUserBlacklist(param);
	}
	
	@Override
	public UserBlacklist getUserBlacklist(Integer id) {
		return userBlacklistDao.selectUserBlacklistById(id);
	}

}
