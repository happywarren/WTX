package com.lt.model.statistic;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;

/**
 * 下线统计数据汇总实体
 * @author jingwb
 *
 */
public class StatisticBrancherSummaryLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -754071795074959155L;
	/***/
	private Integer id;
	/***/
	private String userId;
	private String promoterUserId;//上线userId
	/**充值金额*/
	private Double rechargeAmount;
	/**交易手数*/
	private Integer handCount;
	/**交易金额*/
	private Double tradeAmount;
	/***/
	private Date createTime;
	
	private Date modifyTime;
	private Integer flag;//是否存在 1是  0否

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = DoubleUtils.scaleFormatEnd(rechargeAmount,2);
	}

	public Integer getHandCount() {
		return handCount;
	}

	public void setHandCount(Integer handCount) {
		this.handCount = handCount;
	}

	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = DoubleUtils.scaleFormatEnd(tradeAmount,2);
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPromoterUserId() {
		return promoterUserId;
	}

	public void setPromoterUserId(String promoterUserId) {
		this.promoterUserId = promoterUserId;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}
