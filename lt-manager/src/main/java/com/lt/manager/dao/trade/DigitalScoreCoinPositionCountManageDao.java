package com.lt.manager.dao.trade;



import com.lt.manager.bean.trade.DigitalCoinPosition;

import java.io.Serializable;
import java.util.List;

/**
 * 差价合约类型商品额外字段配置
 *
 * @author yanzhenyu
 * @data 2017-10-21
 */
public interface DigitalScoreCoinPositionCountManageDao extends Serializable {
    public void insertCoinPosition(DigitalCoinPosition configVO);

    public DigitalCoinPosition selectCoinPosition(DigitalCoinPosition configVO);

    public DigitalCoinPosition selectCoinPositionForUpdate(DigitalCoinPosition configVO);

    public void updateCoinPosition(DigitalCoinPosition configVO);

    public void deleteCoinPosition(DigitalCoinPosition configVO);

    List<DigitalCoinPosition> getAllBuySellPosition();

}
