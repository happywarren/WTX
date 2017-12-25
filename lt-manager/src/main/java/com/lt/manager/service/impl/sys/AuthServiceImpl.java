package com.lt.manager.service.impl.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.AuthInfoVo;
import com.lt.manager.bean.sys.AuthVO;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.MenusAuth;
import com.lt.manager.bean.sys.MenusVO;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.RoleAuth;
import com.lt.manager.bean.sys.RoleMap;
import com.lt.manager.dao.sys.AuthDao;
import com.lt.manager.dao.sys.MenusDao;
import com.lt.manager.dao.sys.RoleDao;
import com.lt.manager.service.sys.IAuthService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

@Service
public class AuthServiceImpl implements IAuthService {
    Logger logger = LoggerTools.getInstance(this.getClass());
    @Autowired
    private AuthDao authDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenusDao menusDao;


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void updateAuthMap(List<Integer> idList, Integer staffId) {
        authDao.deleteAuthMap(staffId);
        authDao.addAuthMap(idList, staffId);
    }


    @Override
    @Transactional
    public int removeById(Integer id) throws Exception {
        List<RoleAuth> list = roleDao.selectRoleAuthForId(id);
        if (list != null && list.size() > 0) {
            return 0;
        }
        //菜单权限关联关系删除
        menusDao.deleteForMenusAuth("auth_id", id);
        //权限删除
        authDao.delAuthById(id);
        return 1;
    }

    @Override
    public void updateAuth(Auth auth) {
        authDao.upadteAuth(auth);

    }

    @Override
    public Auth selectAuthById(Integer id) {
        return authDao.selectAuthById(id);
    }

    @Override
    @Transactional
    public void saveAuth(Auth auth) {
        authDao.saveAuth(auth);
    }

    @Override
    public List<Auth> selectAuthList() {
        return authDao.selectAuthList();
    }

    @Override
    public List<AuthVO> selectAuthListByMenuId(Integer menuId) {
        List<Auth> auths = authDao.selectAuthList();
        List<AuthVO> result = new ArrayList<>();
        for (Auth auth : auths) {
            AuthVO aq = new AuthVO();
            BeanUtils.copyProperties(auth, aq);
            Auth au = menusDao.selectAuthByMenuAndAuthId(menuId, auth.getId());
            if (au != null) {
                aq.setChecked(true);
            } else {
                aq.setChecked(false);
            }
            result.add(aq);
        }
        return result;
    }

    @Override
    public List<Auth> selectAuthListByStaffId(Integer id) throws Exception {
        //根据登录账户id 查询旗下角色
        List<RoleMap> list = roleDao.selectByIdForRoleMap("staff_id", id);
        if (list == null || list.size() < 1) {
            return null;
        }
        List<Auth> authList = roleDao.selectByRoleId(list);
        return authList;
    }

    @Override
    public List<MenusVO> selectMenusListByStaffId(Integer id) {
        //根据登录账户id 查询旗下角色
        List<RoleMap> list = roleDao.selectByIdForRoleMap("staff_id", id);
        if (list == null || list.size() < 1) {
            return null;
        }
        //查询用户一级菜单
        List<Menus> menusList = menusDao.selectByRoleId(list, -1);
        List<MenusVO> queryList = new ArrayList<>();
        for (int i = 0; i < menusList.size(); i++) {
            MenusVO vo = new MenusVO();
            BeanUtils.copyProperties(menusList.get(i), vo);
            //查询用户的二级菜单
            List<Menus> menusList2 = menusDao.selectByRoleId(list, menusList.get(i).getId());
            List<MenusVO> queryList2 = new ArrayList<>();
            for (int j = 0; j < menusList2.size(); j++) {
                MenusVO ms = new MenusVO();
                BeanUtils.copyProperties(menusList2.get(j), ms);
                //查询三级菜单
                List<Menus> ls = menusDao.selectByPid(ms.getId());
                logger.info("三级：" + JSONObject.toJSONString(ls));
                ms.setChildrenOfMenu(ls);
                queryList2.add(ms);
            }
            vo.setChildren(queryList2);
            queryList.add(vo);
        }
        return queryList;
    }

