package com.lt.model.product;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品时间配置表
 * 下列参数时间格式如：12:00:00
 * 			
 * @author jingwb
 *
 */
public class ProductTimeConfig implements Serializable{
	private String timeId;//主键由系统生成并非数据库自增
	private Integer productId;//商品id
	
	private String quotaBeginTime;//行情开始时间
	private String quotaEndTime;//行情结束时间
	private String tradeBeginTime;//交易开始时间
	private String tradeEndTime;//交易结束时间
	private String sysSaleBeginTime;//系统清仓开始时间
	private String sysSaleEndTime;//系统清仓结束时间
	private String sysSaleTime;//系统清仓时间
	private String deferBalanceTime;//延期结算时间
	private String nextTimeId;//下一个时段ID
	private String prevTimeId;//上一个时段ID
	private Date createDate;//
	private Integer createUser;//

	
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getQuotaBeginTime() {
		return quotaBeginTime;
	}
	public void setQuotaBeginTime(String quotaBeginTime) {
		this.quotaBeginTime = quotaBeginTime;
	}
	public String getQuotaEndTime() {
		return quotaEndTime;
	}
	public void setQuotaEndTime(String quotaEndTime) {
		this.quotaEndTime = quotaEndTime;
	}
	public String getTradeBeginTime() {
		return tradeBeginTime;
	}
	public void setTradeBeginTime(String tradeBeginTime) {
		this.tradeBeginTime = tradeBeginTime;
	}
	public String getTradeEndTime() {
		return tradeEndTime;
	}
	public void setTradeEndTime(String tradeEndTime) {
		this.tradeEndTime = tradeEndTime;
	}
	public String getSysSaleBeginTime() {
		return sysSaleBeginTime;
	}
	public void setSysSaleBeginTime(String sysSaleBeginTime) {
		this.sysSaleBeginTime = sysSaleBeginTime;
	}
	public String getSysSaleEndTime() {
		return sysSaleEndTime;
	}
	public void setSysSaleEndTime(String sysSaleEndTime) {
		this.sysSaleEndTime = sysSaleEndTime;
	}
	public String getSysSaleTime() {
		return sysSaleTime;
	}
	public void setSysSaleTime(String sysSaleTime) {
		this.sysSaleTime = sysSaleTime;
	}
	public String getDeferBalanceTime() {
		return deferBalanceTime;
	}
	public void setDeferBalanceTime(String deferBalanceTime) {
		this.deferBalanceTime = deferBalanceTime;
	}
	
	public String getNextTimeId() {
		return nextTimeId;
	}
	public void setNextTimeId(String nextTimeId) {
		this.nextTimeId = nextTimeId;
	}
	public String getPrevTimeId() {
		return prevTimeId;
	}
	public void setPrevTimeId(String prevTimeId) {
		this.prevTimeId = prevTimeId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
}
