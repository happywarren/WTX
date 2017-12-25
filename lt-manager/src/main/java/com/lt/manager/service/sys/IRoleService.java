package com.lt.manager.service.sys;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.RoleVo;
import com.lt.manager.bean.sys.Staff;

public interface IRoleService {

	Role selectRoleById(Integer id)throws Exception;

	void updateRole(Role role)throws Exception;

	/**
	 * 
	 *	
	 * 描述:删除角色  同时删除其关联关系  若存在用户角色有关联关系 不允许删除
	 *
	 * @author  郭达望
	 * @created 2015年6月3日 上午10:16:06
	 * @since   v1.0.0 
	 * @param id
	 * @return
	 * @throws Exception
	 * @return  int
	 */
	int removeById(Integer id)throws Exception;

	void saveRole(Role role)throws Exception;

	/**
	 * 
	 *	
	 * 描述:角色管理分配权限
	 *
	 * @author  郭达望
	 * @created 2015年6月2日 下午5:54:29
	 * @since   v1.0.0 
	 * @param id
	 * @param ids
	 * @return  void
	 */
	void authOfDist(String id, String ids)throws Exception;
	
	
	/**
	 * 
	 *	
	 * 描述:分配角色
	 *
	 * @author  郭达望
	 * @created 2015年6月2日 下午6:03:04
	 * @since   v1.0.0 
	 * @param staffId
	 * @param roleIds
	 * @return  void
	 */
	void roleOfDist(String staffId,String roleIds)throws Exception;

	/**
	 * 
	 *	
	 * 描述:给部门分配用户
	 * 删除原来的
	 * 新增新的
	 *
	 * @author  郭达望
	 * @created 2015年8月10日 下午4:38:18
	 * @since   v1.0.0 
	 * @param roleId
	 * @param staffIds
	 * @return
	 * @return  String
	 */
	void distRoleToStaff(String roleId, List<Integer> staffIds);

	/**
	 * 
	 *	
	 * 描述:根据RoleID查询后台用户
	 *
	 * @author  郭达望
	 * @created 2015年8月10日 下午5:55:52
	 * @since   v1.0.0 
	 * @param valueOf
	 * @return
	 * @return  List<Staff>
	 */
	List<Staff> selectStaffListById(Integer valueOf);

	List<Integer> selectRoleListById(Integer valueOf);

	Page<RoleVo> selectRole(Map<String, Object> paramMap, Integer rows,
			Integer page);

	/**
	 * 查询所有Role
	 * @return
	 */
	List<Role> selectAllRole();

	/**
	 * 查询员工所属于哪些部门
	 * @param id
	 * @return
	 */
	List<Role> selectRoleByStaffId(Integer id);

	/**
	 * 非部门员工
	 * @param valueOf
	 * @return
	 */
	List<Staff> selectStaffListByIsNotId(Integer roleId);

	void deleteForRoleMap(String string, Integer id);

}
