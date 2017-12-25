package com.lt.quota.core.service.impl;

import com.lt.quota.core.dao.IProductDao;
import com.lt.quota.core.dao.IQuotaCoreConfigDao;
import com.lt.quota.core.model.QuotaCoreConfigModel;
import com.lt.quota.core.service.IQuotaCoreConfigService;
import com.lt.quota.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 创建：cndym
 * 时间：2017/5/2 11:39
 */

@Service
@Transactional
public class QuotaCoreConfigServiceImpl implements IQuotaCoreConfigService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IQuotaCoreConfigDao quotaCoreConfigDao;

    @Autowired
    private IProductDao productDao;

    @Override
    public List<QuotaCoreConfigModel> getQuotaCoreConfigModelByName(String name) {
        return quotaCoreConfigDao.getQuotaCoreConfigModelByName(name);
    }

    @Override
    public List<Map<String, Object>> preProductList(Integer plate) {

        List<Map<String, Object>> productList = productDao.getPreProduct(plate);

        if (!Utils.isNotEmpty(productList)) {
            return new ArrayList<Map<String, Object>>();
        }

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> entry : productList) {
            Integer id = (Integer) entry.get("id");
            String shortCode = entry.get("shortCode") + "";
            String exchangeCode = entry.get("exchangeCode") + "";
            String productCode = entry.get("productCode") + "";
            Map<String, Object> dataMap = new HashMap<String, Object>();

            dataMap.put("index", id);
            dataMap.put("exchangeNo", exchangeCode);
            dataMap.put("commodityNo", shortCode);
            dataMap.put("contractNo", productCode.replace(shortCode, ""));
            dataList.add(dataMap);
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> productList(Integer plate) {

        List<Map<String, Object>> productList = productDao.getCurrentProduct(plate);

        if (!Utils.isNotEmpty(productList)) {
            return new ArrayList<Map<String, Object>>();
        }

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> entry : productList) {
            Integer id = (Integer) entry.get("id");
            String shortCode = entry.get("shortCode") + "";
            String exchangeCode = entry.get("exchangeCode") + "";
            String productCode = entry.get("productCode") + "";
            Map<String, Object> dataMap = new HashMap<String, Object>();

            dataMap.put("index", id);
            dataMap.put("exchangeNo", exchangeCode);
            dataMap.put("commodityNo", shortCode);
            dataMap.put("contractNo", productCode.replace(shortCode, ""));
            dataList.add(dataMap);
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> expireProductList(Integer plate) {

        List<Map<String, Object>> productList = productDao.getExpireProduct(plate);

        if (!Utils.isNotEmpty(productList)) {
            return new ArrayList<Map<String, Object>>();
        }

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> entry : productList) {
            Integer id = (Integer) entry.get("id");
            String shortCode = entry.get("shortCode") + "";
            String exchangeCode = entry.get("exchangeCode") + "";
            String productCode = entry.get("productCode") + "";
            Map<String, Object> dataMap = new HashMap<String, Object>();

            dataMap.put("index", id);
            dataMap.put("exchangeNo", exchangeCode);
            dataMap.put("commodityNo", shortCode);
            dataMap.put("contractNo", productCode.replace(shortCode, ""));
            dataList.add(dataMap);
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getProductList(Long productTypeId) {
        return productDao.getProductList(productTypeId);
    }

    @Override
    public List<Map<String, Object>> getCoinProductList() {
        return productDao.getCoinProductList();
    }
}
