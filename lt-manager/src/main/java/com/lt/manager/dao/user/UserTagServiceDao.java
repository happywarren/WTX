package com.lt.manager.dao.user;

import java.util.List;

import com.lt.manager.bean.user.UserService;
import com.lt.manager.bean.user.UserTag;

/**
 * 商品类型管理dao
 * @author licy
 *
 */
public interface UserTagServiceDao {
	
	/**
	 * 添加用户服务
	 * @param param
	 */
	public Integer insertUserTagService(UserTag param);
	
	/**
	 * 修改用户服务
	 * @param param
	 */
	public void updateUserTagService(UserTag param);
	
	/***
	 * 删除用户服务
	 * @param param
	 */
	public void deleteUserTagService(Integer tagId);
	
	/***
	 * 根据Code查询用户服务
	 * @param param
	 */
	public List<UserTag>  selectUserTagService();
	
	public void updateTagInfo(UserTag param);
}
