package com.lt.manager.service.user;

import java.util.List;

import com.lt.manager.bean.user.UserServiceMapper;
/**
 * 用户管理service
 * @author licy
 *
 */
public interface IUserServiceMapper {
	
	public List<UserServiceMapper>  findUserServiceById(String userId);

	/**
	 * 查询券商用户
	 * @param serviceCode
	 * @return
	 */
	List<String> findUserServiceByCode(String serviceCode);
	
	/**
	 * 修改用户服务
	 * @param userId
	 * @param codes
	 */
	public void modifyUserServiceMapper(String userId,String codes,String all);

}
