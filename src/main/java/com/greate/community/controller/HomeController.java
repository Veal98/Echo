package com.greate.community.controller;

import com.greate.community.entity.DiscussPost;
import com.greate.community.entity.Page;
import com.greate.community.entity.User;
import com.greate.community.service.DiscussPostSerivce;
import com.greate.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理首页逻辑
 */
@Controller
public class HomeController {

    @Autowired
    private DiscussPostSerivce discussPostSerivce;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page) {
        // 获取总页数
        page.setRows(discussPostSerivce.findDiscussPostRows(0));
        page.setPath("/index");

        // 分页查询
        List<DiscussPost> list = discussPostSerivce.findDiscussPosts(0, page.getOffset(), page.getLimit());
        // 封装帖子和该帖子对应的用户信息
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "index";
    }

}
