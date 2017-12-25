package com.lt.quota.core.dao;

import java.util.List;
import java.util.Map;


public interface IProductDao {

    List<Map<String, Object>> getCurrentProduct(Integer plate);

    List<Map<String, Object>> getPreProduct(Integer plate);

    List<Map<String, Object>> getExpireProduct(Integer plate);

    List<Map<String,Object>> getProductList(Long productTypeId);

    /**
     * 数字合约
     * @return
     */
    List<Map<String,Object>> getCoinProductList();
}
