/*
 * PROJECT NAME: lt-fund
 * PACKAGE NAME: com.lt.fund.dao
 * FILE    NAME: FundFlowCashDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.fund.dao;

import com.lt.model.fund.FundFlow;
import com.lt.vo.fund.FundFlowVo;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO 现金流水数据接口
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年11月30日 下午5:13:52
 */
public interface FundFlowCashDao extends Serializable {

    /**
     * TODO 添加现金流水
     *
     * @param fundFlow
     * @author XieZhibing
     * @date 2016年12月6日 下午9:45:15
     */
    public void addFundFlowCash(FundFlow fundFlow);

    /**
     * TODO 批量添加现金流水
     *
     * @param flowList
     * @author XieZhibing
     * @date 2016年12月6日 下午9:50:01
     */
    public void addFundFlowCashList(@Param("flowList") List<FundFlow> flowList);


    /**
     * 查询订单流水数量
     *
     * @param orderId
     * @param userId
     * @param List<secondOptcode> 二级资金业务码集合
     * @return
     */
    public Double queryFundFlowCashSum(@Param("orderId") String orderId, @Param("userId") String userId, @Param("secondOptcodeList") List<String> secondOptcodeList);

    /**
     * 查询用户资金明细
     *
     * @param map
     * @return
     */
    public List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map);

    /**
     * 获取用户单日充值的总额
     *
     * @param userId
     * @return
     * @throws
     * @return: Double
     * @author yuanxin
     * @Date 2017年4月10日 上午11:36:51
     */
    public Double getUserDailyRechargeTopAmt(@Param("userId") String userId);

    /**
     * 获取用户剩余平安今年可充值数额
     *
     * @param userId
     * @return
     * @throws
     * @return: Double
     * @author yuanxin
     * @Date 2017年5月8日 下午7:16:33
     */
    public Double getPingAnRechargeAmt(@Param("userId") String userId);

    /**
     * 修改现金流(根据id 修改备注 一二三级业务码)
     *
     * @param fundFlow
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2017年5月23日 上午10:22:10
     */
    public void updateFundCashFlow(FundFlow fundFlow);

    /**
     * 根据关联id 查询用户现金流信息（现金流ID，现金流三级业务码）
     *
     * @param id
     * @return
     * @throws
     * @return: List<fundFlow>
     * @author yuanxin
     * @Date 2017年5月23日 上午10:46:46
     */
    public List<FundFlow> qryFundCashFlowByExternId(String id);

    /**
     * 根据订单查询冻结保证金
     *
     * @param orderId
     * @return
     */
    public String selectHoldFundByOrderId(String orderId);

    /**
     * 充值资金流水
     * <p>
     * 根据id和thirdOptCode获取充值资金流水记录
     *
     * @param id
     * @param thirdOptCode
     * @return
     */
    public FundFlow getFundFlow(@Param("id") String id, @Param("thirdOptCode") String thirdOptCode);

    /**
     * 根据订单查询订单资金明细
     * @return
     */
    public List<FundFlowVo> findFundFlowListByOrder(Map<String, Object> map);
}
