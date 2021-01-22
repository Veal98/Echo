package com.greate.community.controller;

import com.greate.community.entity.DiscussPost;
import com.greate.community.entity.User;
import com.greate.community.service.DiscussPostSerivce;
import com.greate.community.service.UserService;
import com.greate.community.util.CommunityUtil;
import com.greate.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostSerivce discussPostSerivce;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 添加帖子（发帖）
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "您还未登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostSerivce.addDiscussPost(discussPost);

        // 报错的情况将来会统一处理
        return CommunityUtil.getJSONString(0, "发布成功");
    }

    /**
     * 进入帖子详情页
     * @param discussPostId
     * @param model
     * @return
     */
    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        // 帖子
        DiscussPost discussPost = discussPostSerivce.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPost);
        // 作者
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);

        return "/site/discuss-detail";

    }


}
