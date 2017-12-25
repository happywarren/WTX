package com.lt.manager.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.MenusAuth;
import com.lt.manager.bean.sys.MenusMap;
import com.lt.manager.bean.sys.RoleMap;

public interface MenusDao {

	//menus 增删改查
	public void insertInfo(Menus menus) throws RuntimeException;

	public void deleteById(Integer id) throws RuntimeException;

	public void updateInfo(Menus menus) throws RuntimeException;

	public Menus selectById(Integer id) throws RuntimeException;

	public List<Menus> selectByPid(Integer pid) throws RuntimeException;

	public List<Integer> selectByName(String name) throws RuntimeException;

	//menusmap 增删改查
	public void insertInfoForMenusMap(MenusMap menusMap) throws RuntimeException;

	public void deleteForMenusMap(@Param("field") String field, @Param("id") Integer id) throws RuntimeException;
	public void deleteMenusMapByRoleId( @Param("roleId") Integer roleId) ;

	public MenusMap selectMenusMapByRoleAndMenu(@Param("menuId") Integer menuId, @Param("roleId") Integer roleId);

	//menus_auth 增删改查
	public void insertInfoForMenusAuth(MenusAuth menusAuth) throws RuntimeException;

	public void deleteForMenusAuth(@Param("field") String field, @Param("id") Integer id) throws RuntimeException;

	public List<Menus> selectByRoleId(@Param("list") List<RoleMap> list, @Param("pid") Integer pid);

	public List<Auth> selectAuthByMenusId(Integer id);

	public Auth selectAuthByMenuAndAuthId(@Param("menuId") Integer menuId, @Param("authId") Integer authId);

	public List<Menus> selectAllSecondaryMenus();

	public MenusAuth selectByMIdAndAId(@Param("menusId")Integer menusId, @Param("authId")Integer authId);

	public List<Menus> selectByQuery(Map<String, Object> paramMap);
	
	public Integer selectByQueryCount(Map<String, Object> paramMap);
}
