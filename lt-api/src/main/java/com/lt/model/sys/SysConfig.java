package com.lt.model.sys;

import java.util.Date;

/**
 * 系统配置实体
 * @author jingwb
 *
 */
public class SysConfig implements java.io.Serializable{
	private Integer id;
	private String cfgCode;//配置项编码
	private String cfgValue;//配置项值
	private Integer cfgType;//配置项类型 1.订单 2.资金 3.商品 4.用户 5 行情 6.风控 7.定时任务 
	private String releaseName;//配置项解释
	private Integer isuse;//可用状态0不可用 1可用
	private Date createDate;//创建时间
	private Integer creater;//创建人
	
	
	
	public SysConfig() {
		super();
	}
	public SysConfig(String cfgCode, String cfgValue) {
		super();
		this.cfgCode = cfgCode;
		this.cfgValue = cfgValue;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCfgCode() {
		return cfgCode;
	}
	public void setCfgCode(String cfgCode) {
		this.cfgCode = cfgCode;
	}
	public String getCfgValue() {
		return cfgValue;
	}
	public void setCfgValue(String cfgValue) {
		this.cfgValue = cfgValue;
	}
	public Integer getCfgType() {
		return cfgType;
	}
	public void setCfgType(Integer cfgType) {
		this.cfgType = cfgType;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public Integer getIsuse() {
		return isuse;
	}
	public void setIsuse(Integer isuse) {
		this.isuse = isuse;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}
	
}
