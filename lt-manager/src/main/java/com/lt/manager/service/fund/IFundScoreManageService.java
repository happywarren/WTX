package com.lt.manager.service.fund;

import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;
import com.lt.model.fund.FundIoCashInner;
import com.lt.util.error.LTException;

public interface IFundScoreManageService {
	/**
	 * 积分流水fund_flow_cash
	 * @param fundFlow
	 * @return
	 */
	public Page<FundFlowVO> selectFundFlowPageList(FundFlowVO fundFlow) ;
	
	/**
	 * 查询积分汇总金额
	 * @param fundFlow
	 * @return    
	 * @return:       Page<FundFlowVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 上午10:19:53
	 */
	public Map<String,Double> selectFundFlowTotalAmt(FundFlowVO fundFlow) ;
	
	/**
	 * 
	 * @param fundMainCashScore
	 * @return    
	 * @return:       Page<FundMainCashScore>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午8:17:25
	 */
	Page<FundIoCashInnerVO> qryFundInnerInOutPageDetail(FundIoCashInnerVO fundIoCashInnerVO);
	
	/**
	 * 
	 * @param fundMainCashScore
	 * @return    
	 * @return:       Page<FundMainCashScore>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午8:17:25
	 */
	Map<String,Double> qryFundInnerInOutDetailAmt(FundIoCashInnerVO fundIoCashInnerVO);
	
	/**
	 * 新增内部存入单
	 * @param cashInner 
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月11日 上午9:07:11
	 */
	void addFundIoInnerOut(FundIoCashInner cashInner,String remark) throws LTException;
}
