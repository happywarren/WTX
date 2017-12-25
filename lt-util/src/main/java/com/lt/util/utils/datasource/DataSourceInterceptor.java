package com.lt.util.utils.datasource;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author jwb
 * @Description:
 * @Date: Create in 16:08 2017/10/17
 */
@Component
public class DataSourceInterceptor {

    private static Logger logger = LoggerFactory.getLogger(DataSourceInterceptor.class);

    public void setDataSourceDefault(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        logger.info("after-methodName={}",methodName);
        DataSourceContextHolder.clearDbType();
    }

    public void setDataSourceScore(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        logger.info("before-methodName={}",methodName);
        DataSourceContextHolder.setDbType("dataSourceScore");
    }
}
