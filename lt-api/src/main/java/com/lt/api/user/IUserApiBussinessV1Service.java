package com.lt.api.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundVo;
import com.lt.model.user.BankInfo;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.UserChargeBankInfo;
import com.lt.model.user.UserService;
import com.lt.model.user.UserServiceMapper;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.error.LTException;
import com.lt.vo.user.BankcardVo;
import com.lt.vo.user.UserAccountInfoVo;

/**   
* 项目名称：lt-api   
* 类名称：IUserApiBussinessService   
* 类描述： 用户对外业务接口  
* 创建人：yubei   
* 创建时间：2016年12月7日 下午7:12:30      
*/
public interface IUserApiBussinessV1Service {
	
	/**
	 * 用户激活服务
	 * @param mapper 包含 用户id，服务编码
	 * @return    
	 * @return:       String   返回处理结果  
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月7日 下午7:13:49
	 */
	public void activeUserService(UserServiceMapper mapper) throws LTException;
	
	/**
	 * 判断用户是否激活服务
	 * @param userId
	 * @param serviceCode
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月11日 下午2:52:31
	 */
	public boolean isUserServiceActived(String userId,String serviceCode) throws LTException;
	
	/**
	 * 用户开户流程
	 * @param updateInfoLog 用户id,deviceModel,ip
	 * @param value1 步骤为0：无   步骤 1：真实姓名 步骤 2：身份证正面链接 步骤3：银行名称 步骤4：无
	 * @param value2 步骤为0：无   步骤 1：正确身份证 步骤 2：身份证反面连接 步骤3：银行卡号 步骤4：无
	 * @param step
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月8日 下午3:19:21
	 */
	public void userActiveAccount(UserUpdateInfoLog updateInfoLog,String value1,String value2,String step,String brandCode)  throws LTException;
	
	/**
	 * 用户开户流程,获取历史流程记录
	 * @param updateInfoLog
	 * @return
	 * @throws LTException    
	 * @return:       HashMap<String,Object>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月15日 上午9:59:45
	 */
	public HashMap<String,Object> userActiveAccount0(String user_id) throws LTException;
	
	/**
	 * 用户开户流程,提交银行卡信息
	 * @param updateInfoLog
	 * @param bankName
	 * @param bankNum
	 * @return
	 * @throws LTException    
	 * @return:       HashMap<String,Object>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月15日 上午10:25:43
	 */
	public void userActiveAccount3(UserUpdateInfoLog updateInfoLog,String bankName,String bankNum, int isDefault) throws LTException;
	
	/**
	 * 获取用户的账户服务
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月9日 下午2:43:35
	 */
	public ArrayList<UserService> getUserAccountService() throws LTException; 
	
	/**
	 * 激活用户的开户状态
	 * @param userId
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月10日 下午5:42:34
	 */
	public void activeUserAccountStatus(String userId);
	
	/**
	 * 查询用户关联银行卡表
	 * @param bankInfo 查询条件用户id，银行卡id
	 * @return    
	 * @return:       List<UserBussinessInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月12日 下午8:48:30
	 */
	public UserBussinessInfo getUserBankList(UserBankInfo bankInfo);
	
	/**
	 * 返回用户首页信息
	 * @param userId 用户id
	 * @return    
	 * @return:       Map<String,Object>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月19日 下午9:00:56
	 */
	public List<UserService> getUserHomePageInfo(String userId) throws LTException;
	
	/**
	 * 返回用户余额和用户保证金
	 * @param userId 用户id
	 * @return    
	 * @return:       Map<String,Object>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月19日 下午9:00:56
	 */
	public FundVo getUserAmt(String userId) throws LTException;
	
	/**
	 * 获取银行基础列表
	 * @return
	 * @throws LTException    
	 * @return:       List<BankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月5日 上午9:21:32
	 */
	public List<BankInfo> getBankInfo()  throws LTException;
	
	/**
	 * 获取出金银行卡信息
	 * @param userId
	 * @return
	 * @throws LTException
	 */
	public Map<String,Object> getWithdrawBankInfo(String userId)  throws LTException;
	
	/**
	 * 获取用户资金界面详情
	 * @return
	 * @throws LTException    
	 * @return:       UserBussinessInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月6日 下午3:28:45
	 */
	public UserBussinessInfo getUserBussiness(UserBaseInfo baseInfo) throws LTException;
	
	/**
	 * 修改用户身份证图片和银行图片
	 * @param bussinessInfo
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月21日 上午11:03:36
	 */
	public void UpdateUserIdPicAndBankPic(UserBussinessInfo bussinessInfo) throws LTException;
	
	/**
	 * 返回用户充值的银行卡信息
	 * @param useId
	 * @return 
	 * @throws LTException    
	 * @return:       UserChargeBankInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月27日 下午5:22:13
	 */
	public UserchargeBankDetailInfo getUserChargeBankInfo(String useId) throws LTException;
	
	
	/**
	 * 返回用户绑定的银行卡信息
	 * @param useId
	 * @return
	 * @throws LTException    
	 * @return:       List<UserChargeBankInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月27日 下午5:24:09
	 */
	public List<UserChargeBankInfo> getUserChargeBankInfoList(String useId) throws LTException;

	/**
	 * 获取默认银行卡信息
	 * @param userId
	 * @return
	 */
	public UserAccountInfoVo getUserAccountInfo(String userId);

	/**
	 * 获取用户绑定的银行卡信息
	 * @param userId
	 * @return
	 */
	public List<BankcardVo> getBankcardVo(String userId);

	/**
	 * 删除用户银行卡
	 * @param updateInfoLog
	 * @param id
	 * @param userId
	 */
	public void deleteBankcard(UserUpdateInfoLog updateInfoLog, int id, String userId);

	/**
	 * 查询用户
	 * @param userId
	 * @return
	 */
	public UserBaseInfo queryUserBuyId(String userId);
	
	public String queryUserId(String id);
	
}
