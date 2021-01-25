package com.greate.community.controller;

import com.greate.community.entity.DiscussPost;
import com.greate.community.entity.Page;
import com.greate.community.entity.User;
import com.greate.community.service.DiscussPostSerivce;
import com.greate.community.service.LikeService;
import com.greate.community.service.UserService;
import com.greate.community.util.CommunityConstant;
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
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostSerivce discussPostSerivce;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     * 进入首页
     * @param model
     * @param page
     * @return
     */
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
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "index";
    }

    /**
     * 进入 500 错误界面
     * @return
     */
    @GetMapping("/error")
    public String getErrorPage() {
        return "/error/500";
    }

}
