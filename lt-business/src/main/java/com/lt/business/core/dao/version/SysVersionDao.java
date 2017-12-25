package com.lt.business.core.dao.version;

import com.lt.vo.news.SystemConfigVO;

/**   
* 项目名称：lt-business   
* 类名称：SysVersionDao   
* 类描述： 系统版本处理类  
* 创建人：yuanxin   
* 创建时间：2017年3月21日 下午3:37:30      
*/
public interface SysVersionDao {
	
	/**
	 * 
	 * @return    查询安卓系统配置bean
	 * @return:       SystemConfigVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月21日 下午3:41:08
	 */
	public SystemConfigVO getVersion();
	
	/**
	 * 查询IOS系统版本
	 * @return    
	 * @return:       SystemConfigVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月21日 下午3:41:27
	 */
	public SystemConfigVO getVersionIOS();
}
