package com.lt.manager.dao.user;

import java.util.List;

import com.lt.manager.bean.user.BankVo;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.UserChargeMapper;

/**
 * 
 * 商品类型管理dao
 * @author yubei
 * @date 2017年6月13日
 *
 */
public interface BankManageDao {

	/**
	 * 添加银行信息
	 * 
	 * @param bankVo
	 */
	public void insertBankInfo(BankVo bankVo);
	
	/**
	 * 添加银行充值信息
	 * @param bankVo
	 */
	public void insertBankCharge(BankVo bankVo);

	/**
	 * 修改银行信息
	 * 
	 * @param bankVo
	 */
	public void updateBankInfo(BankVo bankVo);
	
	/**
	 * 修改银行充值信息
	 * @param bankVo
	 */
	public void updateBankCharge(BankVo bankVo);

	/***
	 * 删除银行信息
	 * 
	 * @param bankVo
	 */
	public void deleteBankInfo(BankVo bankVo);

	/**
	 * 删除银行充值信息
	 * @param bankVo
	 */
	public void deleteBankCharge(BankVo bankVo);
	/**
	 * 查询银行充值列表
	 * @return
	 */
	public List<BankVo> selectBankVoList(BankVo bankVo);
	
	/**
	 * 查询银行列表总数
	 * @param bankVo
	 * @return
	 */
	public Integer selectBankVoCount(BankVo bankVo);
	
	/**
	 * 查询银行充值信息
	 * @param bankVo
	 * @return
	 */
	public BankVo selectBankVo(String bankCode);
	
	/**
	 * 根据银行编号查询渠道列表信息
	 * @param bankCode
	 * @return
	 */
	public List<BankVo> selectChannelByBankCode(String bankCode);
	
	/**
	 * 查询银行限额信息列表
	 * @param bankCode
	 * @return
	 */
	public List<BankChargeMapper> selectBankLimitInfoList(String bankCode);
	/**
	 * 查询银行信息列表
	 * @param bankCode
	 * @return
	 */
	public List<BankVo> selectBankInfoList(BankVo bankVo);
	
	/**
	 * 查询银行信息总数
	 * @return
	 */	
	public Integer selectBankInfoCount();
	
	/**
	 * 查询银行渠道列表
	 * @return
	 */
	
	public List<BankVo> selectChargeChannelPriorityList();
	/**
	 * 查询银行渠道列表
	 * @return
	 */
	public List<BankVo> selectChargeChannelList();
	
	
	/**
	 * 查询当前银行最大编号
	 * @return
	 */
	public String selectMaxBankCode();
	
	
	/**
	 * 根据银行编号查询渠道列表信息
	 * @param bankCode
	 * @return
	 */
	public List<BankVo> selectBankVoByBankCode(String bankCode);	
	
	/**
	 * 
	 * @return    
	 * @return:       List<UserBase>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午4:00:39
	 */
	public List<UserBaseInfoQueryVO> qryAllUser();
	
	/**
	 * 插入用户渠道关联关系
	 * @param userChargeMapper    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午2:29:17
	 */
	public void insertUserChargeMapperMutil(List<UserChargeMapper> userChargeMapper);
}
