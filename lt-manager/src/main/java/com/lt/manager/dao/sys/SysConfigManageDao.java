package com.lt.manager.dao.sys;

import java.util.List;

import com.lt.model.sys.SysConfig;

/**
 * 系统配置dao
 * @author jingwb
 *
 */
public interface SysConfigManageDao {

	/**
	 * 查询系统配置信息
	 * @return
	 */
	public List<SysConfig> selectSysConfigs();
	
	/**
	 * 根据配置代码查询配置信息
	 * @param sysCode
	 * @return
	 * @author yubei
	 */
	public	SysConfig selectSysConfigByCode(String cfgCode);
	
	
	/**
	 * 更新系统配置信息
	 * @param sysConfig
	 * @return
	 * @author yubei
	 */
	public int updateSysConfig(SysConfig sysConfig);
	
	/**
	 * 模糊查询
	 * @param cfgCodes
	 * @return
	 */
	public List<SysConfig> fuzzySelectSysConfig(List<String> cfgCodes);
	
	/**
	 * 批量更新
	 * @param sysConfigs
	 * @return
	 */
	public int updateSysConfigList(List<SysConfig> sysConfigs);
}
