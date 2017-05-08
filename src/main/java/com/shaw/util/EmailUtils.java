package com.shaw.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by imn5100 on 2017/5/3.
 */
public class EmailUtils {
    private static Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    private static final String MAIL_USERNAME = PropertiesUtil.getConfiguration().getString("email.username");
    private static final String MAIL_PASSWORD = PropertiesUtil.getConfiguration().getString("email.password");

    public static String sendEmail(String subject, String msg, String... tos) {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.163.com");
            email.setSmtpPort(995);
            email.setAuthenticator(new DefaultAuthenticator(MAIL_USERNAME, DesUtils.getDefaultInstance().decrypt((MAIL_PASSWORD))));
            email.setSSLOnConnect(true);
            email.setFrom(MAIL_USERNAME + "@163.com");
            email.setSubject(subject);
            email.setMsg(msg);
            email.addTo(tos);
            return email.send();
        } catch (Exception e) {
            logger.error("Send Email Fail:", e);
            return null;
        }
    }

}
