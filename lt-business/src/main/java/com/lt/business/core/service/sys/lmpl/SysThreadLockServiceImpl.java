package com.lt.business.core.service.sys.lmpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.business.core.dao.sys.ISysThreadLockDao;
import com.lt.business.core.service.sys.ISysThreadLockService;


/**
 * 系统线程锁业务处理
 * @author jingwb
 *
 */
@Service
public class SysThreadLockServiceImpl implements ISysThreadLockService{

	@Autowired
	private ISysThreadLockDao sysThreadLockDao;
	
	/**
	 * 废弃掉，sys_thread_lock这张表在发布之前手动插入要的code，提高性能
	 */
	/*@Override
	@Transactional
	public boolean lock(String code) {
		Integer i = sysThreadLockDao.selectSysThreadLock(code);
		if(i == 0){
			SysThreadLock sysThreadLock = null;
			SysThreadLockEnum[] lock = SysThreadLockEnum.values();
			for(SysThreadLockEnum sys : lock){
				if(sys.getCode().equals(code)){
					sysThreadLock = new SysThreadLock(sys.getName(),code);
					break;
				}
			}
			if(sysThreadLock == null){
				throw new LTException("线程锁业务"+code+"未配置");
			}
			
			i = sysThreadLockDao.insertSysThreadLock(sysThreadLock);
		}else{
			i = sysThreadLockDao.updatesSysThreadLocked(code);
		}
		
		if(i == 1){
			return true;
		}
		return false;
	}*/

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
