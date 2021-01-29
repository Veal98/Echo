package com.greate.community;

import com.greate.community.dao.LoginTicketMapper;
import com.greate.community.dao.MessageMapper;
import com.greate.community.entity.LoginTicket;
import com.greate.community.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class MapperTests {

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    MessageMapper messageMapper;

    @Test
    public void testLoginTicketMapper() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    
    @Test
    public void testSelectTicketMapper() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testMessageMapper() {
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for(Message message: messages) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        List<Message> list = messageMapper.selectLetters("111_112", 0, 10);
        for(Message message: list) {
            System.out.println(message);
        }

        int i = messageMapper.selectLetterCount("111_112");
        System.out.println(i);

        System.out.println(messageMapper.selectLetterUnreadCount(131,"111_131"));
        System.out.println(messageMapper.selectLetterUnreadCount(131, null));

    }
}
