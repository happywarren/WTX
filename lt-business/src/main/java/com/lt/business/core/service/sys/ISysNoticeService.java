package com.lt.business.core.service.sys;

import java.util.List;

import com.lt.model.sys.SysNotice;

/**
 * 系统公告service
 * @author jingwb
 *
 */
public interface ISysNoticeService {

	/**
	 * 查询系统公告--两条数据
	 * @return
	 */
	public List<SysNotice> getSysNoticeList();
}
