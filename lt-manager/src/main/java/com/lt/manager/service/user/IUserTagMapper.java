package com.lt.manager.service.user;

import java.util.List;

import com.lt.manager.bean.user.UserTag;
import com.lt.manager.bean.user.UserTagMapper;
/**
 * 用户管理service
 * @author licy
 *
 */
public interface IUserTagMapper {
	
	public List<UserTagMapper>  findUserTagById(String userId);

	public List<UserTagMapper>  findUserTag();
	
	public void  addUserTagMapper(UserTagMapper param);
	
	public void  removeUserTagMapper(UserTagMapper param);
	
	public void  removeUserTag(Integer id);
	
	public void  editUserTag(UserTag param);
}
