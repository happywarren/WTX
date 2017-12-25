package com.lt.manager.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.sys.StaffIp;

public interface StaffDao {
	public List<Staff> pageSelectStaff(Map<String,Object> paramMap);
	
	public int pageSelectStaffCount(Map<String,Object> paramMap);

	public List<Auth> queryStaffAuthByStaffId(Integer id);

	public int saveStaffInfo(Staff staff);
	
	public void updateStaffInfo(Staff staff);

	public Integer queryStaffByLoginName(@Param("loginName")String loginName, @Param("staffId")Integer staffId);
	
	public Integer queryStaffByEmail(@Param("email")String email, @Param("staffId")Integer staffId);
	
	public Integer queryStaffByTele(@Param("tele")String tele, @Param("staffId")Integer staffId);
	
    /**
     * 查询登陆用户信息
     */
    Staff selectLoginStaff(String userName, String password);
	
    Staff queryStaffInfoById(Integer staffId);
    
    void updateStaffPassword(@Param("staffId")Integer staffId, @Param("pwd")String pwd);
    
    void updateStaffStatus(@Param("staffId")Integer staffId, @Param("status")String status);

    public Staff selectLoginStaffByUserName(String userName);
	
	public List<Staff> selectStaffByUserName(String userName);

//	public void updateStaffIps(String ips);

	/**
	 * 
	 *	
	 * 描述:获取用户列表 id与用户名
	 *
	 * @author  郭达望
	 * @created 2015年11月11日 下午2:02:36
	 * @since   v1.0.0 
	 * @param id
	 * @return
	 * @return  List<Staff>
	 */
	public List<Staff> getStaffAll();
	

	public void updateStaffClickNum(@Param("staffId")Integer staffId, @Param("clickNum")Integer c);
	
	public void updateDeviceStatus(@Param("staffId")Integer sId, @Param("status")String status);

	/**
	 * 
	 *	
	 * 描述:删除用户
	 *
	 * @author  郭达望
	 * @created 2016年3月31日 下午4:02:05
	 * @since   v1.0.0 
	 * @param id
	 * @return  void
	 */
	public void deleteById(Integer id);
	
	public String selectUserId(Integer id);

	/**
	 * 查询白名单中IP个数
	 * @param ip
	 * @return
	 */
	public int findCountByIp(String ip);

	/**
	 * 更新员工ip限制状态
	 * @param sId
	 * @param status
	 */
	public void updateIPStatus(@Param("sId")int sId, @Param("status") int status);

	
	public void saveStaffIp(StaffIp staffIp);
	
	public List<StaffIp> findStaffIpById(Map<String,Object> map);
	public int findStaffIpCount(Map<String,Object> map);
	
	
	public void deleteStaffIpById(Integer id);
	
	public void updateStaffIp(StaffIp staffIp);
}
