package com.greate.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装事件（用于系统通知）
 */
public class Event {

    private String topic; // 事件类型
    private int userId; // 事件由谁触发
    private int entityType; // 实体类型
    private int entityId; // 实体 id
    private int entityUserId; // 实体的作者(该通知发送给他）
    private Map<String, Object> data = new HashMap<>(); // 存储未来可能需要用到的数据

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
