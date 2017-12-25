package com.lt.trade.defer.service.impl;


import com.lt.trade.defer.dao.ProductDeferRunRecordDao;
import com.lt.trade.defer.service.ProductDeferRunRecordService;
import com.lt.util.utils.StringTools;
import com.lt.vo.defer.ProductDeferRunRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductDeferRunRecordServiceImpl implements ProductDeferRunRecordService {

    @Autowired
    private ProductDeferRunRecordDao productDeferRunRecordDao;

    @Override
    public boolean doRun(String futureType, String nextOnePeriod,String day) {
        try {
            ProductDeferRunRecordVo productDeferRunRecord = new ProductDeferRunRecordVo();
            productDeferRunRecord.setFutureType(futureType);
            productDeferRunRecord.setNextOnePeriod(nextOnePeriod);
            productDeferRunRecord.setDay(day);
            productDeferRunRecord.setCreateTime(new Date());
            ProductDeferRunRecordVo sub = productDeferRunRecordDao.queryProductDeferRunRecord(productDeferRunRecord);
            if (StringTools.isNotEmpty(sub)){
                return false;
            }
            productDeferRunRecordDao.insertProductDeferRunRecord(productDeferRunRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
