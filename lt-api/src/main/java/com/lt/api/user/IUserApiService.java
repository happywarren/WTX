package com.lt.api.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.model.sys.QuotaHostBean;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTException;
import com.lt.vo.user.UserProductSelectListVo;

public interface IUserApiService {

    /**
     * 通过userId查询用户信息
     */
    public UserBaseInfo findUserByUserId(String userId);

    /**
     * @param request
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年11月29日 下午3:47:58
     */
    public void sendRegisterMessage(String tele, String ip, String sms_type, String brandCode) throws LTException;


    /**
     * 校验手机验证码 品牌字段筛选
     */
    public void checkRegCode(String tele, String brandCode, String smsType, String validCode) throws LTException;

    /**
     * 校验手机验证码
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2016年11月30日 下午8:17:05
     */
    public void checkRegCode(String tele, String smsType, String validCode) throws LTException;

    /**
     * 用户注册
     *
     * @param baseInfo 用户对象
     * @return
     * @throws
     * @return: Response
     * @author yuanxin
     * @Date 2016年11月30日 下午9:02:16
     */
    public HashMap<String, Object> userRegister(UserBaseInfo baseInfo, String reg_code) throws LTException;

    /**
     * 用户登录
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月1日 下午2:57:05
     */
    public HashMap<String, Object> userLogin(UserBaseInfo baseInfo) throws LTException;

    /**
     * 修改用户昵称和签名
     *
     * @param baseInfo
     * @param type
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月2日 下午2:10:53
     */
    public UserBaseInfo updateUserNickAndSign(UserBaseInfo baseInfo, int type) throws LTException;

    /**
     * 修改用户密码
     *
     * @param baseInfo
     * @param oldPwd
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月2日 下午8:49:23
     */
    public void updateUserPwd(UserBaseInfo baseInfo, String oldPwd) throws LTException;

    /**
     * 找回用户密码
     *
     * @param baseInfo
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2016年12月3日 下午3:49:51
     */
    public HashMap<String, Object> findUserPassword(UserBaseInfo baseInfo, String auth_code) throws LTException;

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

    /**
     * 自选商品删除
     *
     * @param userId
     */
    void deleteProductOptional(String userId, String productCode) throws LTException;

    /**
     * 自选商品查询
     *
     * @param userId
     */
    List<UserProductSelectListVo> selectProductOptional(String userId) throws LTException;


    /**
     * 自选商品查询
     *
     * @param userId
     * @clientVersion 客户端版本号
     */
    List<UserProductSelectListVo> selectProductOptional(String userId, String clientVersion) throws LTException;

    /**
     * 自选商品修改
     *
     * @param userId
     * @param productIds
     */
    void updateProductOptional(String userId, String productIds) throws LTException;

    /**
     * 自选商品新增
     *
     * @param userId
     * @param productIds
     */
    void addProductOptional(String userId, String productIds) throws LTException;

    /**
     * 新增自选商品
     *
     * @param userId
     * @param productCode
     */
    public void addProductOptionalByCode(String userId, String productCode);

    int checkUserPasswdCount(String token, String passwd) throws LTException;

    /**
     * 检查用户是否登陆
     *
     * @param token
     * @throws LTException
     */
    void checkToken(String token) throws LTException;


    /**
     * 查询行情接口  获取host port
     *
     * @return
     */
    List<QuotaHostBean> findClientQuotaHost();

    /**
     * 获取渠道号
     *
     * @param code       渠道号
     * @param deviceType 客户端类型
     * @return
     */
    String getChannelCode(String code, String deviceType);


    /**
     * 用户可以获取 btc
     */
    void userAccessBtc(String userId);

    /**
     *  查询用户可以获取 btc
     */
    boolean findUserAccessBtc(String userId);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserBaseInfo findUserInfo(String userId);

    /**
     * 保存crash日志
     * @param crashMap
     */
    void saveCrashLog(Map<String,Object> crashMap);

    /**
     * 实名认证身份证姓名是否可以修改
     * @return
     */
    String isOpen();

    /**
     * 获取直播链接
     * @return
     */
    String getLiveUrl();


}
