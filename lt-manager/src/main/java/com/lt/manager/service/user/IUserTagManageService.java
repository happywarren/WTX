package com.lt.manager.service.user;

import java.util.List;

import com.lt.manager.bean.user.UserTag;
import com.lt.util.utils.model.Response;

/**
 * 用户管理service
 * @author licy
 *
 */
public interface IUserTagManageService {

	/**
	 * 添加商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer addUserTagService(UserTag param) throws Exception;
	
	/**
	 * 编辑商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editUserTagService(UserTag param) throws Exception;

	/**
	 * 删除商品类型
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeUserTagService(Integer id) throws Exception;
	
	
	/**
	 *g根据用户信息查询银行卡
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public List<UserTag>  findUserTagService();

}
