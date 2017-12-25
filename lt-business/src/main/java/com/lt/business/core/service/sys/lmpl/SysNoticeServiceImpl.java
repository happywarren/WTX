package com.lt.business.core.service.sys.lmpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.business.core.dao.sys.ISysNoticeDao;
import com.lt.business.core.service.sys.ISysNoticeService;
import com.lt.model.sys.SysNotice;

/**
 *  系统公告service
 * @author jingwb
 *
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService{

	@Autowired
	private ISysNoticeDao sysNoticeDao;
	
	@Override
	public List<SysNotice> getSysNoticeList() {
		return sysNoticeDao.selectSysNoticeList();
	}

}
