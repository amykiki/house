package com.april.house.test;

import com.april.house.biz.service.MailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MailServiceTest extends BaseTest {

    @Autowired
    private MailService mailService;

    @Test
    public void testSendRedisMail() {
        String title = "hello - 002";
        String url = "第二封";
        String email = "abc@email.com";

        mailService.sendRedisMail(title, url, email);
    }

    @Test
    public void testGetRedisMail() {
        String email = "abc@email.com";
        List<String> emails = mailService.getRedisMails(email);
        emails.forEach(e -> System.out.println(e));
    }
}
