package com.greate.community;

import com.greate.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("1912420914@qq.com", "TEST", "Welcome");

    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "Jack");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("1912420914@qq.com", "HTMLTEST", content);
    }

}
