package com.lt.manager.service.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.MenusAuth;
import com.lt.manager.bean.sys.MenusVO;

public interface IMenusService {

	Menus selectMenusById(Integer id);

	void updateMenus(Menus menus);

	void removeById(Integer id);

	void saveMenus(Menus menus);

	/**
	 * 
	 *	
	 * 描述:分配权限
	 *
	 * @author  郭达望
	 * @created 2015年6月2日 下午4:03:44
	 * @since   v1.0.0 
	 * @param id 菜单id
	 * @param ids 多个权限id
	 * @return  void
	 */
	void authOfDist(String id, String ids);

	/**
	 * 
	 *	
	 * 描述:获得所有的菜单权限
	 *
	 * @author  郭达望
	 * @created 2015年6月2日 下午4:11:59
	 * @since   v1.0.0 
	 * @return
	 * @return  String
	 */
	String getMenusAuthList();

	/**
	 * 
	 *	
	 * 描述:分配菜单
	 *
	 * @author  郭达望
	 * @created 2015年6月2日 下午5:50:31
	 * @since   v1.0.0 
	 * @param roleId
	 * @param menusIds
	 * @return  void
	 */
	void menusOfDist(String roleId, String menusIds);

	/**
	 * 
	 *	
	 * 描述:获得所有父级菜单
	 *
	 * @author  郭达望
	 * @created 2015年6月4日 上午11:46:40
	 * @since   v1.0.0 
	 * @return
	 * @return  List<Menus>
	 */
	List<Menus> selectMenusParent(Integer pid);

	/**
	 * 
	 * 描述:获取权限所属的菜单列表，权限分配时使用
	 *
	 * @author  jiupeng
	 * @created 2015年6月25日 下午2:59:17
	 * @since   v1.0.0 
	 * @param authId
	 * @return
	 * @return  List<MenusVO>
	 */
	List<MenusVO> selectMenusOfAuth(Integer authId);

	/**
	 * 
	 *	
	 * 描述:获得角色的菜单权限
	 *
	 * @author  郭达望
	 * @created 2015年6月5日 下午4:38:03
	 * @since   v1.0.0 
	 * @param roleId
	 * @return
	 * @return  String
	 */
	String getMenusAuthListByRoleId(Integer roleId);

	List<Menus> selectAllSecondaryMenus();
	
	void insertMenusAuth(Integer menusId,Integer authId);

	void removeByAuthId(Integer authId) throws Exception;
	
	MenusAuth selectByMIdAndAId(Integer menusId,Integer authId);

	void removeMenusAuthById(Integer id);

	Page<Menus> selectMenus(Map<String, Object> paramMap, Integer rows,
			Integer page);

}
