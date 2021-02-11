package com.greate.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.greate.community.entity.Message;
import com.greate.community.entity.Page;
import com.greate.community.entity.User;
import com.greate.community.service.MessageService;
import com.greate.community.service.UserService;
import com.greate.community.util.CommunityConstant;
import com.greate.community.util.CommunityUtil;
import com.greate.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import java.util.*;

/**
 * 私信/系统通知
 */
@Controller
public class MessageController implements CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 私信列表
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        // Integer.valueOf("abc"); // 测试统一异常处理（普通请求）
        User user = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCout(user.getId()));
        // 私信列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message); // 私信
                map.put("letterCount", messageService.findLetterCount(
                        message.getConversationId())); // 私信数量
                map.put("unreadCount", messageService.findLetterUnreadCount(
                        user.getId(), message.getConversationId())); // 未读私信数量
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId)); // 私信对方

                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        // 查询当前用户的所有未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnReadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/letter";

    }

    /**
     * 私信详情页
     * @param conversationId
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());

        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));

        // 将私信列表中的未读消息改为已读
        List<Integer> ids = getUnreadLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";

    }

    /**
     * 获取私信对方对象
     * @param conversationId
     * @return
     */
    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        }
        else {
            return userService.findUserById(id0);
        }
    }

    /**
     * 获取当前登录用户未读私信的 id
     * @param letterList
     * @return
     */
    private List<Integer> getUnreadLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                // 当前用户是私信的接收者且该私信处于未读状态
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    /**
     * 发送私信
     * @param toName 收信人 username
     * @param content 内容
     * @return
     */
    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content) {
        // Integer.valueOf("abc"); // 测试统一异常处理（异步请求）
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }
        else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0); // 默认就是 0 未读，可不写
        message.setCreateTime(new Date());

        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);

    }

    /**
     * 通知列表（只显示最新一条消息）
     * @param model
     * @return
     */
    @GetMapping("/notice/list")
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMNET);
        // 封装通知需要的各种数据
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMNET);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(user.getId(), TOPIC_COMMNET);
            messageVO.put("unread", unread);

            model.addAttribute("commentNotice", messageVO);
        }

        // 查询点赞类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(user.getId(), TOPIC_LIKE);
            messageVO.put("unread", unread);

            model.addAttribute("likeNotice", messageVO);
        }

        // 查询关注类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("unread", unread);

            model.addAttribute("followNotice", messageVO);
        }

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnReadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    /**
     * 查询某个主题所包含的通知列表
     * @param topic
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/notice/detail/{topic}")
    public String getNoticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = messageService.findNotices(user.getId(), topic,page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 发送系统通知的作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getUnreadLetterIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }
}
