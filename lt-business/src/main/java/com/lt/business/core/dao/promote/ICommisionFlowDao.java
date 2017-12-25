package com.lt.business.core.dao.promote;

import java.util.List;

import com.lt.model.promote.CommisionFlow;

/**
 * 佣金流水dao
 * @author jingwb
 *
 */
public interface ICommisionFlowDao {

	/**
	 * 批量插入佣金流水
	 * @param list
	 */
	public void insertCommisionFlows(List<CommisionFlow> list);
	
	/**
	 * 插入佣金流水
	 * @param flow
	 */
	public void insertCommisionFlow(CommisionFlow flow);
}
