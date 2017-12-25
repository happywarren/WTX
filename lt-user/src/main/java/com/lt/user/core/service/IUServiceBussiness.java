package com.lt.user.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 项目名称：lt-user
 * 类名称：IUServiceBussiness
 * 类描述：用户业务处理的内部接口
 * 创建人：yuanxin
 * 创建时间：2016年12月7日 下午7:18:44
 */
public interface IUServiceBussiness {

    /**
     * 用户进入开户流程
     *
     * @param userId 用户id
     * @return
     * @throws
     * @return: Response
     * @author yuanxin
     * @Date 2016年12月8日 下午5:27:16
     */
    public HashMap<String, Object> userActivityAccountStep0(String userId) throws LTException;

    /**
     * 用户开户过程中的填真实姓名和身份证号
     *
     * @param updateInfoLog 包括用户id， deviceModel，ip
     * @param user_name     真实姓名
     * @param idCard        身份证号
     * @param url_positive  照片正面
     * @param url_reverse   照片反面     
     * @return
     * @throws
     * @return: Response
     * @author yuanxin
     * @Date 2016年12月8日 下午7:16:29
     */
    public void userActivityAccountStep1(UserUpdateInfoLog updateInfoLog, String userName, String idCard,String urlPositive, String urlReverse,String urlFacePath) throws LTException;

    /**
     *开户，保存用户测评风险数据
     * @return
     * @throws
     * @author yubei
     * @Date 2016年12月9日 上午10:42:55
     */
    public void userActivityAccountStep3(UserUpdateInfoLog updateInfoLog, String riskRet,String riskLevel) throws LTException;
    
    
    /**
     * 开户，保存用户签名
     * @param updateInfoLog
     * @param signPicPath
     * @param riskRet
     * @throws LTException
     */
    public void userActivityAccountStep4(UserUpdateInfoLog updateInfoLog, String signPicPath) throws LTException;

    /**
     * 用户开户过程中绑定银行卡操作
     *
     * @param updateInfoLog包括用户id， deviceModel，ip
     * @param bankName             银行卡名称
     * @param bankNum              银行卡账号
     * @return
     * @throws
     * @return: Response
     * @author yuanxin
     * @Date 2016年12月9日 上午11:00:58
     */
    public Map<String, Object> userActivityAccountStep2(UserUpdateInfoLog updateInfoLog, String bankCode, String bankNum, int isDefault) throws LTException;

    /**
     * 保存开户日志
     * @param updateInfoLog
     * @param step
     */
    public void userActivityAccountLog(UserUpdateInfoLog updateInfoLog,String step);
    /**
     * 用户服务查询
     *
     * @param service 支持查询条件 service_code
     * @return
     * @throws
     * @return: List<UserService>
     * @author yuanxin
     * @Date 2016年12月9日 下午2:16:40
     */
    public List<UserService> queryUserService(UserService service) throws LTException;

    /**
     * 用户关联开通服务
     *
     * @param mapper
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年12月9日 下午2:32:02
     */
    public boolean activeUserService(UserServiceMapper mapper) throws LTException;

    /**
     * 激活用户的开户状态
     *
     * @param userId
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年12月10日 下午5:44:25
     */
    public void activeUserAccount(String userId);

    /**
     * 获取用户银行卡列表
     *
     * @param bankInfo
     * @return
     * @throws
     * @return: UserBussinessInfo
     * @author yuanxin
     * @Date 2016年12月14日 上午9:59:13
     */
    public UserBussinessInfo getUserDefaultBankInfo(UserBankInfo bankInfo);
    /**
     * 获取用户绑定银行卡信息(智付接口专用)
     *
     * @param bankInfo
     * @return
     * @throws
     * @return: UserBussinessInfo
     */
    public UserBussinessInfo getUserDefaultBankInfoForDinpay(UserBankInfo bankInfo);

    /**
     * 获取用户服务
     *
     * @param userId 用户id
     * @return
     * @throws
     * @return: List<UserService>
     * @author yuanxin
     * @Date 2016年12月19日 下午9:32:15
     */
    public List<UserService> getUserService(String userId) throws LTException;

    /**
     * 查询银行列表
     *
     * @param userId
     * @return
     * @throws LTException
     * @throws
     * @return: List<BankInfo>
     * @author yuanxin
     * @Date 2017年1月5日 上午9:24:34
     */
    public List<BankInfo> getBankInfo() throws LTException;

    /**
     * 查询用户详细信息
     *
     * @param bankInfo 支持id 查询
     * @return
     * @throws LTException
     * @throws
     * @return: UserBussinessInfo
     * @author yuanxin
     * @Date 2017年1月6日 下午3:48:42
     */
    public UserBussinessInfo getUserDetailInfo(UserBaseInfo bankInfo) throws LTException;

    /**
     * 获取用户已经激活的服务
     *
     * @param userId
     * @return
     * @throws LTException
     * @throws
     * @return: List<UserServiceMapper>
     * @author yuanxin
     * @Date 2017年1月11日 下午3:00:21
     */
    public List<UserServiceMapper> getUserActivedService(String userId) throws LTException;

    /**
     * 修改用户身份证照片和银行卡照片
     *
     * @param bussinessInfo
     * @throws LTException
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2017年3月21日 上午11:05:29
     */
    public void updateUserIdPicAndBankPic(UserBussinessInfo bussinessInfo, UserUpdateInfoLog updateInfoLog) throws LTException;

    /**
     * 获取出金银行卡信息
     *
     * @param userId
     * @return
     * @throws LTException
     */
    public Map<String, Object> getWithdrawBankInfo(String userId) throws LTException;

    /**
     * 获取用户出金银行卡信息
     *
     * @param userId
     * @return
     * @throws LTException
     * @throws
     * @return: UserChargeBankInfo
     * @author yuanxin
     * @Date 2017年3月27日 下午5:31:42
     */
    public UserchargeBankDetailInfo getUserChargeBankDetailInfo(String userId) throws LTException;

    /**
     * 获取用户绑定银行卡列表
     *
     * @param userId
     * @return
     * @throws LTException
     * @throws
     * @return: List<UserChargeBankInfo>
     * @author yuanxin
     * @Date 2017年3月28日 上午9:52:47
     */
    public List<UserChargeBankInfo> getUserChargeBankInfoList(String userId) throws LTException;

    /**
     * 获取账户信息 可用余额 手机号 姓名 身份证号
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
    void deleteBankCard(UserUpdateInfoLog updateInfoLog, int id, String userId);

    
    /**
     * 获取用户开户步骤
     * @param userId
     * @return
     * @throws LTException
     */
    public int getActivityAccountStep(String userId) throws LTException;
    
    
    /**
     * 查询用户开户次数
     * @param userId
     * @return
     * @throws LTException
     */
    public int getUserOpeningCount(String userId,int step) throws LTException;
    
    /**
     * 查询身份证号码历史是否已经开户成功
     * @param idCardNum
     * @return
     * @throws LTException
     */
    public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum) throws LTException;
    

    /**
     * 查询身份证号码历史是否已经开户成功 品牌筛选
     */
    public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum,String brandCode) throws LTException;


    /**
     * 查询用户出入金限额信息
     * @return
     * @throws LTException
     */
    public Map<String, Object> getUserFundInOutLimitInfo() throws LTException;
}
