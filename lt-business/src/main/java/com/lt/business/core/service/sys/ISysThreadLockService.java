package com.lt.business.core.service.sys;
/**
 * 系统线程锁业务处理
 * @author jingwb
 *
 */
public interface ISysThreadLockService {

	/**
	 * 锁
	 * @return
	 */
	public boolean lock(String code);
	
	/**
	 * 解锁
	 * @return
	 */
	public boolean unlock(String code);
}
