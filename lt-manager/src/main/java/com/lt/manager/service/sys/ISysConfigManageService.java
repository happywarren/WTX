package com.lt.manager.service.sys;

import java.util.List;

import com.lt.model.sys.SysConfig;
import com.lt.util.error.LTException;

/**
 * 系统配置业务接口
 * @author jingwb
 *
 */
public interface ISysConfigManageService {

	/**
	 * 加载系统配置到缓存中
	 * @throws Exception
	 */
	public void loadSysCfgToRedis() throws Exception;
	
	
	/**
	 * @param sysCode
	 * @author yubei
	 * 根据配置代码查询配置信息
	 */
	public SysConfig querySysConfigByCode(String sysCode) throws LTException;;
	
	/**
	 * 更新配置信息
	 * @param sysConfig
	 * @return
	 * @throws LTException
	 * @author yubei
	 */
	public void updateSysConfig(SysConfig sysConfig) throws LTException;
	
	
	/**
	 * 模糊查询
	 * @param cfgCodes
	 * @return
	 */
	public List<SysConfig> fuzzyQuerySysConfigByCfgCode(List<String> cfgCodes)throws LTException;
	
	
	/**
	 * 批量更新
	 * @param sysConfigs
	 * @throws LTException
	 */
	public void updateSysConfigList(List<SysConfig> sysConfigs) throws LTException;
	
	public void modifyWinterSummerChange(String value);
	
}
