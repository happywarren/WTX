package com.lt.trade.order.service;

import com.lt.trade.tradeserver.bean.DigitalCoinPosition;

import java.util.List;
import java.util.Map;

/**
 * Description: 统计系统，看多，看空持仓数
 *
 * @author yanzhenyu
 * @date 2017/10/23
 */
public interface ICoinPositionSumService {
    List<DigitalCoinPosition> getAllBuySellPosition();
    DigitalCoinPosition getAllBuySellPositionByInvestorId(String productCode,String investorId);
}