package com.lt.manager.mail;

import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailEngine {

    @Autowired
    private MailUtil mailUtil;

    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
    }

    public void sendText(String email, String content) throws Exception {
        SimpleEmail simpleEmail = new SimpleEmail();
        simpleEmail.addTo(email);
        simpleEmail.setSubject("用户登录验证码");
        simpleEmail.setMsg(content);
        mailUtil.send(simpleEmail);
    }

    public boolean sendMail(String email, String content) {
        try {
            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.addTo(email);
            htmlEmail.setSubject("用户登录验证码");
            StringBuilder htmlText = new StringBuilder();
            htmlText.append("<html>");
            htmlText.append("<head>").append("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\">").append("</head>");
            htmlText.append("<body>").append(content).append("</body>");
            htmlText.append("</html>");
            htmlEmail.setHtmlMsg(htmlText.toString());
            mailUtil.send(htmlEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
