package com.lt.manager.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.AuthInfoVo;
import com.lt.manager.bean.sys.AuthMap;
import com.lt.manager.bean.sys.RoleAuth;

public interface AuthDao {

	//
	public int addAuth(List<AuthMap> authMapList);

	public int delAuth(List<Integer> authIdList);

	//
	public Auth selectAuthById(int id);

	public List<Auth> selectAuthList();

	public List<Auth> selectStaffAuth(int staffId);


	public void deleteAuthMap(Integer staffId);

	public void addAuthMap(@Param("idList") List<Integer> idList, @Param("staffId") Integer staffId);

	public int saveAuth(Auth auth);


	public void upadteAuth(Auth auth);

	public void delAuthById(Integer id);
	

	/**
	 * 
	 *	
	 * 描述:查询角色权限对应关系是否存在
	 *
	 * @author  jiupeng
	 * @created 2015年6月25日 下午10:27:28
	 * @since   v1.0.0 
	 * @param roleId
	 * @param authId
	 * @return
	 * @return  RoleAuth
	 */
	public RoleAuth getRoleAuthByRoleAndAuthId(@Param("roleId") Integer roleId, @Param("authId") Integer authId);

	/**
	 * 查询权限信息
	 * @param paramMap
	 * @param rows
	 * @param page
	 * @return
	 */
	public List<AuthInfoVo> selectAuth(Map<String, Object> paramMap);
	
	/**
	 * 查询权限个数
	 * @param paramMap
	 * @param rows
	 * @param page
	 * @return
	 */
	public Integer selectAuthCount(Map<String, Object> paramMap);

	public Auth getAuthByUrl(String url);

	

}
