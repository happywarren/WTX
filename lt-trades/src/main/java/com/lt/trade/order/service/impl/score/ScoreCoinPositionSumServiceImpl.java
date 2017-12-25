package com.lt.trade.order.service.impl.score;

import com.lt.trade.order.dao.DigitalScoreCoinPositionCountManageDao;
import com.lt.trade.order.service.ICoinPositionSumService;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 积分数字货币持仓量统计
 *
 * @author yanzhenyu
 * @date 2017/10/23
 */
@Service
public class ScoreCoinPositionSumServiceImpl implements ICoinPositionSumService {
    @Autowired
    DigitalScoreCoinPositionCountManageDao positionCountManageDao;

    @Override
    public List<DigitalCoinPosition> getAllBuySellPosition() {
        return positionCountManageDao.getAllBuySellPosition();
    }

    @Override
    public DigitalCoinPosition getAllBuySellPositionByInvestorId(String productCode,String investorId) {
        return positionCountManageDao.getAllBuySellPositionByInvestorId(productCode,investorId);
    }
}