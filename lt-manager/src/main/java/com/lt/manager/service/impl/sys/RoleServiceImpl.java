package com.lt.manager.service.impl.sys;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.RoleAuth;
import com.lt.manager.bean.sys.RoleMap;
import com.lt.manager.bean.sys.RoleVo;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.dao.sys.RoleDao;
import com.lt.manager.service.sys.IRoleService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

@Service
public class RoleServiceImpl implements IRoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Role selectRoleById(Integer id) {
		return roleDao.selectById(id);
	}


	@Override
	@Transactional
	public void updateRole(Role role) {
		roleDao.updateInfo(role);
	}

	@Override
	@Transactional
	public int removeById(Integer id) {
		/*用户角色关联表 存在关联关系 不允许删除 */
		List<RoleMap> list = roleDao.selectByIdForRoleMap("role_id", id);
		if(list!=null&&list.size()>0){
			return 0;
		}
		/*删除角色权限关联关系*/
		roleDao.deleteForRoleAuth("role_id", id);
		/* 删除角色 */
		roleDao.deleteById(id);
		return 1;
	}

	@Override
	@Transactional
	public void saveRole(Role role) {
		roleDao.insertInfo(role);
	}

	@Override
	@Transactional
	public void authOfDist(String id, String ids) {
		//删除原来的部门权限关联关系
		roleDao.deleteForRoleAuth("role_id", Integer.valueOf(id));
		//保存新的部门权限关系
		if(StringTools.isNotEmpty(ids)){
			if(ids.contains(",")){
				String[] strs = ids.split(",");
				//去重复
				HashSet<String> userAIdSet = new HashSet<String>(Arrays.asList(strs));
				for (String str : userAIdSet) {
					saveAuthIdAndRoleId(str, id);
				}
			}else{
				saveAuthIdAndRoleId(ids, id);
			}
		}
	}

	/**
	 * 保存权限部门关联关系
	 * @param authId
	 * @param roleId
	 */
	private void saveAuthIdAndRoleId(String authId,String roleId){
		try {
			
			RoleAuth info = new RoleAuth();
			info.setAuthId(Integer.valueOf(authId));
			info.setRoleId(Integer.valueOf(roleId));
			roleDao.insertInfoForRoleAuth(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("保存权限部门关联关系SQL异常", e);
		}
	}
	
	@Override
	@Transactional
	public void roleOfDist(String staffId, String roleIds) {
		//删除员工部门关联关系
		roleDao.deleteForRoleMap("staff_id", Integer.valueOf(staffId));
		//保存新的员工部门关联关系
		if(StringTools.isNotEmpty(roleIds)){
			if(roleIds.contains(",")){
				String[] strs = roleIds.split(",");
				//去重复
				HashSet<String> userAIdSet = new HashSet<String>(Arrays.asList(strs));
				for (String str : userAIdSet) {
					saveStaffIdAndRoleId(staffId, str);
				}
			}else{
				saveStaffIdAndRoleId(staffId, roleIds);
			}
		}
	}
	/**
	 * 保存员工部门关联关系
	 * @param staffId
	 * @param roleId
	 */
	private void saveStaffIdAndRoleId(String staffId,String roleId){
		try {
			RoleMap info = new RoleMap();
			info.setRoleId(Integer.valueOf(roleId));
			info.setStaffId(Integer.valueOf(staffId));
			roleDao.insertInfoForRoleMap(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("保存权限部门关联关系SQL异常", e);
		}
	}
	@Override
	public void distRoleToStaff(String roleId, List<Integer> staffIds) {
		try {
			roleDao.deleteForRoleMap("role_id", Integer.valueOf(roleId));
			for (Integer staffId : staffIds) {
				saveStaffIdAndRoleId(staffId+"", roleId);
			}
		} catch (Exception e) {
			throw new LTException(LTResponseCode.MA00026);
		}
		
	}

	@Override
	public List<Staff> selectStaffListById(Integer roleId) {
		return roleDao.selectStaffListById(roleId);
	}


	@Override
	public List<Integer> selectRoleListById(Integer authId) {
		return roleDao.selectRoleListById(authId);
	}

	@Override
	public Page<RoleVo> selectRole(Map<String, Object> paramMap, Integer rows,
			Integer page) {
		Page<RoleVo> pg = new Page<RoleVo>();
		pg.setPageNum(page);
		pg.setPageSize(rows);
		paramMap.put("limit1", (page-1)*rows);
		paramMap.put("limit2", rows);
		List<RoleVo> list = roleDao.selectByQuery(paramMap);
		pg.addAll(list);
		pg.setTotal(roleDao.selectByQueryCount(paramMap));
		return pg;
	}


	@Override
	public List<Role> selectAllRole() {
		List<Role> list = roleDao.selectAllRole();
		return list;
	}


	@Override
	public List<Role> selectRoleByStaffId(Integer id) {
		return roleDao.selectRoleByStaffId(id);
	}


	@Override
	public List<Staff> selectStaffListByIsNotId(Integer roleId) {
		return roleDao.selectStaffListByIsNotId(roleId);
	}


	@Override
	public void deleteForRoleMap(String string, Integer id) {
		roleDao.deleteForRoleMap(string, id);
	}
}
