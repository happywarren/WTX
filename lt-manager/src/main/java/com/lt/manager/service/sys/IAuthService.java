package com.lt.manager.service.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.AuthInfoVo;
import com.lt.manager.bean.sys.AuthMap;
import com.lt.manager.bean.sys.AuthVO;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.MenusVO;
import com.lt.manager.bean.sys.Role;

/**
 *
 * 描述:
 *
 * @author C.C
 * @created 2015年4月24日 下午4:04:20
 */
public interface IAuthService {

	public int removeById(Integer id) throws Exception;

	public void updateAuth(Auth auth) throws Exception;

	public Auth selectAuthById(Integer valueOf) throws Exception;

	public void saveAuth(Auth auth);

	public List<Auth> selectAuthList();

	public List<AuthVO> selectAuthListByMenuId(Integer menuId);

	/**
	 * 
	 *
	 * 描述:查询 用户去重复后所有菜单 树级结构组装返回对象
	 *
	 * @author 郭达望
	 * @created 2015年6月3日 下午4:38:02
	 * @since v1.0.0
	 * @param id
	 * @return
	 * @return List<MenusListQuery>
	 */
	List<MenusVO> selectMenusListByStaffId(Integer id);

	/**
	 * 
	 *
	 * 描述:查询用户去重复后所有权限
	 *
	 * @author 郭达望
	 * @created 2015年6月3日 下午4:38:37
	 * @since v1.0.0
	 * @param id
	 * @return
	 * @return List<Auth>
	 * @throws Exception 
	 */
	List<Auth> selectAuthListByStaffId(Integer id) throws Exception;

	/**
	 * 
	 *
	 * 描述:根据员工id查询他/她所有角色
	 *
	 * @author 郭达望
	 * @created 2015年6月4日 下午4:49:11
	 * @since v1.0.0
	 * @param staffId
	 * @return
	 * @return List<Role>
	 */
	List<Role> selectRoleByStaffId(Integer staffId);

	/**
	 * 
	 *
	 * 描述:查询菜单所有权限，角色菜单所有权限中设置checked:true
	 *
	 * @author 郭达望
	 * @created 2015年6月5日 上午10:58:02
	 * @since v1.0.0
	 * @param id
	 * @param type
	 * @return
	 * @return List<MenusQuery>
	 */
	List<MenusVO> selectMenusListByRoleId(Integer id);

	/**
	 * 
	 *
	 * 描述:分配权限到菜单
	 *
	 * @author jiupeng
	 * @created 2015年6月25日 下午7:19:22
	 * @since v1.0.0
	 * @param authId
	 * @param menusIds
	 * @return
	 * @return String
	 */
	String distAuthToMenus(Integer authId, List<Integer> menusIds);

	/**
	 * 
	 *
	 * 描述:分配权限到菜单
	 *
	 * @author 郭达望
	 * @created 2015年8月10日 下午5:26:51
	 * @since v1.0.0
	 * @param authId
	 * @param rids
	 * @return
	 * @return String
	 */
	public void distAuthToRole(Integer authId, List<Integer> rids);



	Page<AuthInfoVo> selectAuth(Map<String, Object> paramMap, Integer rows,
			Integer page);

	List<Menus> selectMenusBottom();

}
