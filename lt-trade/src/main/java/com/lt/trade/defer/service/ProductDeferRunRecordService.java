package com.lt.trade.defer.service;


public interface ProductDeferRunRecordService {

    public boolean doRun(String futureType, String nextOnePeriod, String day);

}
