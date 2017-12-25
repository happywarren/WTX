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
 * 创建人：yuanxin
 * 创建时间：2016年12月7日 下午7:12:30
 */
public interface IUserApiBussinessService {

    /**
     * 用户激活服务
     *
     * @param mapper 包含 用户id，服务编码
     * @return
     * @throws
     * @return: String   返回处理结果
     * @author yuanxin
     * @Date 2016年12月7日 下午7:13:49
     */
    public void activeUserService(UserServiceMapper mapper) throws LTException;

    /**
     * 判断用户是否激活服务
     *
     * @param userId
     * @param serviceCode
     * @throws LTException
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2017年1月11日 下午2:52:31
     */
    public boolean isUserServiceActived(String userId, String serviceCode) throws LTException;

    /**
     * 用户开户流程
     *
     * @param updateInfoLog 用户id,deviceModel,ip
     * @param value1        步骤为0：无   步骤 1：真实姓名 步骤 2：身份证正面链接 步骤3：银行名称 步骤4：无
     * @param value2        步骤为0：无   步骤 1：正确身份证 步骤 2：身份证反面连接 步骤3：银行卡号 步骤4：无
     * @param step
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月8日 下午3:19:21
     */
    public void userActiveAccount(UserUpdateInfoLog updateInfoLog, String value1, String value2, String value3, String value4, String value5, String value6, String step) throws LTException;

    /**
     * 用户开户流程,获取历史流程记录
     *
     * @param updateInfoLog
     * @return
     * @throws LTException
     * @throws
     * @return: HashMap<String,Object>
     * @author yuanxin
     * @Date 2016年12月15日 上午9:59:45
     */
    public HashMap<String, Object> userActiveAccount0(String user_id) throws LTException;

    /**
     * 用户开户流程,提交银行卡信息
     *
     * @param updateInfoLog
     * @param bankName
     * @param bankNum
     * @return
     * @throws LTException
     * @throws
     * @return: HashMap<String,Object>
     * @author yuanxin
     * @Date 2016年12月15日 上午10:25:43
     */
    public void userActiveAccount1(UserUpdateInfoLog updateInfoLog, String userName, String idCard, String urlPositive, String urlReverse, String urlFacePath) throws LTException;

    /**
     * 保存开户日志
     */
    public void saveOpenLog(UserUpdateInfoLog updateInfoLog, String step);

    /**
     * 用户开户流程，保存银行，卡号，默认信息
     *
     * @param updateInfoLog
     * @param bankName      银行名称
     * @param bankNum       卡号
     * @param isDefalut     默认标志
     * @throws LTException
     */
    public Map<String, Object> userActiveAccount2(UserUpdateInfoLog updateInfoLog, String bankName, String bankNum, int isDefalut) throws LTException;

    /**
     * 用户开户流程，完成测评和风险阅读，保存风险测评结果
     *
     * @param updateInfoLog 用户日志
     * @param signPic       签名路径
     * @param riskRet       测评结果
     * @throws LTException
     */
    public void userActiveAccount3(UserUpdateInfoLog updateInfoLog, String riskRet, String riskLevel) throws LTException;


    /**
     * 用户开户流程，确认协议，保存用户签名
     *
     * @param updateInfoLog
     * @param signPic
     * @throws LTException
     */
    public void userActiveAccount4(UserUpdateInfoLog updateInfoLog, String signPic) throws LTException;

    /**
     * 获取用户的账户服务
     *
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月9日 下午2:43:35
     */
    public ArrayList<UserService> getUserAccountService() throws LTException;

    /**
     * 激活用户的开户状态
     *
     * @param userId
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月10日 下午5:42:34
     */
    public void activeUserAccountStatus(String userId);

    /**
     * 查询用户关联银行卡表
     *
     * @param bankInfo 查询条件用户id，银行卡id
     * @return
     * @throws
     * @return: List<UserBussinessInfo>
     * @author yuanxin
     * @Date 2016年12月12日 下午8:48:30
     */
    public UserBussinessInfo getUserBankList(UserBankInfo bankInfo);

