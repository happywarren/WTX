package com.lt.manager.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


public class SmtpAuthenticator extends Authenticator {

    private String username;

    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
