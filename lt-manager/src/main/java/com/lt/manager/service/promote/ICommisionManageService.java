package com.lt.manager.service.promote;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.promote.CommisionParamVo;
import com.lt.model.promote.CommisionOptcode;

/**
 * 佣金管理接口
 * @author jingwb
 *
 */
public interface ICommisionManageService {

	/**
	 * 查询佣金存取明细
	 * @param param
	 * @return
	 */
	public Page<CommisionParamVo> queryCommisionIoPage(CommisionParamVo param);
	
	/**
	 * 查询佣金流水--分页
	 * @param param
	 * @return
	 */
	public Page<CommisionParamVo> queryCommisionFlowPage(CommisionParamVo param);
	
	/**
	 * 查询佣金流水汇总数据
	 * @param param
	 * @return
	 */
	public Map<String,Object> getCommisionFlowData(CommisionParamVo param);
	
	/**
	 * 获取推广一级目录
	 * @return
	 */
	public List<CommisionOptcode> getFirstOptcode();
	
	/**
	 * 获取二级目录
	 * @param code
	 * @return
	 */
	public List<CommisionOptcode> getSecondOptcode(CommisionOptcode code);
}
