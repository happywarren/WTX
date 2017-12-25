package com.lt.user.core.service;

import com.lt.model.sys.QuotaHostBean;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserService;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.error.LTException;
import com.lt.util.utils.model.Token;

import java.util.List;

public interface IUserService {

    /**
     * TODO 根据ID查询用户基本信息
     *
     * @param id
     * @return
     * @author XieZhibing
     * @date 2017年2月6日 上午11:16:40
     */
    public UserBaseInfo queryUserBuyId(String id);

    /**
     * 校验验证短信发送是否已超过上限
     *
     * @param tele    电话
     * @param ip      ip
     * @param smsType 短信类型
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年11月29日 下午2:31:55
     */
    public boolean chkRegiterMaxCount(String tele, String smsType,String brandId);

    /**
     * 校验验证码是否正确
     *
     * @param tele       电话
     * @param brandId    品牌
     * @param smsType    校验短信类型
     * @param clientCode 客户端传值验证码
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年11月30日 下午8:32:02
     */
    public boolean chkRegisterCode(String tele, String brandId, String smsType, String clientCode) throws LTException;


    /**
     * 校验验证码是否正确
     *
     * @param tele       电话
     * @param brandId    品牌
     * @param smsType    校验短信类型
     * @param clientCode 客户端传值验证码
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年11月30日 下午8:32:02
     */
    public boolean chkRegisterCode(String tele, String smsType, String clientCode) throws LTException;

    /**
     * 校验手机号是否已被注册
     *
     * @param tele
     * @return
     * @throws
     * @return: boolean  true :已被注册，
     * false：没被注册
     * @author yuanxin
     * @Date 2016年11月30日 下午9:05:11
     */
    public boolean checkTeleIsUsed(String tele) throws LTException;

    /**
     * 校验手机号是否已被注册
     *
     * @param tele
     * @param brandId
     * @return
     * @throws
     * @return: boolean  true :已被注册，
     * false：没被注册
     * @author yuanxin
     * @Date 2016年11月30日 下午9:05:11
     */
    public boolean checkTeleIsUsed(String tele, String brandId) throws LTException;

    /**
     * 插入用户基本信息
     *
     * @param baseInfo
     * @return
     * @throws Exception
     * @throws
     * @return: int
     * @author yuanxin
     * @Date 2016年12月14日 下午5:47:22
     */
    public int insertUserBaseInfo(UserBaseInfo baseInfo) throws LTException;

    /**
     * 实例化token信息
     *
     * @param baseUser
     * @return
     * @throws
     * @return: Token
     * @author yuanxin
     * @Date 2016年12月1日 上午10:47:18
     */
    public Token initToken(UserBaseInfo baseUser);

    /**
     * 更新用户token
     *
     * @param baseUser
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2016年12月1日 上午10:54:38
     */
    public void updateToken(String id, String token) throws LTException;

    /**
     * 用户登录模块
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月1日 下午3:00:59
     */
    public Integer userLogin(UserBaseInfo baseInfo) throws LTException;

    /**
     * 修改用户昵称
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月2日 下午2:32:48
     */
    public UserBaseInfo updateUserNickName(UserBaseInfo baseInfo) throws LTException;

    /**
     * 修改用户的个性签名
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月2日 下午2:34:24
     */
    public UserBaseInfo updateUserPersonSign(UserBaseInfo baseInfo) throws LTException;

    /**
     * 修改用户的密码
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月2日 下午2:34:39
     */
    public void updateUserPwd(UserBaseInfo baseInfo, String oldPwd) throws LTException;

    /**
     * 修改用户头像
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月2日 下午2:34:58
     */
    public UserBaseInfo updateUserHeadPic(UserBaseInfo baseInfo) throws LTException;

    /**
     * 找回用户密码
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月3日 下午4:07:20
     */
    public UserBaseInfo findUserPwd(UserBaseInfo baseInfo) throws LTException;

    /**
     * 校验 验证验证码是否超过最大操作次数
     *
     * @param tele
     * @throws
     * @return: boolean   true 通过校验，不超过最大次数
     * false未通过校验
     * @author yuanxin
     * @Date 2016年12月5日 下午6:00:06
     */
    public boolean checkRegCodeCount(String tele);

    /**
     * 用户服务自动激活，根据用户服务配置表中默认开启的配置激活服务,并返回服务列表
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年12月14日 上午11:17:58
     */
    public List<UserService> UserAutoActiveService(UserBaseInfo baseInfo) throws LTException;

    /**
     * 查询非注销状态用户信息
     *
     * @param baseInfo 昵称，id，电话,手机状态
     * @return
     * @throws
     * @return: UserBaseInfo
     * @author yuanxin
     * @Date 2016年12月8日 上午9:48:30
     */
    public UserBaseInfo getBaseUserInfoNoCancel(UserBaseInfo baseInfo) throws LTException;

    /**
     * 修改用户信息
     *
     * @param baseInfo
     * @return
     * @throws LTException
     * @throws
     * @return: int
     * @author yuanxin
     * @Date 2016年12月14日 下午9:35:01
     */
    public int updateUserBaseInfo(UserBaseInfo baseInfo) throws LTException;

    /**
     * 插入用户操作日志
     *
     * @param operateLog
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2016年12月14日 下午5:55:04
     */
    public void insertUserOperateLog(UserOperateLog operateLog) throws LTException;

    /**
     * 插入用户修改信息日志
     *
     * @param updateInfoLog
     * @throws LTException
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2016年12月14日 下午9:30:00
     */
    public void insertUserUpdateInfoLog(UserUpdateInfoLog updateInfoLog) throws LTException;

    /**
     * 获取实名认证状态
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Integer getCertificationStatus(String userId) throws Exception;

    /**
     * 验证用户密码是否正确
     *
     * @param userId
     * @param passwd
     * @return
     * @throws Exception
     */
    public boolean checkUserPasswd(String token, String passwd) throws LTException;

    int checkUserPasswdCount(String tokenStr, String passwd) throws LTException;

    public String queryUserId(String id);

    /**
     * 检查userId 用户未登陆 或者在已在其他地方登陆
     *
     * @return
     */
    void checkToken(String userId);


    /**
     * 根据用户昵称查询次数
     *
     * @param nickName
     * @return
     * @throws LTException
     */
    public Integer getUserBaseCountByNickName(String nickName) throws LTException;

    /**
     * 查询redis中客户端所需要的行情ip 端口
     *
     * @return
     */
    List<QuotaHostBean> findClientQuotaHost();
}
