package com.lt.promote.dao.promote;

import java.util.List;

import com.lt.model.promote.CommisionOptcode;

/**
 * 佣金流转方式dao
 * @author jingwb
 *
 */
public interface ICommisionOptcodeDao {

	/**
	 * 查询佣金操作码
	 * @param commisionOptcode
	 * @return
	 */
	public List<CommisionOptcode> selectCommisionOptcode(CommisionOptcode commisionOptcode);
}
