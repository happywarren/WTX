package com.lt.manager.service.fund;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;
import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.manager.bean.fund.FundMainCashScore;
import com.lt.manager.bean.fund.FundTransferDetailById;
import com.lt.manager.bean.fund.FundTransferDetailVO;
import com.lt.model.fund.FundIoCashInner;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.BankInfo;
import com.lt.util.error.LTException;

public interface IFundCashManageService {

	/**
	 * 现金流水明细列表fund_flow_cash
	 */
	Page<FundFlowVO> selectFundFlowPageList(FundFlowVO fundFlow);
	
	/**
	 * 返回现金流汇总金额字段
	 * @param fundFlow
	 * @return    
	 * @return:       Map<String,Double>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 上午9:46:42
	 */
	Map<String,Double> selectFundCashFlowList(FundFlowVO fundFlow);
	
	/**
	 * 统计数据
	 * @param fundFlow
	 * @return
	 */
	Map<String,Double> selectFundCashFlowMap(FundFlowVO fundFlow);
	
	/**
	 * 充值明细列表 fund_io_cash_recharge
	 */
	Page<FundIoCashRechargeVO> selectFundIoCashRechargePageList(FundIoCashRechargeVO cashRecharge);
	
	/**
	 * 查询所有的充值记录
	 * @param cashRecharge
	 * @return    
	 * @return:       Map<String,Double>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 上午10:39:06
	 */
	Map<String,Double> selectFundIoCashRechargeList(FundIoCashRechargeVO cashRecharge);
	
	/**
	 * 充值明细记录查询（补单使用）
	 * @param id
	 * @return
	 * @throws LTException    
	 * @return:       FundIoCashRechargeVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 下午7:11:45
	 */
	FundIoCashRechargeVO qryRechargeRepair(String id,Integer status) throws LTException;

	/**
	 * 提现转账明细列表
	 */
	Page<FundIoCashWithdrawalVO> selectFundIoCashWithdrawaPagelList(FundIoCashWithdrawalVO  fundIoCashWithdrawal);
	
	/**
	 * 提现转账明细列表
	 */
	Map<String,Double> selectFundIoCashWithdrawalList(FundIoCashWithdrawalVO  fundIoCashWithdrawal);
	
	/**
	 * 查询详细提现记录
	 * @param ioId 现金流id
	 * @return    
	 * @return:       FundIoCashWithdrawalVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月11日 下午5:44:28
	 */
	FundIoCashWithdrawalVO selectFundIoCashWithdrawalDetail(String ioId);
	
	/**
	 * 转账明细（分页）
	 */
	Page<FundTransferDetailVO> selectFundTransferPageDetail(FundTransferDetailVO detailVO);
	
	/**
	 * 转账明细
	 */
	Map<String,Double> selectFundTransferDetailList(FundTransferDetailVO detailVO);
	
	/**
	 * 返回用户转账明细信息
	 * @return    
	 * @return:       FundTransferDetailVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:00:31
	 */
	FundTransferDetailById qryFundTransferDetail(String id) throws LTException;
	
	/**
	 * 返回用户主账户情况（内部存取使用）
	 * @param fundMainCashScore
	 * @return    
	 * @return:       Page<FundMainCashScore>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:43:29
	 */
	Page<FundMainCashScore> qryUserMainCashScore(FundMainCashScore fundMainCashScore);
	
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
	
	/**
	 * 返回银行信息
	 * @return
	 * @throws LTException    
	 * @return:       List<BankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月18日 下午4:14:18
	 */
	List<BankInfo> findBankInfo() throws LTException;
	
	/**
	 * 返回现金流配置信息
	 * @return
	 * @throws LTException    
	 * @return:       List<BankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月18日 下午4:14:18
	 */
	List<FundOptCode> getFundOptCode() throws LTException;
	
	/**
	 * 返回一级验证码
	 * @return
	 * @throws LTException    
	 * @return:       List<FundOptCode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月21日 下午2:15:42
	 */
	List<FundOptCode> getFirstOptCode(Integer fundType) throws LTException;
	
	/**
	 * 通过一级验证码查询二级验证码
	 * @param fundType
	 * @param firstOptCode
	 * @return
	 * @throws LTException    
	 * @return:       List<FundOptCode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月21日 下午3:19:19
	 */
	List<FundOptCode> getSecondOptCode(Integer fundType,Integer firstOptCode) throws LTException;
	
	/**
	 * 根据
	 * @param optList
	 * @return
	 * @throws LTException    
	 * @return:       List<FundOptCode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月2日 下午5:15:49
	 */
	List<FundOptCode> getOptCodeByFirst(List<String> firstOptCode) throws LTException;
}
