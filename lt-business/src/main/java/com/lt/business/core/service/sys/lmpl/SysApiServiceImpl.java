package com.lt.business.core.service.sys.lmpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.sys.ISysApiService;
import com.lt.business.core.service.sys.ISysNoticeService;
import com.lt.model.sys.SysNotice;

@Service
public class SysApiServiceImpl implements ISysApiService{

	private static final long serialVersionUID = -4205358613831199689L;
	@Autowired
	private ISysNoticeService sysNoticeServiceImpl;
	
	@Override
	public List<SysNotice> getSysNoticeList() {
		return sysNoticeServiceImpl.getSysNoticeList();
	}

}
