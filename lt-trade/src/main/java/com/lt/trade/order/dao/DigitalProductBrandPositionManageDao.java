package com.lt.trade.order.dao;


import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import com.lt.trade.tradeserver.bean.DigitalProductBrandPosition;

import java.io.Serializable;
import java.util.List;

/**
 * 差价合约 品牌净持仓量统计表
 *
 * @author yanzhenyu
 * @data 2017-10-21
 */
public interface DigitalProductBrandPositionManageDao extends Serializable {
    public void insertCoinPosition(DigitalProductBrandPosition configVO);

    public DigitalProductBrandPosition selectCoinPosition(DigitalProductBrandPosition configVO);

    public DigitalProductBrandPosition selectCoinPositionForUpdate(DigitalProductBrandPosition configVO);

    public void updateCoinPosition(DigitalProductBrandPosition configVO);

    public void deleteCoinPosition(DigitalProductBrandPosition configVO);
}
