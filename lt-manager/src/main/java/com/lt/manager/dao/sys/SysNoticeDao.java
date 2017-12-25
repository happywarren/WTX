package com.lt.manager.dao.sys;

import java.util.List;

import com.lt.manager.bean.sys.SysNoticeParam;
import com.lt.model.sys.SysNotice;

/**
 * 系统公告dao
 * @author jingwb
 *
 */
public interface SysNoticeDao {

	/**
	 * 查询系统公告--分页
	 * @param param
	 * @return
	 */
	public List<SysNotice> selectSysNoticePage(SysNoticeParam param);
	
	/***
	 * 查询系统公告--数量
	 * @param param
	 * @return
	 */
	public Integer selectSysNoticeCount(SysNoticeParam param);
	
	/**
	 * 新增系统公告
	 * @param param
	 */
	public void insertSysNotice(SysNoticeParam param);
	
	/**
	 * 删除公告
	 * @param param
	 */
	public void  deleteSysNotice(SysNoticeParam param);
	
	/**
	 * 修改公告
	 * @param param
	 */
	public void updateSysNotice(SysNoticeParam param);
	
	/**
	 * 查询一条
	 * @param param
	 * @return
	 */
	public SysNotice selectSysNoticeOne(SysNoticeParam param);
	
}
