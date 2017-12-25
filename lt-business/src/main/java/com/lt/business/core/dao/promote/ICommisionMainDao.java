package com.lt.business.core.dao.promote;

import java.util.List;

import com.lt.model.promote.CommisionMain;

/**
 * 佣金dao
 * @author jingwb
 *
 */
public interface ICommisionMainDao {

	/**
	 * 初始化佣金账户
	 * @param userId
	 */
	public void initCommisionMain(String userId);
	
	/**
	 * 批量更新佣金账户
	 * @param list
	 */
	public void updateCommisionMains(List<CommisionMain> list);
	
	/**
	 * 查询佣金主表
	 * @param userId
	 * @return
	 */
	public CommisionMain selectCommisionMain(String userId);
	
	/**
	 * 更新佣金账户
	 * @param commisionMain
	 */
	public void updateCommisionMain(CommisionMain commisionMain);
}
