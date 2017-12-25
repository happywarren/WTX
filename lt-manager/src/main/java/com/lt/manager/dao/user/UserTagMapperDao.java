package com.lt.manager.dao.user;

import java.util.List;

import com.lt.manager.bean.user.UserTagMapper;

/**
 * 商品类型管理dao
 * @author licy
 *
 */
public interface UserTagMapperDao {
	
	public List<UserTagMapper>  selectUserTagMapperByUserId(String userId);
	
	public List<UserTagMapper>  selectUserTagMapper();
	
	public void  insertUserTagMapper(UserTagMapper param);
	
	public void  deleteUserTagMapper(UserTagMapper param);
	
	public void deleteUserTagMapperByTagId(Integer tagId);
	
	
}
