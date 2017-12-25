package com.lt.manager.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.RoleAuth;
import com.lt.manager.bean.sys.RoleMap;
import com.lt.manager.bean.sys.RoleVo;
import com.lt.manager.bean.sys.Staff;

public interface RoleDao {

	//role增删改查
	public void insertInfo(Role role)throws  RuntimeException;
	
	public void deleteById(Integer id)throws RuntimeException; 
	
	public void updateInfo(Role role)throws RuntimeException;
	
	public Role selectById(Integer id)throws RuntimeException;
	
	//rolemap 增删改查
    public void insertInfoForRoleMap(RoleMap roleMap)throws  RuntimeException;
	
	public void deleteForRoleMap(@Param("field")String field,@Param("id")Integer id)throws RuntimeException; 
	
	public List<RoleMap> selectByIdForRoleMap(@Param("field")String field,@Param("id")Integer id)throws RuntimeException;
	//role_auth 增删改查
	public void insertInfoForRoleAuth(RoleAuth roleAuth)throws  RuntimeException;
		
	public void deleteForRoleAuth(@Param("field")String field,@Param("id")Integer id)throws RuntimeException; 
	
	public List<RoleAuth> selectByIdForRoleAuth(@Param("field")String field,@Param("id")Integer id)throws RuntimeException;
	
	public List<RoleAuth> selectRoleAuthForId(Integer id)throws RuntimeException;

	public List<Auth> selectByRoleId(List<RoleMap> list);

	public List<Role> selectRoleByStaffId(Integer staffId);

	public List<Staff> selectStaffListById(Integer roleId);

	public List<Integer> selectRoleListById(Integer authId);

	public List<RoleVo> selectByQuery(Map<String, Object> paramMap);

	public Integer selectByQueryCount(Map<String, Object> paramMap);

	/**
	 * 查询所有role
	 * @return
	 */
	public List<Role> selectAllRole();

	public List<Staff> selectStaffListByIsNotId(Integer roleId);
		
	
}
