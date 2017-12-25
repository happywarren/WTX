package com.lt.trade.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志相关
 *
 * Created by sunch on 2016/11/8.
 */
public class LoggerTools {

    public static Logger getInstance(Class cls){
        return LoggerFactory.getLogger(cls);
    }

}
