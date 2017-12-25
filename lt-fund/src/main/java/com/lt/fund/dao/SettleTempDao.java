package com.lt.fund.dao;

import java.util.List;

import com.lt.model.settle.SettleTmpBean;

public interface SettleTempDao {

	
	public void delete(Integer id);
	
	public List<SettleTmpBean> findAll();

	public int updateOrderStatus(String displayId);
}
