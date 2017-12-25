package com.lt.manager.dao.sys;

import com.lt.model.sys.SysThreadLock;

public interface SysThreadLockDao {

	/**
	 * 查询该任务是否存在
	 * @param code
	 * @return
	 */
	public Integer selectSysThreadLock(String code);
	
	/**
	 * 查询任务信息
	 * @param sysThreadLock
	 */
	public int insertSysThreadLock(SysThreadLock sysThreadLock);
	
	/**
	 * 将该任务锁住
	 * @param code
	 * @return
	 */
	public int updatesSysThreadLocked(String code);
	
	/**
	 * 将该任务解锁
	 * @param code
	 * @return
	 */
	public int updatesSysThreadUnLocked(String code);
}
