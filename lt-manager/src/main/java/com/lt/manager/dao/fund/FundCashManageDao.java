package com.lt.manager.dao.fund;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;
import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.manager.bean.fund.FundMainCashScore;
import com.lt.manager.bean.fund.FundTransferDetailById;
import com.lt.manager.bean.fund.FundTransferDetailVO;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.BankInfo;

public interface FundCashManageDao {
	/**
	 * 现金流水明细列表fund_flow_cash
	 */
	public List<FundFlowVO> selectFundFlowPageList(FundFlowVO fundFlow);	
	
	/**
	 * 现金流水明细列表fund_flow_cash
	 */
	public List<FundFlowVO> selectFundFlowList(FundFlowVO fundFlow);
	
	/***
	 * 统计资金流水信息
	 * @param fundFlow
	 * @return
	 */
	public Map<String,Object> selectfundFlowMap(FundFlowVO fundFlow);
	
	/**
	 * 返回现金流水列表的数量
	 * @param fundFlow
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月30日 上午11:16:53
	 */
	public Integer selectFundFlowListCount(FundFlowVO fundFlow);	
	
	/**
	 * 查询充值明细数据
	 * @param fundIoCashRechargeVO
	 * @return       List<FundIoCashRechargeVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月4日 上午10:14:55
	 */
	public List<FundIoCashRechargeVO> selectFundChargeFlowPage(FundIoCashRechargeVO fundIoCashRechargeVO);
	
	/**
	 * 查询充值金额 和 人民币金额
	 * @param fundIoCashRechargeVO
	 * @return       List<FundIoCashRechargeVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月4日 上午10:14:55
	 */
	public List<FundIoCashRechargeVO> selectFundChargeFlow(FundIoCashRechargeVO fundIoCashRechargeVO);
	
	/**
	 * 查询充值明细数据的数量
	 * @param fundIoCashRechargeVO
	 * @return    Integer
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月4日 上午10:16:28
	 */
	public Integer selectFundChargeFlowCount(FundIoCashRechargeVO fundIoCashRechargeVO);
	
	/**
	 * 查询用户的提现记录(分页)
	 * @param cashWithdrawalVO
	 * @return    List<FundIoCashWithdrawalVO>
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月4日 下午3:05:30
	 */
	public List<FundIoCashWithdrawalVO> selectFundWithdrawalPage(FundIoCashWithdrawalVO cashWithdrawalVO);
	
	/**
	 * 查询用户的提现记录
	 * @param cashWithdrawalVO
	 * @return    List<FundIoCashWithdrawalVO>
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月4日 下午3:05:30
	 */
	public List<FundIoCashWithdrawalVO> selectFundWithdrawal(FundIoCashWithdrawalVO cashWithdrawalVO);
	
	/**
	 * 查询充值明细数据的数量
	 * @param fundIoCashRechargeVO
	 * @return    Integer
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月4日 上午10:16:28
	 */
	public Integer selectFundWithdrawalCount(FundIoCashWithdrawalVO fundIoCashRechargeVO);
	
	/**
	 * 查询具体的某一条转账明细内容
	 * @param id 转账id
	 * @return    
	 * @return:       FundTransferDetailVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午4:48:16
	 */
	public FundIoCashWithdrawalVO getFundDrawDetailById(String id);
	
	/**
	 * 查询用户转账明细(分页)
	 * @param fundTransferDetailVO
	 * @return    
	 * @return:       List<FundTransferDetailVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午2:38:12
	 */
	public List<FundTransferDetailVO> selectFundTransferPageDetail(FundTransferDetailVO fundTransferDetailVO);
	
	/**
	 * 查询用户转账明细
	 * @param fundTransferDetailVO
	 * @return    
	 * @return:       List<FundTransferDetailVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午2:38:12
	 */
	public List<FundTransferDetailVO> selectFundTransferDetailList(FundTransferDetailVO fundTransferDetailVO);
	
	/**
	 * 查询用户转账明细
	 * @param fundTransferDetailVO
	 * @return    
	 * @return:       List<FundTransferDetailVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午2:38:12
	 */
	public List<FundTransferDetailVO> selectFundTransferDetailListTotalAmt(FundTransferDetailVO fundTransferDetailVO);
	/**
	 * 查询用户转账明细(总数)
	 * @param fundTransferDetailVO
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午3:00:42
	 */
	public Integer selectFundTransferCount(FundTransferDetailVO fundTransferDetailVO);
	
	/**
	 * 查询具体的某一条转账明细内容
	 * @param id 转账id
	 * @return    
	 * @return:       FundTransferDetailVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午4:48:16
	 */
	public FundTransferDetailById getFundTransferDetailById(@Param("ioId")Integer id);
	
	/**
	 * 获取用户的总的提现金额（成功的） 
	 * @param userId 用户ID
	 * @param doneDate
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:23:13
	 */
	public Double getUserFundTransferTotalAmount(@Param("userId")String userId,@Param("doneDate")Date doneDate);
	
	/**
	 * 获取用户的总的提现手续费（成功的） 
	 * @param userId
	 * @param doneDate
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:23:42
	 */
	public Double getUserFundTransferTotalTax(@Param("userId")String userId,@Param("doneDate")Date doneDate);
	
	/**
	 * 获取用户资金账户信息
	 * @return    
	 * @return:       FundMainCashScore    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午5:58:39
	 */
	public List<FundMainCashScore> getUserMainCashScoreList(FundMainCashScore cashScore);
	
	/**
	 * 获取用户数量
	 * @param cashScore
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 下午6:01:50
	 */
	public Integer getUserMainCashScoreCount(FundMainCashScore cashScore);
	
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
	
	/**
	 * 查询银行信息
	 * @return    
	 * @return:       List<BankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月18日 下午4:26:09
	 */
	public List<BankInfo> fundBankInfo();
	
	/**
	 * 查询现金流配置
	 * @return    
	 * @return:       List<BankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月18日 下午4:26:09
	 */
	public List<FundOptCode> getFundOptCode();
	
	/**
	 * 查询一级现金流
	 * @return    
	 * @return:       List<FundOptCode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月21日 下午3:01:15
	 */
	public List<FundOptCode> getFirstFundOptCode(@Param("fundType")Integer fund_type);
	
	/**
	 * 通过一级现金流 查询二级
	 * @param fund_type
	 * @param firstOptCode
	 * @return    
	 * @return:       List<FundOptCode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月21日 下午3:20:47
	 */
	public List<FundOptCode> getSecondFundOptCode(@Param("fundType")Integer fund_type,@Param("firstOptCode")Integer firstOptCode);
	
	/**
	 * 通过一级业务码查询现金流配置信息(排除入金)
	 * @param firstOptCode
	 * @return    
	 * @return:       List<FundOptCode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月2日 下午5:22:12
	 */
	public List<FundOptCode> getFundOptCodeByFirst(@Param("list")List<String> list);
}
