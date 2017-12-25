package com.lt.vo.news;

import java.io.Serializable;

/**   
* 项目名称：lt-business   
* 类名称：SystemConfigVO   
* 类描述：  系统版本bean 
* 创建人：yuanxin   
* 创建时间：2017年3月21日 下午3:40:35      
*/
public class SystemConfigVO implements Serializable{

	private String minVersion;
	private String upVersion;
	private String url;
	private String updateMsg;

	
	public String getUpdateMsg() {
		return updateMsg;
	}

	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMinVersion() {
		return minVersion;
	}

	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}

	public String getUpVersion() {
		return upVersion;
	}

	public void setUpVersion(String upVersion) {
		this.upVersion = upVersion;
	}

}
