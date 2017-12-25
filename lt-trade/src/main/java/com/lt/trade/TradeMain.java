package com.lt.trade;

import com.lt.util.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by sunch on 2016/11/7.
 */
public class TradeMain {

    private static Logger LOGGER = LoggerFactory.getLogger(TradeMain.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:/config/app*.xml"});
        context.start();
        SpringUtils.applicationContext =  context;
        synchronized (TradeMain.class) {
            while (context.isActive()) {
                try {
                	TradeMain.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

}
