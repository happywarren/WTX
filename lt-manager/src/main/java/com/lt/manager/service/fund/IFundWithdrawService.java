package com.lt.manager.service.fund;

import com.lt.enums.fund.IFundOptCode;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundOptCode;
import com.lt.util.error.LTException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * 提现接口
 * @author jingwb
 * @param <IFundOptCode>
 *
 */
public interface IFundWithdrawService {
	
	/**
	 * 审核提现申请
	 * @param ioIds 多个申请ID
	 * @param operate 操作 1，审核通过 2，审核拒绝
	 * @param modifyId 修改用户ID
	 * @param thirdOptCode 三级现金流代码
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 上午10:15:35
	 */
	public void auditWithdraw(String ioIds,int operate,String modifyId,String remark,IFundOptCode codeEnum) throws LTException;

	/**
	 * 支付宝状态
	 * @param ioIds 列表
	 * @param modifyId 操作用户id
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 下午5:49:01
	 */
	public void alipayToBank(String ioIds,String modifyId) throws LTException;
	
	/**
	 * 查询提现单信息
	 * @param fundIoCashWithdrawal
	 * @return
	 * @throws LTException    
	 * @return:       FundIoCashWithdrawal    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 下午7:02:30
	 */
	public FundIoCashWithdrawal getFundCashDrawInfo(FundIoCashWithdrawal fundIoCashWithdrawal) throws LTException;
	
	/**
	 * 支付宝提现结果处理方法
	 * @param fundIoCashWithdrawal 提现单对象
	 * @param flag 成功或失败处理流程
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月9日 下午7:18:28
	 */
	public void alipayTransferResiveResult(FundIoCashWithdrawal fundIoCashWithdrawal,boolean flag) throws LTException;

	/**
	 * 银生宝提现
	 * @param ioArr
	 * @throws Exception
	 */
	public void withdrawForUnspay(String[] ioArr,Integer transferUserId) throws Exception;
	/**
	 * 银生宝提现
	 * @param ioArr
	 * @throws Exception
	 */
	public void withdrawForUnspayZJ(String[] ioArr,Integer transferUserId) throws Exception;	
	/**
	 * 银生宝提现回调接口
	 * @param map
	 * @throws Exception
	 */
	public void callbackForUnspay(Map<String,Object> map) throws Exception;
	/**
	 * 钱通提现回调接口
	 * @param notifyResultStr
	 * @throws Exception
	 */
	public void callbackForQtong(String notifyResultStr) throws Exception;
	
	/**
	 * 银生宝提现定时任务查询结果接口
	 * @throws Exception
	 */
	public void queryWithdrawResultForUnspay() throws Exception;
	/**
	 * 智付提现定时任务查询结果接口
	 * @throws Exception
	 */
	public void queryWithdrawResultForDinpay() throws Exception;

	/**
	 * 钱通提现定时任务查询结果接口
	 * @throws Exception
	 */
	public void queryWithdrawResultForQtong() throws Exception;

	/**
	 * 智付提现
	 * @param ioArr
	 * @throws Exception
	 */
	public void withdrawForDinpay(String[] ioArr,Integer transferUserId, Integer tranType) throws Exception;


	/**
	 * 爸爸付提现
	 * @param ioArr
	 * @throws Exception
	 */
	public void withdrawForDaddypay(String[] ioArr,Integer transferUserId) throws Exception;

	/**
	 * 钱通提现
	 * @param ioArr
	 * @throws Exception
	 */
	public void withdrawForQtongPay(String[] ioArr,Integer transferUserId) throws Exception;

	/**
	 * 转账成功（公共方法）
	 * @param fio
	 * @throws Exception
	 */
	public void withdrawSuccess(FundIoCashWithdrawal fio,String thirdOptCode) throws Exception;
	
	/**
	 * 转账失败（公共方法）
	 * @param fio
	 * @throws Exception
	 */
	public void withdrawFail(FundIoCashWithdrawal fiog) throws Exception;

	/** 
	 * @param ioIds
	 * @param operate
	 * @param modifyId
	 * @param amount
	 * @param remark
	 * @param codeEnum
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月11日 下午8:58:03 
	 */
	void auditWithdrawSingle(String ioIds, int operate, String modifyId, Double amount, String remark,
			IFundOptCode codeEnum) throws LTException;
	
	/**
	 * 用户补单操作
	 * @param id 流水id
	 * @param amt 补单金额(人民币)
	 * @param rate 利率
	 * @param remark 备注
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 下午1:37:43
	 */
	void repairFinancyIo(String id,String amt,String rmbAmt,FundOptCode fundOptCode,Integer modifyUserId) throws LTException;	
	
	/**
	 * 批量拒绝（充值）
	 * @param ids
	 * @param remark
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月17日 下午1:20:58
	 */
	void financyIoReject(String ids) throws LTException;
	
	/**
	 * 人工转账
	 * @param ids
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月3日 下午3:18:50
	 */
	void manualTransfer(String ids,Integer modifyUserId) throws LTException;
	
	/**
	 * 强制 转账 成功/失败 
	 * @param id 转账id
	 * @param statu 1 成功 2 失败
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月3日 下午4:42:34
	 */
	void transferForce(String id,Integer statu,String remark) throws LTException;
	/**
	 * daddy提现信息确认
	 * @param map
	 * @throws LTException
	 * @throws

	 */
	Map<String,String> requestWithdrawForDaddypay(Map<String, String> map);
	/**
	 * daddy提现结果确认
	 * @param map
	 * @throws LTException
	 * @return:       void
	 * @throws
	 */
	Map<String,String> withdrawalResultForDaddypay(Map<String, String> map);

	/**
	 * 九派提现
	 * @param ioArr
	 * @param transferUserId
	 */
	public void withdrawForJiuPaiPay(String[] ioArr, Integer transferUserId, HttpServletRequest request) throws Exception;

	/**
	 * 九派通知回调
	 * @param map
	 * @return
	 */
	Map<String,String> withdrawalResultForJiuPai(Map<String,String> map,HttpServletRequest request);

}
