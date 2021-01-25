package com.greate.community.util;

/**
 * 生成 Redis 的 key
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";

    // 某个实体（帖子、评论/回复）的赞
    // like:entity:entityType:entityId -> set(userId)
    // 谁给这个实体点了赞，就将这个用户的id存到这个实体对应的集合里
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞(被赞)
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_ENTITY_LIKE + SPLIT + userId;
    }

}
