package com.lt.util.utils.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * @Author jwb
 * @Description:
 * @Date: Create in 15:40 2017/10/17
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSocure = DataSourceContextHolder.getDbType();
        logger.info("dataSocure={}", dataSocure);
        return dataSocure;
        //return DataSourceContextHolder.getDbType();
    }
}
