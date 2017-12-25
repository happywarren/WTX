package com.lt.business.core.service.sys.lmpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.sys.IThreadLockService;
import com.lt.business.core.dao.sys.ISysThreadLockDao;


/**
 * 系统线程锁业务处理
 * @author jingwb
 *
 */
@Service
public class ThreadLockServiceImpl implements IThreadLockService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private ISysThreadLockDao sysThreadLockDao;
	

	@Override
	
	public boolean lock(String code) {
		Integer i = sysThreadLockDao.updatesSysThreadLocked(code);
		if(i == 1){
			return true;//修改成功，代表未被锁住
		}
		return false;//修改失败表示，表已被锁住
	}
	
	@Override
	public boolean unlock(String code) {
		int i = sysThreadLockDao.updatesSysThreadUnLocked(code);
		if(i == 1){
			return true;
		}
		return false;
	}

}
