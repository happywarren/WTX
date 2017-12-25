package com.lt.quota.core.service;


import com.lt.quota.core.model.QuotaCoreConfigModel;

import java.util.List;
import java.util.Map;

/**
 * 创建：cndym
 * 时间：2017/5/2 11:39
 */
public interface IQuotaCoreConfigService {

    List<QuotaCoreConfigModel> getQuotaCoreConfigModelByName(String name);

    public List<Map<String, Object>> preProductList(Integer plate);

    public List<Map<String, Object>> productList(Integer plate);

    public List<Map<String, Object>> expireProductList(Integer plate);

    List<Map<String,Object>> getProductList(Long productTypeId);

    List<Map<String,Object>> getCoinProductList();
}
