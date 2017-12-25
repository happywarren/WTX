package com.lt.controller.promote.bean;

import java.io.Serializable;
import java.util.Date;

public class CommissionsMain implements Serializable{
	
	private static final long serialVersionUID = 2677618185283941604L;

	private Integer id;
	
	private Integer user_id;
	
	private Double used_commissions;//可用佣金
	
	private Double total_commissions;//累计赚钱佣金,包含内部存入的佣金
	
	private Double cur_draw_commissions;//可转现金的佣金
	
	private Double total_draw_commissions;//累计佣金转现金的额度
	
	private Double total_level_one_commissions;//累计一级佣金
	
	private Double total_level_two_commissions;//累计二级佣金
	
	private Double freeze_commissions;//冻结的佣金
	
	private Date create_date;
	
	public CommissionsMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Double getUsed_commissions() {
		return used_commissions;
	}

	public void setUsed_commissions(Double used_commissions) {
		this.used_commissions = used_commissions;
	}

	public Double getTotal_commissions() {
		return total_commissions;
	}

	public void setTotal_commissions(Double total_commissions) {
		this.total_commissions = total_commissions;
	}

	public Double getCur_draw_commissions() {
		return cur_draw_commissions;
	}

	public void setCur_draw_commissions(Double cur_draw_commissions) {
		this.cur_draw_commissions = cur_draw_commissions;
	}

	public Double getTotal_draw_commissions() {
		return total_draw_commissions;
	}

	public void setTotal_draw_commissions(Double total_draw_commissions) {
		this.total_draw_commissions = total_draw_commissions;
	}

	public Double getTotal_level_one_commissions() {
		return total_level_one_commissions;
	}

	public void setTotal_level_one_commissions(Double total_level_one_commissions) {
		this.total_level_one_commissions = total_level_one_commissions;
	}

	public Double getTotal_level_two_commissions() {
		return total_level_two_commissions;
	}

	public void setTotal_level_two_commissions(Double total_level_two_commissions) {
		this.total_level_two_commissions = total_level_two_commissions;
	}

	public Double getFreeze_commissions() {
		return freeze_commissions;
	}

	public void setFreeze_commissions(Double freeze_commissions) {
		this.freeze_commissions = freeze_commissions;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	
}
