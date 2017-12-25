package com.lt.manager.service.sys;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.sys.StaffIp;

public interface IStaffService {

	/**
	 * 校验是否启用IP限制
	 * @param staff
	 * @param ip
	 * @param code
	 * @return
	 */
	boolean checkIp(Staff staff, String ip);

	/**
	 * 根据登录名称查询员工详情
	 * @param userName
	 * @return
	 */
	Staff selectLoginStaffByUserName(String userName);

	/**
	 * 更新员工状态
	 * @param id
	 * @param status
	 */
	void updateStaffStatus(Integer id, String status);

	/**
	 * 是否通过错误登录次数校验
	 * @param staff
	 */
	boolean checkErrorNum(Staff staff);


	/**
	 * 发送验证邮件
	 * @param email
	 * @param ip
	 */
	void sendRegisterEmailMsg(String email, String ip, Integer staffId);

	/**
	 * 发送验证短信
	 * @param tele
	 * @param ip
	 */
	void sendRegisterMsg(String tele, String ip, Integer staffId);

	/**
	 * 设备验证
	 * @param staff
	 * @param code
	 * @param uuid
	 * @return
	 */
	boolean checkEmailDevice(Staff staff, String code, String uuid);

	/**
	 * 设备验证
	 * @param staff
	 * @param code
	 * @param uuid
	 * @return
	 */
	boolean checkDevice(Staff staff, String code, String uuid);

	/**
	 * 分页查询相关员工列表
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	Page<Staff> pageSelectStaff(Map<String, Object> paramMap, int page,
			int rows);

	/**
	 * 查询员工已有权限
	 * @param parseInt
	 * @return
	 */
	List<Auth> queryStaffAuthByStaffId(int statffId);

	/**
	 * 查询员工详情信息
	 * @param parseInt
	 * @return
	 */
	Staff queryStaffInfoById(int staffId);


	/**
	 * 更新员工权限
	 * @param staffId
	 * @param authIdArray
	 */
	void updateStaffAuth(String staffId, String[] authIdArray);

	Integer queryStaffByLoginName(String loginName, Integer staffId);

	void saveStaffInfo(Staff staff);

	void updateStaffInfo(Staff staff);

	void updateStaffPassword(int userId, String np1);

	List<Staff> getStaffAll();

	void updateIPStatus(int sId, int status);

	void saveSatffIp(String ip, Integer staffId, String desc);

	Page<StaffIp> findStaff(Map<String, Object> paramMap, int rows, int page);

	void deleteStaffIpById(Integer id);

	void updateSatffIp(String ip, Integer staffId, Integer id, String desc);

	void deleteStaffIpByIds(String ids);

	void deleteById(Integer id);

}
