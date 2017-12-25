package com.lt.manager.service.user;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.UserBlacklist;

/**
 * 
 * <br>
 * <b>功能：</b>UserBlacklistService<br>
 */
public interface IUserBlacklistService {

/**
	 * 查询UserBlacklist--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public Page<UserBlacklist> queryUserBlacklistPage(UserBlacklist param) throws Exception;
	
	/**
	 * UserBlacklist--数量用于分页
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public Integer queryUserBlacklistCount(UserBlacklist param) throws Exception;
	
	/**UserBlacklist--用于编辑
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void editUserBlacklist(UserBlacklist param) throws Exception;
	
	/**UserBlacklist--用于添加
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void addUserBlacklist(UserBlacklist param) throws Exception;
	
	/**
	 *UserBlacklist--用于移除
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void removeUserBlacklist(String id) throws Exception;
	/**
	 *UserBlacklist--用于获得对象
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public UserBlacklist getUserBlacklist(Integer id) throws Exception;
	
	/**
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public List<UserBlacklist> findUserBlacklist(UserBlacklist param) throws Exception;

}
