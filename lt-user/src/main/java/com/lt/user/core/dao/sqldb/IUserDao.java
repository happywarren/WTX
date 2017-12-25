package com.lt.user.core.dao.sqldb;

import com.lt.model.user.UserBaseInfo;

import java.util.List;

public interface IUserDao {

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
     * 统计手机号在日期区接收某类型短信的数量
     *
     * @param tele      电话号码
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param sms_type  短信类型
     * @return
     * @throws
     * @return: Integer
     * @author yuanxin
     * @Date 2016年11月29日 下午2:48:16
     */
    public Integer getTeleMsgCount(String tele, String beginDate, String endDate, String sms_type,String brandId);

    /**
     * 获取短信内容（暂时用于验证）
     *
     * @param tele      电话
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param sms_type  短信类型
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年11月30日 下午8:37:37
     */
    public String getAuthCodeByBrand(String tele, String brandId, String beginDate, String endDate, String sms_type);

    /**
     * 获取短信内容（暂时用于验证）
     *
     * @param tele      电话
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param sms_type  短信类型
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年11月30日 下午8:37:37
     */
    public String getAuthCode(String tele, String beginDate, String endDate, String sms_type);

    /**
     * 获取用户基本信息
     *
     * @param baseInfo 目前支持 昵称，id，电话，状态,手机状态
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年11月30日 下午9:12:44
     */
    public List<UserBaseInfo> getBaseUserInfo(UserBaseInfo baseInfo);

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
    public UserBaseInfo getBaseUserInfoNoCancel(UserBaseInfo baseInfo);

    /**
     * 插入用户基本信息
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: int
     * @author yuanxin
     * @Date 2016年11月30日 下午9:47:44
     */
    public int insertUserBaseInfo(UserBaseInfo baseInfo);

    /**
     * 修改用户基本信息
     *
     * @param baseInfo 支持修改 昵称，密码，个性签名，上次登录时间，上次登录设备，头像
     * @return
     * @throws
     * @return: int
     * @author yuanxin
     * @Date 2016年12月1日 下午4:38:01
     */
    public int updateUserBaseInfo(UserBaseInfo baseInfo);

    /**
     * 查询用户实名认证状态
     *
     * @param userId
     * @return
     */
    public Integer selectCertificationStatus(String userId);

    /**
     * 查询用户id
     *
     * @param id
     * @return
     */
    public String selectUserId(String id);

    /**
     * 查询昵称的数量
     *
     * @param nickName
     * @return
     */
    public Integer selectUserBaseCountByNickName(String nickName);
}