    @Override
    public List<MenusVO> selectMenusListByRoleId(Integer id) {
        try {
            //父级Menus
            List<MenusVO> result = new ArrayList<>();
            /*查询所有1级目录*/
            List<Menus> list = menusDao.selectByPid(-1);
            logger.info("获取父级菜单  ：一级菜单个数total:{},json:{}" , list.size(),JSONObject.toJSONString(list));
            if (null != list && list.size() > 0) {
                for (Menus menus : list) {
                    MenusVO vo = new MenusVO();
                    BeanUtils.copyProperties(menus, vo);
                    //根据父级查询其二级目录
                    List<Menus> list2 = menusDao.selectByPid(menus.getId());
                    logger.info("获取根据父级查询的二级菜单名 ：  {},个数：{},json:{}" ,menus.getName() , list2.size(),JSONObject.toJSONString(list2));
                    //二级menus
                    List<MenusVO> subList = new ArrayList<>();
                    if (null != list2 && list2.size() > 0) {
                        for (Menus menus2 : list2) {
                            MenusVO subVO = new MenusVO();
                            BeanUtils.copyProperties(menus2, subVO);

                            //查询三级菜单
                            List<Menus> ls = menusDao.selectByPid(menus2.getId());
                            if (ls == null || ls.size() == 0) {
                                //2级目录的权限
                                List<Auth> list3 = menusDao.selectAuthByMenusId(menus2.getId());
                                //二级目录的权限加入 二级menus级对象
                                for (Auth a : list3) {
                                	logger.info("获取根据二级目录查询的权限名称：{} 二级菜单名称：{} 子权限个数total:{} json:{}" ,a.getName(), subVO.getName() , list3.size(),JSONObject.toJSONString(list3));
                                    AuthVO authVO = new AuthVO();
                                    BeanUtils.copyProperties(a, authVO);
                                    RoleAuth ra = authDao.getRoleAuthByRoleAndAuthId(id, a.getId());
                                    if (ra != null) {
                                        authVO.setChecked(true);
                                    } else {
                                        authVO.setChecked(false);
                                    }
                                    subVO.addAuth(authVO);
                                }
                            } else {
                            	 logger.info("获取根据父级查询的三级菜单名 ：  {},个数：{},json:{}" ,menus2.getName() , ls.size(),JSONObject.toJSONString(ls));
                                //二级menus
                                List<MenusVO> subList3 = new ArrayList<>();
                                if (null != ls && ls.size() > 0) {
                                    for (Menus menus3 : ls) {
                                        MenusVO subVO3 = new MenusVO();
                                        BeanUtils.copyProperties(menus3, subVO3);
                                        //3级目录的权限
                                        List<Auth> list4 = menusDao.selectAuthByMenusId(menus3.getId());
                                        //3级目录的权限加入 3级menus级对象
                                        for (Auth a : list4) {
                                        	logger.info("获取根据三级目录查询的权限名称：{} 三级菜单名称：{} 子权限个数total:{} json:{}" ,a.getName(), subVO3.getName() , list4.size(),JSONObject.toJSONString(list4));
                                            AuthVO authVO = new AuthVO();
                                            BeanUtils.copyProperties(a, authVO);
                                            RoleAuth ra = authDao.getRoleAuthByRoleAndAuthId(id, a.getId());
                                            if (ra != null) {
                                                authVO.setChecked(true);
                                            } else {
                                                authVO.setChecked(false);
                                            }
                                            subVO3.addAuth(authVO);
                                        }
                                        if (menusDao.selectMenusMapByRoleAndMenu(menus3.getId(), id) != null) {
                                            subVO3.setChecked(true);
                                        } else {
                                            subVO3.setChecked(false);
                                        }
                                        subList3.add(subVO3);
                                        subVO.setChildren(subList3);
                                    }
                                }
                            }
                            if (menusDao.selectMenusMapByRoleAndMenu(menus2.getId(), id) != null) {
                                subVO.setChecked(true);
                            } else {
                                subVO.setChecked(false);
                            }
                            subList.add(subVO);
                        }
                    }
                    if (menusDao.selectMenusMapByRoleAndMenu(menus.getId(), id) != null) {
                        vo.setChecked(true);
                    } else {
                        vo.setChecked(false);
                    }
                    //把二级目录加入父级对象
                    vo.setChildren(subList);
                    result.add(vo);
                }
            }
            logger.info(JSONObject.toJSONString(result));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException();
        }

    }

