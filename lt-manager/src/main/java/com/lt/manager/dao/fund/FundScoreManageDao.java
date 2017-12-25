package com.lt.manager.dao.fund;

import java.util.List;

import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;

public interface FundScoreManageDao {

	/**
	 * 积分流水明细列表fund_flow_cash
	 */
	public List<FundFlowVO> selectFundFlowList(FundFlowVO fundFlow);
	
	/**
	 * 积分流水明细列表数量fund_flow_cash
	 */
	public Integer selectFundFlowListCount(FundFlowVO fundFlow);
	
	/**
	 * 查询所有条件数据用作汇总
	 * @param fundFlow
	 * @return    
	 * @return:       List<FundFlowVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 上午10:21:58
	 */
	public List<FundFlowVO> selectFundFlowTotalAmt(FundFlowVO fundFlow);
	
	/**
	 * 获取内部存取信息(分页)
	 * @return    
	 * @return:       FundMainCashScore    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:58:39
	 */
	public List<FundIoCashInnerVO> qryFundInnerInOutPageDetail(FundIoCashInnerVO cashScore);
	
	/**
	 * 获取内部存取信息
	 * @return    
	 * @return:       FundMainCashScore    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:58:39
	 */
	public List<FundIoCashInnerVO> qryFundInnerInOutDetailList(FundIoCashInnerVO cashScore);
	
	/**
	 * 获取内部存取数量
	 * @param cashScore
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午6:01:50
	 */
	public Integer qryFundInnerInOutDetailCount(FundIoCashInnerVO cashScore);
	
}
