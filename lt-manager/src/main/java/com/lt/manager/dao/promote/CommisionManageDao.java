package com.lt.manager.dao.promote;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.promote.CommisionParamVo;
import com.lt.model.promote.CommisionOptcode;

/**
 * 佣金管理dao
 * @author jingwb
 *
 */
public interface CommisionManageDao {

	/**
	 * 查询佣金存取明细--分页
	 * @param param
	 * @return
	 */
	public List<CommisionParamVo> selectCommisionIoPage(CommisionParamVo param);
	
	/**
	 * 查询佣金存取明细数量
	 * @param param
	 * @return
	 */
	public Integer selectCommisionIoCount(CommisionParamVo param);
	
	/**
	 * 查询佣金流水--分页
	 * @param param
	 * @return
	 */
	public List<CommisionParamVo> selectCommisionFlowPage(CommisionParamVo param);
	
	/**
	 * 查询佣金流水数量
	 * @param param
	 * @return
	 */
	public Integer selectCommisionFlowCount(CommisionParamVo param);
	
	/**
	 * 查询佣金流水汇总数据
	 * @param param
	 * @return
	 */
	public Map<String,Object> selectCommisionFlowData(CommisionParamVo param);
	
	/**
	 * 查询佣金业务码
	 * @param code
	 * @return
	 */
	public List<CommisionOptcode> getCommisionOptcodes(CommisionOptcode code);
}
