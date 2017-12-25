package com.lt.manager.service.impl.sys;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.AuthInfoVo;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.MenusAuth;
import com.lt.manager.bean.sys.MenusMap;
import com.lt.manager.bean.sys.MenusVO;
import com.lt.manager.dao.sys.MenusDao;
import com.lt.manager.service.sys.IAuthService;
import com.lt.manager.service.sys.IMenusService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

@Service
public class MenusServiceImpl implements IMenusService {
	Logger logger = LoggerTools.getInstance(this.getClass());
	@Autowired
	private MenusDao menusDao;
	@Autowired
	private IAuthService authService;

	@Override
	public Menus selectMenusById(Integer id) {
		return menusDao.selectById(id);
	}


	@Override
	public void updateMenus(Menus menus) {
		menusDao.updateInfo(menus);
	}

	@Override
	@Transactional
	public void removeById(Integer id) {
		//删除角色菜单关联关系
		menusDao.deleteForMenusMap("menus_id", Integer.valueOf(id));
		logger.info("删除角色菜单关联关系成功");
		//删除菜单权限关联关系
		menusDao.deleteForMenusAuth("menus_id", Integer.valueOf(id));
		logger.info("删除菜单权限关联关系成功");
		//删除菜单
		menusDao.deleteById(id);
		logger.info("删除菜单成功");

	}

	@Override
	public void saveMenus(Menus menus) {
		menusDao.insertInfo(menus);
	}

	@Override
	@Transactional
	public void authOfDist(String menusId, String authIds) {
		menusDao.deleteForMenusAuth("menus_id", Integer.valueOf(menusId));
		logger.info("根据menusid:" + menusId + "删除所有menus_auth表数据");
		if (StringTools.isNotEmpty(authIds)) {
			logger.info("authIds :" + authIds);
			if (authIds.contains(",")) {
				String[] strs = authIds.split(",");
				for (String str : strs) {
					MenusAuth info = new MenusAuth();
					info.setAuthId(Integer.valueOf(str));
					info.setMenusId(Integer.valueOf(menusId));
					menusDao.insertInfoForMenusAuth(info);
					logger.info("保存菜单权限关联关系 menusId:" + menusId + "    authId :" + str + "成功");
				}
			} else {
				MenusAuth info = new MenusAuth();
				info.setAuthId(Integer.valueOf(authIds));
				info.setMenusId(Integer.valueOf(menusId));
				menusDao.insertInfoForMenusAuth(info);
				logger.info("保存 菜单权限关联关系 menusId:" + menusId + "    authId :" + authIds + "成功");
			}
		}
	}

	@Override
	public String getMenusAuthListByRoleId(Integer roleId) {
		List<MenusVO> menusQueryList = authService.selectMenusListByRoleId(roleId);
		String json = JSON.toJSONString(menusQueryList);
		logger.info("所有菜单权限json = " + json);
		return json;
		/**/
	}

	@Override
	@Transactional
	public void menusOfDist(String roleId, String menusIds) {
		//删除角色对应的权限然后重新分配
		menusDao.deleteMenusMapByRoleId(Integer.valueOf(roleId));
		logger.info("根据roleId:" + roleId + "删除所有menusmap表数据");
		if (StringTools.isNotEmpty(menusIds)) {
			if (menusIds.contains(",")) {
				String[] strs = menusIds.split(",");
				//去重复
				HashSet<String> userAIdSet = new HashSet<String>(Arrays.asList(strs));
				for (String str : userAIdSet) {
					saveMenuIdAndRoleId(str, roleId);
				}
			} else {
				saveMenuIdAndRoleId(menusIds, roleId);
			}
		}
	}

