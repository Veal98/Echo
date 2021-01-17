# Greate Community

---

## 🥝 从本项目你能学到什么

- 👍 学会主流的 Java Web 开发技术和框架
- 👍 积累一个真实的 Web 项目开发经验
- 👍 掌握本项目中涉及的常见面试题的答题策略

## 🍉 技术栈

- Spring
- Spring Boot 2.x
- Spring MVC
- ORM：MyBatis
- 数据库：MySQL 5.7
- 缓存：Redis
- 消息队列：Kafka
- 搜索引擎：Elasticsearch
- 安全：Spring Security
- 监控：Spring Actuator
- 前端：Thymeleaf + Bootstrap
- 日志：SLF4J（日志接口） + Logback（日志实现）

## 🍋 开发环境

- 构建工具：Apache Maven
- 集成开发工具：Intellij IDEA
- 数据库：MySQL、Redis
- 应用服务器：Apache Tomcat
- 版本控制工具：Git

## 🍏 功能列表

## 🍑 界面展示

## 🍓 数据库文件

用户 `user`：

```sql
DROP TABLE IF EXISTS `user`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `salt` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0-普通用户; 1-超级管理员; 2-版主;',
  `status` int(11) DEFAULT NULL COMMENT '0-未激活; 1-已激活;',
  `activation_code` varchar(100) DEFAULT NULL,
  `header_url` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_username` (`username`(20)),
  KEY `index_email` (`email`(20))
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
```

讨论帖 `discuss_post`：

```sql
DROP TABLE IF EXISTS `discuss_post`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `discuss_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `type` int(11) DEFAULT NULL COMMENT '0-普通; 1-置顶;',
  `status` int(11) DEFAULT NULL COMMENT '0-正常; 1-精华; 2-拉黑;',
  `create_time` timestamp NULL DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `score` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

评论（回复）`comment`：

```sql
DROP TABLE IF EXISTS `comment`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_type` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `target_id` int(11) DEFAULT NULL,
  `content` text,
  `status` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

