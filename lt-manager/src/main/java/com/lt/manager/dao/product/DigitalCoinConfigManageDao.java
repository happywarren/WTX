package com.lt.manager.dao.product;

import com.lt.manager.bean.product.DigitalCoinConfigVO;
import org.apache.ibatis.annotations.Param;

/**
 * 差价合约类型商品额外字段配置
 *
 * @author yanzhenyu
 * @data 2017-10-21
 */
public interface DigitalCoinConfigManageDao {
    public void insertCoinConfig(DigitalCoinConfigVO configVO);

    public DigitalCoinConfigVO selectCoinConfig(DigitalCoinConfigVO configVO);

    public void updateCoinConfig(DigitalCoinConfigVO configVO);

    public void deleteCoinConfig(DigitalCoinConfigVO configVO);

    public void deleteCoinConfigBatch(@Param("productCodes") String productCodes);
}
