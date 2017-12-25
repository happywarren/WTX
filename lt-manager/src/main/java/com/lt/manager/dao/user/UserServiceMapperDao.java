package com.lt.manager.dao.user;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.user.UserServiceMapper;

/**
 * 商品类型管理dao
 * @author licy
 *
 */
public interface UserServiceMapperDao {
	
	public List<UserServiceMapper>  selectUserServiceMapperByUserId(String userId);
	
	
	public void deleteUserServiceMapper(UserServiceMapper mapper);


	public List<String> findUserServiceByCode(String serviceCode);
	
	public void deleteUserService(Map<String,Object> map);
	
	public void insertServiceMappper(Map<String,Object> map);
	
	
	/**
	 * 修改券商id
	 * @param userId
	 */
	public void updateUserInvestorId(String userId);	
}
