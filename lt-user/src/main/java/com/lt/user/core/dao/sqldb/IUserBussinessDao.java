package com.lt.user.core.dao.sqldb;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.model.user.BankInfo;
import com.lt.model.user.UserBankCardAuth;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.UserChargeBankInfo;
import com.lt.model.user.UserService;
import com.lt.model.user.UserServiceMapper;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.vo.user.BankcardVo;
import com.lt.vo.user.UserAccountInfoVo;

/**   
* 项目名称：lt-user   
* 类名称：IUserBussinessDao   
* 类描述： 用户业务查询dao  
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午7:25:31      
*/
public interface IUserBussinessDao {
	
	/**
	 * 查询非注销状态用户开户信息
	 * @return    
	 * @return:       UserBussinessInfo  提供 id查询   
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月8日 下午4:04:53
	 */
	public UserBussinessInfo getUserActivityAccountInfo(UserBussinessInfo bussinessInfo);
	
	/**
	 * 修改用户实名信息
	 * @param bussinessInfo 正式姓名，身份证号，身份证正反面链接
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月8日 下午5:39:15
	 */
	public int updateUserBussinessInfo(UserBussinessInfo bussinessInfo);

	/**
	 * 查询非注销状态用户 （查询条件数据）数量
	 * @param bussinessInfo 包含条件 身份证号
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月9日 上午10:47:45
	 */
	public Integer getUserUserIdCard(UserBussinessInfo bussinessInfo);
	
	/**
	 * 存储用户银行卡信息
	 * @param bankInfo
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月9日 上午11:20:51
	 */
	public int insertUserBankInfo(UserBankInfo bankInfo);
	
	/**
	 * 查询用户银行卡信息
	 * @param bankInfo 支持 银行卡号，用户id,银行名称
	 * @return    
	 * @return:       List<UserBankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月12日 下午9:07:51
	 */
	public List<UserBankInfo> getUserBankInfo(UserBankInfo bankInfo);
	
	/**
	 * 查询在使用的用户服务
	 * @param service 支持条件 service_code，默认状态
	 * @return    
	 * @return:       List<UserService>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月9日 下午2:18:37
	 */
	public List<UserService> getUserService(UserService service);
	
	/**
	 * 用户管理服务存储
	 * @param serviceMapper
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月9日 下午2:35:02
	 */
	public int insertUserServiceMapper(UserServiceMapper serviceMapper);
	
	/**
	 * 激活用户的开户状态
	 * @param user_id
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月10日 下午5:46:01
	 */
	public int activeUserAccount(String user_id);
	
	/**
	 * 查询用户绑定的银行卡信息
	 * @param bankInfo 支持条件 user_id，bank_id,bank_num
	 * @return    
	 * @return:       UserBussinessInfo
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月14日 上午10:10:32
	 */
	public UserBussinessInfo getUserDefaultBankInfo(UserBankInfo bankInfo);

	/**
	 * 查询用户绑定的银行卡信息(智付接口专用)
	 * @param bankInfo 支持条件 user_id，bank_id,bank_num
	 * @return
	 * @return:       UserBussinessInfo
	 * @throws
	 */
	public UserBussinessInfo getUserDefaultBankInfoForDinpay(UserBankInfo bankInfo);
	/**
	 * 首页获取用户服务列表
	 * @param user_id
	 * @return    
	 * @return:       List<UserService>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月19日 下午9:23:59
	 */
	public List<UserService> getUserAccountServiceByUserId(String user_id);
	
	/**
	 * 获取用户已经激活的列表 （返回内容包括服务id，用户id）
	 * @param user_id
	 * @return    
	 * @return:       List<UserServiceMapper>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月19日 下午9:23:59
	 */
	public List<UserServiceMapper> getUserActivedService(String user_id);
	
	/**
	 * 返回所有的银行信息
	 * @return    
	 * @return:       List<BankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月5日 上午9:25:58
	 */
	public List<BankInfo> getBankInfo();
	
	/**
	 * 获取银行信息
	 * @param bankCode
	 * @return
	 */
	public BankInfo getBankInfoByCode(String bankCode);
	
	/**
	 * 查询用户详情信息，包括银行卡和开户信息
	 * @param baseInfo(目前支持id 查询)
	 * @return    
	 * @return:       UserBussinessInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 下午3:31:18
	 */
	public UserBussinessInfo getUserDetailInfo(UserBaseInfo baseInfo);
	
	/**
	 * 获取出金银行卡信息
	 * @param userId
	 * @return
	 */
	public Map<String,Object> selectWithdrawBankInfo(String userId);
	
	/**
	 * 获取用户入金默认银行卡信息
	 * @param userId
	 * @return    
	 * @return:       UserChargeBankInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月28日 上午9:09:21
	 */
	public UserchargeBankDetailInfo getUserChargebankDetail(Map<String,String> paraMap);
	
	/**
	 * 查询用户出金银行卡列表
	 * @param userId
	 * @return    
	 * @return:       List<UserChargeBankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月28日 上午9:57:48
	 */
	public List<UserChargeBankInfo> getUserChargeBankInfoList(String userId);

	/**
	 * 获取账户信息 可用余额 手机号 姓名 身份证号
	 * @param userId
	 * @return
	 */
	public UserAccountInfoVo getUserAccountInfo(String userId);
	
	/**
	 * 根据id，查询用户信息
	 * @param userId
	 * @return
	 */
	public UserBaseInfo getUserBaseInfo(String userId);
	

	/**
	 * 获取用户绑定的银行卡信息
	 * @param userId
	 * @return
	 */
	public List<BankcardVo> getBankcardVo(String userId);
	
	/**
	 * 删除用户银行卡
	 * @param id
	 * @return
	 */
	public int deleteBankCard(@Param("bankId")Integer id,@Param("userId")String userId);
	
	
	/**
	 * 根据身份证号码查询是否有开户成功
	 * @param icCardNum
	 * @param openAccountStatus
	 * @return
	 */
	public int getUserBaseCountByIdCardNum(String idCardNum);

	/**
	 * 根据身份证号码查询是否有开户成功 品牌筛选
	 */
	public int getUserBaseCountByIdCardNumAndBrandId(String idCardNum,String brandId);

	/**保存用户银行卡验证信息**/
	
	public int insertUserBaseCardAuth(UserBankCardAuth userBankCardAuth);
	
	/** 获取用户充值渠道最大的金额 1 :单笔  2 ：日限额*/
	public List<Double> getMaxChargeAmt(String bankCode);
}
