package com.lt.manager.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mcsong
 * @create 2017-10-25 13:49
 */
@Component("mailConfig")
public class MailConfig {

    @Value("${MAIL.HOST}")
    private String host;
    @Value("${MAIL.PORT}")
    private String port;
    @Value("${MAIL.DEFAULT.FROM}")
    private String from;
    @Value("${MAIL.USERNAME}")
    private String userName;
    @Value("${MAIL.PASSWORD}")
    private String password;

    public MailConfig() {

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
