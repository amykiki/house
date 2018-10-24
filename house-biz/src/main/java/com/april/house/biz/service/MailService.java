package com.april.house.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    //开发环境不真实发邮件
    /*@Autowired
    private JavaMailSender mailSender;*/

    @Value("${spring.mail.username}")
    private String from;

    @Value(("${domain.name}"))
    private String domainName;

    //发送邮件
    @Async
    public void sendMail(String title, String url, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setTo(email);
        message.setText(url);
//        mailSender.send(message);
    }

    @Async
    public void registerNotify(String email, String randomKey) {
        String url = "http://" + domainName + "/accounts/verify?key=" + randomKey;
        sendMail("房产平台激活邮件", url, email);
    }

    @Async
    public void resetNotify(String email, String randomKey) {
        String url = "http://" + domainName + "/accounts/reset?key=" + randomKey;
        sendMail("房产平台密码重置邮件", url, email);
    }
}
