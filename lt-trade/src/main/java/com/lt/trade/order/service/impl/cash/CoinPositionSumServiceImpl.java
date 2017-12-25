package com.lt.trade.order.service.impl.cash;

import com.lt.trade.order.dao.DigitalCoinPositionCountManageDao;
import com.lt.trade.order.service.ICoinPositionSumService;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description: 现金数字货币持仓量统计
 *
 * @author yanzhenyu
 * @date 2017/10/23
 */
@Service
public class CoinPositionSumServiceImpl implements ICoinPositionSumService {

    @Autowired
    DigitalCoinPositionCountManageDao positionCountManageDao;

    @Override
    public List<DigitalCoinPosition> getAllBuySellPosition() {
        return positionCountManageDao.getAllBuySellPosition();
    }

    @Override
    public DigitalCoinPosition getAllBuySellPositionByInvestorId(String productCode, String investorId) {
        return positionCountManageDao.getAllBuySellPositionByInvestorId(productCode, investorId);
    }
}