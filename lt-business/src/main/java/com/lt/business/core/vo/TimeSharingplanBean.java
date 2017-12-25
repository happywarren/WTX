package com.lt.business.core.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * 分时图对象
 * @author guodw
 *
 */
public class TimeSharingplanBean {

	/**
	 * 时间  年月日时分00
	 */
	private String timeStamp ;
	/**
	 * 当前价
	 */
	private String lastPrice;
	/**
	 * 成交量
	 */
	private Integer volume;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	public TimeSharingplanBean() {
		
	}
	
	public TimeSharingplanBean(String timeStamp, String lastPrice, Integer volume) {
		this.timeStamp = timeStamp;
		this.lastPrice = lastPrice;
		this.volume = volume;
	}
	public String toJSONString(){
		return JSONObject.toJSONString(this);
	}
	
	public TimeSharingplanBean parseObject(String json){
		return JSONObject.parseObject(json, TimeSharingplanBean.class);
	}
	
}
