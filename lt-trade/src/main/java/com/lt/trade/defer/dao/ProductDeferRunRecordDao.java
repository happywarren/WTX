package com.lt.trade.defer.dao;


import com.lt.vo.defer.ProductDeferRunRecordVo;

public interface ProductDeferRunRecordDao {

    public void insertProductDeferRunRecord(ProductDeferRunRecordVo productDeferRunRecord);

    public ProductDeferRunRecordVo queryProductDeferRunRecord(ProductDeferRunRecordVo productDeferRunRecord);

}
