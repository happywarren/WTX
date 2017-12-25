package com.lt.controller.promote.bean;

import java.util.Date;

/**
 * 下线统计数据汇总实体
 * @author jingwb
 *
 */
public class StatisticBrancherSummaryLogOld{
	
	private String user_id;
	private String promoter_user_id;//上线userId
	/**充值金额*/
	private Double recharge_amount;
	/**交易手数*/
	private Integer hand_count;
	/**交易金额*/
	private Double trade_amount;
	/***/
	private Date create_time;
	
	private Date modify_time;
	private Integer flag;//是否存在 1是  0否

	

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPromoter_user_id() {
		return promoter_user_id;
	}

	public void setPromoter_user_id(String promoter_user_id) {
		this.promoter_user_id = promoter_user_id;
	}

	public Double getRecharge_amount() {
		return recharge_amount;
	}

	public void setRecharge_amount(Double recharge_amount) {
		this.recharge_amount = recharge_amount;
	}

	public Integer getHand_count() {
		return hand_count;
	}

	public void setHand_count(Integer hand_count) {
		this.hand_count = hand_count;
	}

	public Double getTrade_amount() {
		return trade_amount;
	}

	public void setTrade_amount(Double trade_amount) {
		this.trade_amount = trade_amount;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}
