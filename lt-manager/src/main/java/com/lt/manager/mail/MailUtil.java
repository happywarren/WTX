package com.lt.manager.mail;

import org.apache.commons.mail.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 多线程发送邮件
 *
 * @author mcsong
 * @create 2017-09-28 17:26
 */
@Component
public class MailUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    @Autowired
    private MailConfig mailConfig;

    public void send(final Email email) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    email.setSSLOnConnect(true);
                    email.setStartTLSEnabled(true);
                    email.setStartTLSRequired(true);
                    email.setAuthentication(mailConfig.getUserName(), mailConfig.getPassword());
                    email.setHostName(mailConfig.getHost());
                    email.setSslSmtpPort(mailConfig.getPort());
                    email.setFrom(mailConfig.getUserName(), mailConfig.getFrom());
                    email.setCharset("UTF-8");
                    email.send();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
