package com.greate.community.dao;

import com.greate.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 根据评论目标（类别、id）对评论进行分页查询
     * @param entityType 评论目标的类别
     * @param entityId 评论目标的 id
     * @param offset 每页的起始索引
     * @param limit 每页显示多少条数据
     * @return
     */
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查询评论的数量
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    int insertComment(Comment comment);

}
