package com.lt.manager.dao.user;


import com.lt.manager.bean.user.UserBlacklist;
import java.util.List;
/**
 * 
 * <br>
 * <b>功能：</b>UserBlacklistDao<br>
 */
public interface UserBlacklistDao {

	/**
	 * UserBlacklist查询接口
	 * @param param
	 * @return
	 */
	public List<UserBlacklist> selectUserBlacklistPage(UserBlacklist param);
	
	/**
	 * UserBlacklist查询根据数量分页
	 * @param param
	 * @return
	 */
	public Integer selectUserBlacklistCount(UserBlacklist param);
	
	
	/**
	 * UserBlacklist查询根据数量分页
	 * @param param
	 * @return
	 */
	public void insertUserBlacklist(UserBlacklist param);
	
	/**
	 *UserBlacklist信息更改
	 * @param param
	 * @return
	 */
	public void updateUserBlacklist(UserBlacklist param);
	
	/**
	 * UserBlacklist信息删除
	 * @param param
	 * @return
	 */
	public void deleteUserBlacklist(String id);
	
	/**
	 * 根据ID获得UserBlacklist对象
	 */
	public UserBlacklist selectUserBlacklistById(Integer id);
	
	/**
	 * 根据对象获得UserBlacklist满足条件列表
	 */
	public List<UserBlacklist> selectUserBlacklist(UserBlacklist param);
	
}
