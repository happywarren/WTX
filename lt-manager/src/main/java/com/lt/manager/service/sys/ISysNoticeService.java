package com.lt.manager.service.sys;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.SysNoticeParam;
import com.lt.model.sys.SysNotice;

/**
 * 系统公告接口
 * @author jingwb
 *
 */
public interface ISysNoticeService {

	/**
	 * 查询系统公告--分页
	 * @param param
	 * @return
	 */
	public Page<SysNotice> querySysNoticePage(SysNoticeParam param);
	
	/***
	 * 新增系统公告
	 * @param param
	 * @return
	 */
	public void addSysNotice(SysNoticeParam param);
	
	/**
	 * 删除公告
	 * @param param
	 */
	public void removeSysNotice(SysNoticeParam param);
	
	/**
	 * 编辑公告
	 * @param param
	 */
	public void editSysNotice(SysNoticeParam param);
	
	/**
	 * 查看
	 * @param param
	 */
	public SysNotice viewSysNotice(SysNoticeParam param);
}
