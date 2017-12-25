package com.lt.manager.service.user;

import java.util.List;

import com.lt.manager.bean.user.UserService;
import com.lt.util.error.LTException;
import com.lt.util.utils.model.Response;

/**
 * 用户管理service
 * @author licy
 *
 */
public interface IUserServiceManage {


	
	/**
	 * 编辑商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editUserService(UserService param) throws Exception;

	/**
	 * 删除商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeUserService(Integer id) throws Exception;
	
	
	/**
	 *g根据用户信息查询银行卡
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public List<UserService>  findUserServiceById(String id);

	void addUserService(UserService param) throws Exception;
	
	/**
	 * 修改用户入金最大限额
	 * @param userId
	 * @param amt
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月10日 下午4:46:49
	 */
	void updateUserDailyTopAmt(String userId,Double amt) throws LTException;
	
	/**
	 * 获取用户最大入金限额
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月10日 下午5:03:14
	 */
	Double getUserDailyTopAmt(String userId) throws LTException;

	Response withdrawApply(String userId, Double outAmount);
}