	/**
	 * 保存部门菜单关联关系
	 * @param menusId
	 * @param roleId
	 */
	private void saveMenuIdAndRoleId(String menusId,String roleId){
		try {
			MenusMap info = new MenusMap();
			info.setMenuId(Integer.valueOf(menusId));
			info.setRoleId(Integer.valueOf(roleId));
			menusDao.insertInfoForMenusMap(info);
			logger.info("保存 角色菜单关联关系 roleId:" + roleId + "    MenuId :" + menusId + "成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("保存部门菜单关联关系异常",e);
		}
	}
	 
	
	@Override
	public List<Menus> selectMenusParent(Integer pid) {
		return menusDao.selectByPid(pid);
	}

	@Override
	public List<MenusVO> selectMenusOfAuth(Integer authId) {
		//查询权限菜单，在权限页分配权限到菜单时使用
		List<MenusVO> result = new ArrayList<>();
		//获取所有一级菜单
		List<Menus> list = menusDao.selectByPid(-1);
		for (Menus menus : list) {
			MenusVO vo = new MenusVO();
			BeanUtils.copyProperties(menus, vo);
			//添加子菜单
			List<Menus> chs = menusDao.selectByPid(menus.getId());
			for (Menus menus2 : chs) {
				MenusVO child = new MenusVO();
				BeanUtils.copyProperties(menus2, child);
				Auth au = menusDao.selectAuthByMenuAndAuthId(menus2.getId(), authId);
				//标记当前菜单已选
				if (au != null) {
					child.setChecked(true);
				}else{
					child.setChecked(false);
				}
				vo.addChild(child);
			}
			Auth au = menusDao.selectAuthByMenuAndAuthId(menus.getId(), authId);
			
			//标记当前菜单已选
			if (au != null) {
				vo.setChecked(true);
			}else{
				vo.setChecked(false);
			}
			result.add(vo);
		}

		return result;
	}

//	@Override
//	public String getMenusAuthList() {
//		logger.info("获取所有菜单目录与子目录的权限 返回类型json {parent,children,auth}");
//		//父级Menus
//		List<MenusVO> menusQueryList = new ArrayList<>();
//		//查询所有1级目录
//		List<Menus> list = menusDao.selectByPid(-1);
//		logger.info("获取父级菜单  ：一级菜单个数total:" + list.size());
//		if (null != list && list.size() > 0) {
//			for (Menus menus : list) {
//				MenusVO menuVO = new MenusVO();
//				BeanUtils.copyProperties(menus, menuVO);
//				//根据父级查询其二级目录
//				List<Menus> list2 = menusDao.selectByPid(menus.getId());
//				logger.info("获取根据父级查询的二级目录 ：  parentId:" + menuVO.getId() + "的子目录total:" + list2.size());
//				//二级menus
//				List<MenusVO> subMenus = new ArrayList<>();
//				if (null != list2 && list2.size() > 0) {
//					for (Menus menus2 : list2) {
//						MenusVO sub = new MenusVO();
//						BeanUtils.copyProperties(menus2, sub);
//						//2级目录的权限
//						List<Auth> auths = menusDao.selectAuthByMenusId(menus2.getId());
//						logger.info("获取根据二级目录查询的权限 ： 二级目录ID:" + sub.getId() + "的子权限个数total:" + auths.size());
//						//二级目录的权限加入 二级menus级对象
//						logger.info("auths = {}",JSONObject.toJSON(auths));
//						sub.setAuthsByOrg(auths);
//						subMenus.add(sub);
//					}
//				}
//				//把二级目录加入父级对象
//				menuVO.setChildren(subMenus);
//				menusQueryList.add(menuVO);
//			}
//		}
//		String json = JSON.toJSONString(menusQueryList);
//		logger.info("所有菜单权限json = " + json);
//		return json;
//	}

	@Override
	public String getMenusAuthList() {
		logger.info("获取所有菜单目录与子目录的权限 返回类型json {parent,children,auth}");
		//父级Menus
		List<MenusVO> menusQueryList = new ArrayList<>();
		//查询所有1级目录
		List<Menus> list = menusDao.selectByPid(-1);
		logger.info("获取父级菜单  ：一级菜单个数total:" + list.size());
		if (null != list && list.size() > 0) {
			for (Menus menus : list) {
				MenusVO menuVO = new MenusVO();
				BeanUtils.copyProperties(menus, menuVO);
				//根据父级查询其二级目录
				List<Menus> list2 = menusDao.selectByPid(menus.getId());
				logger.info("获取根据父级查询的二级目录 ：  parentId:" + menuVO.getId() + "的子目录total:" + list2.size());
				//二级menus
				List<MenusVO> subMenus = new ArrayList<>();
				//说明还有下级目录
				if (null != list2 && list2.size() > 0) {
					for (Menus menus2 : list2) {
						MenusVO sub = new MenusVO();
						BeanUtils.copyProperties(menus2, sub);
						List<Menus> ls = menusDao.selectByPid(menus2.getId());
						List<MenusVO> sbMenus = new ArrayList<>();
						if(null == list || ls.size() == 0 ){
							//2级目录的权限
							List<Auth> auths = menusDao.selectAuthByMenusId(menus2.getId());
							logger.info("获取根据二级目录查询的权限 ： 二级目录ID:" + sub.getId() + "的子权限个数total:" + auths.size());
							//二级目录的权限加入 二级menus级对象
							logger.info("auths = {}",JSONObject.toJSON(auths));
							sub.setAuthsByOrg(auths);
							
						}else{
							for (Menus ms : ls) {
								MenusVO su = new MenusVO();
								BeanUtils.copyProperties(ms, su);
								List<Auth> auths = menusDao.selectAuthByMenusId(ms.getId());
								logger.info("获取根据三级目录查询的权限 ： 三级目录ID:" + su.getId() + "的子权限个数total:" + auths.size());
								//二级目录的权限加入 二级menus级对象
								logger.info("auths = {}",JSONObject.toJSON(auths));
								su.setAuthsByOrg(auths);
								sbMenus.add(su);
							}
							sub.setChildren(sbMenus);
						}
						subMenus.add(sub);
					}
				}
				//把二级目录加入父级对象
				menuVO.setChildren(subMenus);
				menusQueryList.add(menuVO);
			}
		}
		String json = JSON.toJSONString(menusQueryList);
		logger.info("所有菜单权限json = " + json);
		return json;
	}
	
	@Override
	public List<Menus> selectAllSecondaryMenus() {
		return menusDao.selectAllSecondaryMenus();
	}

	@Override
	public void insertMenusAuth(Integer menusId, Integer authId) {
		MenusAuth menusAuth = new MenusAuth();
		menusAuth.setAuthId(authId);
		menusAuth.setMenusId(menusId);
		menusDao.insertInfoForMenusAuth(menusAuth);
		
	}

	@Override
	@Transactional
	public void removeByAuthId(Integer authId) throws Exception{
		int i = authService.removeById(authId);
		if (i == 0) {
			logger.error("删除操作权限失败,存在角色权限关联关系");
			throw new LTException(LTResponseCode.MA00018);
		}
	}

	@Override
	public void removeMenusAuthById(Integer id) {
		menusDao.deleteForMenusAuth("id", id);
	}
	
	@Override
	public MenusAuth selectByMIdAndAId(Integer menusId, Integer authId) {
		return menusDao.selectByMIdAndAId(menusId, authId);
	}

	@Override
	public Page<Menus> selectMenus(Map<String, Object> paramMap, Integer rows,
			Integer page) {
		Page<Menus> pg = new Page<Menus>();
		pg.setPageNum(page);
		pg.setPageSize(rows);
		paramMap.put("limit1", (page-1)*rows);
		paramMap.put("limit2", rows);
		List<Menus> list = menusDao.selectByQuery(paramMap);
		pg.addAll(list);
		pg.setTotal(menusDao.selectByQueryCount(paramMap));
		return pg;
	}
}
