package com.lt.manager.dao.fund;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundIoCashWithdrawal;

public interface FundWithdrawDao {
	
	/**
	 * 审核用户提现申请
	 * @param paramMap 查询条件 ： 多个流水id，修改用户id，状态，备注，审核日期
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 上午10:06:16
	 */
//	public Integer auditWithdraw(@Param("ids") String ids,@Param("modifyId") String modifyId,@Param("status") String status,
//			@Param("remark")String remark,@Param("auditDate")String auditDate);
	public Integer auditWithdraw(Map<String,String> paramMap);
	
	/**
	 * 查询支付宝待转账信息
	 * @param paramMap  查询条件 ：多个流水ID，状态
	 * @return    
	 * @return:       List<FundIoCashWithdrawalVO>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 上午10:05:28
	 */
//	public List<FundIoCashWithdrawalVO> alipayTranferToBank(@Param("ioId") String ids,@Param("status") String status);
	public List<FundIoCashWithdrawalVO> alipayTranferToBank(Map<String,String> paramMap);
	
	/**
	 * 根据ID查询具体体现单
	 * @param fundIoCashWithdrawal 查询条件  pay_id ， id
	 * @return    
	 * @return:       FundIoCashWithdrawal    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 下午7:01:09
	 */
	public FundIoCashWithdrawal qryFundCashWithdsById(FundIoCashWithdrawal fundIoCashWithdrawal);
	
	/**
	 * 修改转账详细信息
	 * @param paramMap 
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 下午7:26:19
	 */
	public Integer updateTransferDetail(Map<String,String> paramMap);
	
	/**
	 * 查询充值明细
	 * @param id
	 * @return    
	 * @return:       FundIoCashRecharge    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 下午5:19:24
	 */
	public FundIoCashRecharge selectRechargeIo(String id);
	
	/**
	 * 查询用户充值明细（补单使用）
	 * @param id
	 * @return    
	 * @return:       FundIoCashRechargeVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 下午7:05:38
	 */
	public FundIoCashRechargeVO selectFundRepair(FundIoCashRechargeVO cashRechargeVO);
	
	/**
	 * 修改充值记录为失败状态表（批量）
	 * @param list 
	 * @param reamrk     
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月17日 下午1:36:07
	 */
	public void updateFinancyIoMutil(@Param("list")List list);
}
