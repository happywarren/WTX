package com.lt.manager.service.user;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.user.BankVo;

/**
 * 用户管理
 * 
 * @author yubei
 * @date 2017年6月13日
 *
 */
public interface IBankManageService {

	/**
	 * 添加银行通道信息
	 * 
	 * @return
	 */
	public void addBankVo(BankVo bankVo);
	
	/**
	 * 添加银行基础信息
	 * @param bankVo
	 */
	public String addBankInfo(BankVo bankVo);


	/**
	 * 修改银行充值信息
	 * 
	 * @param bankVo
	 */
	public void modifyBankVo(BankVo bankVo);

	/**
	 * 修改银行基础信息
	 * @param bankVo
	 */
	public void modifyBankInfo(BankVo bankVo);
	/**
	 * 删除银行信息
	 * 
	 */
	public void removeBankVo(BankVo bankVo);

	/**
	 * 查询银行信息
	 */
	public BankVo queryBankVo(String bankCode);

	/**
	 * 根据银行编号查询渠道列表信息
	 * @param bankCode
	 * @return
	 */
	public List<BankVo> queryChannelByBankCode(String bankCode);
	
	/**
	 * 查询银行信息列表
	 * 
	 * @return
	 */
	public List<BankVo> queryBankInfoList();
	
	
	/**
	 * 查询银行渠道列表
	 * @return
	 */
	public List<BankVo> queryBankChannelList();
	
	/**
	 * 查询银行渠道优先级列表
	 * @return
	 */
	public BankVo queryBankChannelPriorityList();
	
	
	/**
	 * 查询银行渠道信息
	 * @param bankVo
	 * @return
	 */
	public List<BankVo> queryBankVoList(BankVo bankVo);

	/**
	 * 查询银行渠道信息
	 * @param bankVo
	 * @return
	 */
	public List<Map<String, Object>> queryBankInfoList(BankVo bankVo);
	/**
	 * 查询银行渠道信息 次数
	 * @param bankVo
	 * @return
	 */
	public Integer queryBankVoCount();
	/**
	 * 根据银行编号查询银
	 * @param bankCode
	 * @return
	 */
	public List<BankVo> queryBankVoByBankCode(String bankCode);	
	
	/**
	 * 初始化用户充值渠道
	 *     
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午3:59:14
	 */
	public void initUserChargeMapper(String channelCode);
}
