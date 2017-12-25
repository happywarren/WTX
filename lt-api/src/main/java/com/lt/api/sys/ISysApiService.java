package com.lt.api.sys;

import java.io.Serializable;
import java.util.List;

import com.lt.model.sys.SysNotice;

public interface ISysApiService extends Serializable{

	/**
	 * 查询系统公告--返回两条
	 * @return
	 */
	public List<SysNotice> getSysNoticeList();
}
