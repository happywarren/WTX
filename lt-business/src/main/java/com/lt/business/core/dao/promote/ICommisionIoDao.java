package com.lt.business.core.dao.promote;

import com.lt.model.promote.CommisionIo;

/**
 * 佣金存取明细dao
 * @author jingwb
 *
 */
public interface ICommisionIoDao {

	/**
	 * 新增佣金存取明细
	 * @param io
	 */
	public void insertCommisionIo(CommisionIo io);
	
	/**
	 * 查询佣金存取明细--通过id
	 * @param id
	 * @return
	 */
	public CommisionIo selectCommisionIoById(String id);
	
	/**
	 * 通过id更新佣金存取明细
	 * @param io
	 */
	public void updateCommisionIoById(CommisionIo io);
}
