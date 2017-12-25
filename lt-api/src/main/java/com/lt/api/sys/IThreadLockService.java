package com.lt.api.sys;

import java.io.Serializable;

/**
 * 系统线程锁业务处理
 * 
 * @author jingwb
 *
 */
public interface IThreadLockService extends Serializable {

	/**
	 * 锁
	 * 
	 * @return
	 */
	public boolean lock(String code);

	/**
	 * 解锁
	 * 
	 * @return
	 */
	public boolean unlock(String code);
}
