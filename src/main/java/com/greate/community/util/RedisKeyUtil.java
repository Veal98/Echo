package com.greate.community.util;

/**
 * 生成 Redis 的 key
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity"; // 实体的获赞
    private static final String PREFIX_USER_LIKE = "like:user"; // 用户的获赞
    private static final String PREFIX_FOLLOWER = "follower"; // 被关注（粉丝）
    private static final String PREFIX_FOLLOWEE = "followee"; // 关注的目标
    private static final String PREFIX_KAPTCHA = "kaptcha"; // 验证码
    private static final String PREFIX_TICKET = "ticket"; // 登录凭证
    private static final String PREFIX_USER = "user"; // 登录凭证
    private static final String PREFIX_UV = "uv"; // 独立访客
    private static final String PREFIX_DAU = "dau"; // 日活跃用户
    private static final String PREFIX_POST = "post"; // 用于统计帖子分数

    /**
     * 某个实体（帖子、评论/回复）的获赞
     * like:entity:entityType:entityId -> set(userId)
     * 谁给这个实体点了赞，就将这个用户的id存到这个实体对应的集合里
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 某个用户的获赞数量
     * like:user:userId -> int
     * @param userId 获赞用户的 id
     * @return
     */
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 某个用户关注的实体
     * followee:userId:entityType -> zset(entityId, now) 以当前关注的时间进行排序
     * @param userId
     * @param entityType 关注的实体类型
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体拥有的粉丝
     * follower:entityType:entityId -> zset(userId, now)
     * @param entityType
     * @param entityId
     * @return
     */
     public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
     }

    /**
     * 登录验证码（指定这个验证码是针对哪个用户的）
     * @param owner 用户进入登录页面的时候，由于此时用户还未登录，无法通过 id 标识用户
     *              随机生成一个字符串，短暂的存入 cookie，使用这个字符串来标识这个用户
     * @return
     */
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * 登陆凭证
     * @param ticket
     * @return
     */
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    /**
     * 用户信息
     * @param userId
     * @return
     */
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    /**
     * 单日 UV
     * @param date
     * @return
     */
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    /**
     * 区间 UV
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * 单日 DAU
     * @param date
     * @return
     */
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    /**
     * 区间 DAU
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * 帖子分数
     * @return
     */
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }
}