    @Override
    public List<Role> selectRoleByStaffId(Integer staffId) {
        List<Role> list = roleDao.selectRoleByStaffId(staffId);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public String distAuthToMenus(Integer authId, List<Integer> menusIds) {
        //删除当前权限所有的关联菜单关系
        menusDao.deleteForMenusAuth("auth_id", Integer.valueOf(authId));
        //添加新的权限菜单关联关系
        for (Integer menuId : menusIds) {
            MenusAuth menusAuth = new MenusAuth();
            menusAuth.setAuthId(authId);
            menusAuth.setMenusId(menuId);
            menusDao.insertInfoForMenusAuth(menusAuth);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void distAuthToRole(Integer authId, List<Integer> rids) {
        try {
            //删除当前权限所有的关联部门（角色）关系
            roleDao.deleteForRoleAuth("auth_id", Integer.valueOf(authId));
            //添加新的权限部门（角色）关联关系
            for (Integer roleId : rids) {
                RoleAuth roleAuth = new RoleAuth();
                roleAuth.setAuthId(authId);
                roleAuth.setRoleId(roleId);
                roleDao.insertInfoForRoleAuth(roleAuth);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.MA00026);
        }

    }


    @Override
    public Page<AuthInfoVo> selectAuth(Map<String, Object> paramMap,
                                       Integer rows, Integer page) {
        String name = (String) paramMap.get("menusName");
        if (StringTools.isNotEmpty(name)) {
            List<Integer> list = menusDao.selectByName(name);
            if(StringTools.isNotEmpty(list)){
            	paramMap.put("menusId", list);
            }
        }
        Page<AuthInfoVo> pg = new Page<AuthInfoVo>();
        pg.setPageNum(page);
        pg.setPageSize(rows);
        paramMap.put("limit1", (page - 1) * rows);
        paramMap.put("limit2", rows);
        logger.info("JSON = {}",JSONObject.toJSONString(paramMap));
        List<AuthInfoVo> list = authDao.selectAuth(paramMap);
        pg.addAll(list);
        pg.setTotal(authDao.selectAuthCount(paramMap));
        return pg;
    }
    
    @Override
    public List<Menus> selectMenusBottom(){
    	 try {
             //父级Menus
             List<Menus> result = new ArrayList<>();
             /*查询所有1级目录*/
             List<Menus> list = menusDao.selectByPid(-1);
             logger.info("获取父级菜单  ：一级菜单个数total:{}",JSONObject.toJSONString(list));
             if (null != list && list.size() > 0) {
                 for (Menus menus : list) {
                     //根据父级查询其二级目录
                     List<Menus> list2 = menusDao.selectByPid(menus.getId());
                     logger.info("获取父级菜单  {}二级菜单个数total:{}",menus.getName(),JSONObject.toJSONString(list2));
                     //二级menus
                     if (null != list2 && list2.size() > 0) {
                         for (Menus menus2 : list2) {
                             //查询三级菜单
                             List<Menus> ls = menusDao.selectByPid(menus2.getId());
                             logger.info("获取父级菜单  {}三级菜单个数total:{}",menus2.getName(),JSONObject.toJSONString(ls));
                             if (ls != null && ls.size() > 0) {
                            	 result.addAll(ls);
                             }else{
                            	 result.add(menus2);
                             }
                         }
                     }
                     
                 }
             }
             logger.info(JSONObject.toJSONString(result));
             return result;
         } catch (Exception e) {
             e.printStackTrace();
             throw new LTException();
         }

    }
    
public static void main(String[] args) {
	List<Integer> list = new ArrayList<Integer>();
	list.add(1);
	list.add(2);
	list.add(3);
	list.add(4);
	list.add(5);
	System.out.println(list.toString().substring(1, list.toString().length()-1));
}
}
