package com.greate.community.service;

import com.greate.community.dao.DiscussPostMapper;
import com.greate.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostSerivce {

    @Autowired
    private DiscussPostMapper discussPostMapper;


    /**
     * 分页查询讨论帖信息
     *
     * @param userId 当传入的 userId = 0 时查找所有用户的帖子
     *               当传入的 userId != 0 时，查找该指定用户的帖子
     * @param offset 每页的起始索引
     * @param limit  每页显示多少条数据
     * @return
     */
    public List<DiscussPost> findDiscussPosts (int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     * 查询讨论贴的个数
     * @param userId 当传入的 userId = 0 时计算所有用户的帖子总数
     *               当传入的 userId ！= 0 时计算该指定用户的帖子总数
     * @return
     */
    public int findDiscussPostRows (int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