    /**
     * 查询用户绑定的银行卡信息(智付接口专用)
     *
     * @param bankInfo 支持条件 user_id，bank_id,bank_num
     * @return
     * @throws
     * @return: UserBussinessInfo
     */
    public UserBussinessInfo getUserDefaultBankInfoForDinpay(UserBankInfo bankInfo);


    /**
     * 返回用户首页信息
     *
     * @param userId 用户id
     * @return
     * @throws
     * @return: Map<String,Object>
     * @author yuanxin
     * @Date 2016年12月19日 下午9:00:56
     */
    public List<UserService> getUserHomePageInfo(String userId) throws LTException;

    /**
     * 返回用户余额和用户保证金
     *
     * @param userId 用户id
     * @return
     * @throws
     * @return: Map<String,Object>
     * @author yuanxin
     * @Date 2016年12月19日 下午9:00:56
     */
    public FundVo getUserAmt(String userId) throws LTException;

    /**
     * 获取银行基础列表
     *
     * @return
     * @throws LTException
     * @throws
     * @return: List<BankInfo>
     * @author yuanxin
     * @Date 2017年1月5日 上午9:21:32
     */
    public List<BankInfo> getBankInfo() throws LTException;

    /**
     * 获取出金银行卡信息
     *
     * @param userId
     * @return
     * @throws LTException
     */
    public Map<String, Object> getWithdrawBankInfo(String userId) throws LTException;

    /**
     * 获取用户资金界面详情
     *
     * @return
     * @throws LTException
     * @throws
     * @return: UserBussinessInfo
     * @author yuanxin
     * @Date 2017年1月6日 下午3:28:45
     */
    public UserBussinessInfo getUserBussiness(UserBaseInfo baseInfo) throws LTException;

    /**
     * 修改用户身份证图片和银行图片
     *
     * @param bussinessInfo
     * @throws LTException
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2017年3月21日 上午11:03:36
     */
    public void UpdateUserIdPicAndBankPic(UserBussinessInfo bussinessInfo) throws LTException;

    /**
     * 返回用户充值的银行卡信息
     *
     * @param useId
     * @return
     * @throws LTException
     * @throws
     * @return: UserChargeBankInfo
     * @author yuanxin
     * @Date 2017年3月27日 下午5:22:13
     */
    public UserchargeBankDetailInfo getUserChargeBankInfo(String useId) throws LTException;


    /**
     * 返回用户绑定的银行卡信息
     *
     * @param useId
     * @return
     * @throws LTException
     * @throws
     * @return: List<UserChargeBankInfo>
     * @author yuanxin
     * @Date 2017年3月27日 下午5:24:09
     */
    public List<UserChargeBankInfo> getUserChargeBankInfoList(String useId) throws LTException;

    /**
     * 获取默认银行卡信息
     *
     * @param userId
     * @return
     */
    public UserAccountInfoVo getUserAccountInfo(String userId);

    /**
     * 获取用户绑定的银行卡信息
     *
     * @param userId
     * @return
     */
    public List<BankcardVo> getBankcardVo(String userId);

    /**
     * 删除用户银行卡
     *
     * @param updateInfoLog
     * @param id
     * @param userId
     */
    public void deleteBankcard(UserUpdateInfoLog updateInfoLog, int id, String userId);

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    public UserBaseInfo queryUserBuyId(String userId);

    public String queryUserId(String id);


    /**
     * 获取用户开户步骤
     *
     * @param userId
     * @return
     * @throws LTException
     */
    public int getActivityAccountStep(String userId) throws LTException;


    /**
     * 查询用户开户次数
     *
     * @param userId
     * @return
     * @throws LTException
     */
    public int getUserOpeningCount(String userId, int step) throws LTException;

    /**
     * 查询身份证号码历史是否已经开户成功
     *
     * @param idCardNum
     * @return
     * @throws LTException
     */
    public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum) throws LTException;

    /**
     * 查询身份证号码历史是否已经开户成功 品牌筛选
     */
    public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum, String brandCode) throws LTException;

    /**
     * 查询用户银行卡信息
     *
     * @param userId
     * @param bankId
     * @return
     */
    public UserBankInfo getUserBankInfo(String userId, String bankId);


    /**
     * 查询用户出入金限额信息
     *
     * @return
     * @throws LTException
     */
    public Map<String, Object> getUserFundInOutLimitInfo() throws LTException;
}
