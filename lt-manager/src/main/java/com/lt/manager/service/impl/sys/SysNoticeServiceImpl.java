package com.lt.manager.service.impl.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.SysNoticeParam;
import com.lt.manager.dao.sys.SysNoticeDao;
import com.lt.manager.service.sys.ISysNoticeService;
import com.lt.model.sys.SysNotice;

@Service
public class SysNoticeServiceImpl implements ISysNoticeService{

	@Autowired
	private SysNoticeDao sysNoticeDao;
	
	@Override
	public Page<SysNotice> querySysNoticePage(SysNoticeParam param) {
		Page<SysNotice> page = new Page<SysNotice>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		List<SysNotice> list = sysNoticeDao.selectSysNoticePage(param);
		page.setTotal(sysNoticeDao.selectSysNoticeCount(param));
		page.addAll(list);
		return page;
	}

	@Override
	public void addSysNotice(SysNoticeParam param) {
		sysNoticeDao.insertSysNotice(param);
	}

	@Override
	public void removeSysNotice(SysNoticeParam param) {
		sysNoticeDao.deleteSysNotice(param);
	}

	@Override
	public void editSysNotice(SysNoticeParam param) {
		sysNoticeDao.updateSysNotice(param);
	}

	@Override
	public SysNotice viewSysNotice(SysNoticeParam param) {
		return sysNoticeDao.selectSysNoticeOne(param);
	}

}
