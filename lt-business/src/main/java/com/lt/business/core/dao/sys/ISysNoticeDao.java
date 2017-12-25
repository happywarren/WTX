package com.lt.business.core.dao.sys;

import java.util.List;

import com.lt.model.sys.SysNotice;
/**
 * 系统公告dao
 * @author jingwb
 *
 */
public interface ISysNoticeDao {

	public List<SysNotice> selectSysNoticeList();
}
