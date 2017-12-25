package com.lt.manager.dao.user;

import java.util.List;

import com.lt.manager.bean.user.BankCard;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.vo.user.BankcardVo;
import org.apache.ibatis.annotations.Param;


/**
 * 用户查询管理dao
 *
 * @author licy
 */

public interface UserManageDao {

    /**
     * 用户查询接口
     *
     * @param param
     * @return
     */
    public List<UserBase> selectUserInfoPage(UserBaseInfoQueryVO param);

    /**
     * 黑名单用户查询接口
     *
     * @param param
     * @return
     */
    public List<UserBase> selectUserBlackListPage(UserBaseInfoQueryVO param);

    /**
     * 黑名单用户查询根据数量分页
     *
     * @param param
     * @return
     */
    public Integer selectUserBlackListCount(UserBaseInfoQueryVO param);

    /**
     * 用户查询根据数量分页
     *
     * @param param
     * @return
     */
    public Integer selectUserInfoCount(UserBaseInfoQueryVO param);

    /**
     * 用户信息更改
     *
     * @param param
     * @return
     */
    public void updateUserBase(UserBaseInfoQueryVO param);


    /**
     * 用户所属券商修改
     *
     * @return
     */
    public void updateUserInvestor(@Param("list") List<String> list, @Param("investorAccountId") String investorAccountId);


    /**
     * 用户信息删除
     *
     * @param param
     * @return
     */
    public void deleteUserBase(String id);

    /**
     * 获得用户对象
     */
    public UserBase selectUserBase(String id);

    /**
     * 获取用户详情
     *
     * @param bussinessInfo
     * @return
     */
    public UserBussinessInfo selectUserInfo(String userId);

    /**
     * 获得用户图片信息
     */
    public UserBase selectUserPic(String id);

    /**
     * 用户实名制
     *
     * @param param
     * @return
     */
    public List<UserBase> selectRealNameList(UserBaseInfoQueryVO param);

    /**
     * 用户实名制
     *
     * @param param
     * @return
     */
    public Integer selectRealNameListCount(UserBaseInfoQueryVO param);

    /**
     * 查询用户绑定银行卡信息
     *
     * @param userId
     * @return
     */
    public List<BankCard> selectUserBankInfoByUserId(String userId);
    
    /**
     * 查询券商列表
     * @param param
     * @return
     */
    public List<UserBase> selectInvestorList(UserBaseInfoQueryVO param);
    
    /**
     * 查询券商数量
     * @param param
     * @return
     */
    public Integer selectInvestorCount(UserBaseInfoQueryVO param);    
}
