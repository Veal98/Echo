# Echo — 开源社区系统

---

## 🎁 从本项目你能学到什么

- 学会主流的 Java Web 开发技术和框架
- 积累一个真实的 Web 项目开发经验
- 掌握本项目中涉及的常见面试题的答题策略

## 💻 核心技术栈

 后端：

- Spring
- Spring Boot 2.4
- Spring MVC
- ORM：MyBatis
- 数据库：MySQL 5.7
- 缓存：Redis
- 消息队列：Kafka
- 搜索引擎：Elasticsearch
- 安全：Spring Security
- 监控：Spring Actuator
- 日志：SLF4J（日志接口） + Logback（日志实现）

前端：

- Thymeleaf
- Bootstrap 4.x
- Jquery
- Ajax

## 🔨 开发环境

- 构建工具：Apache Maven
- 集成开发工具：Intellij IDEA
- 数据库：MySQL 5.7、Redis
- 应用服务器：Apache Tomcat
- 版本控制工具：Git

## 🔔 功能列表

- [x] 注册
  - 用户注册
  - 发送激活邮件
  - 激活用户
- [x] 登录 | 登出
  - 用户登录（生成验证码）
  - 用户登出
- [x] 账号设置
  - 修改头像
  - 修改密码
- [x] 检查登录状态（禁止未登录用户访问需要登录权限的界面）
- [x] 帖子模块
  - 发布帖子（过滤敏感词）
  - 分页显示帖子
  - 查看帖子详情
- [x] 评论功能（过滤敏感词）
  - 发布对帖子的评论（过滤敏感词）
  - 分页显示评论
  - 发布对评论的回复（过滤敏感词）
- [x] 私信功能
  - 发送私信（过滤敏感词）
  - 发送列表（分页显示发出的私信）
  - 私信列表（分页显示收到的私信）
  - 私信详情
- [ ] 点赞功能
  - 点赞
  - 我收到的赞
- [ ] 关注功能
  - 关注
  - 取消关注
  - 关注列表
  - 粉丝列表
- [ ] 系统通知功能
  - 管理员发送系统通知
  - 用户接收系统通知
- [ ] 搜索功能
- [ ] 权限控制
- [ ] 管理员的置顶、加精、删除帖子功能
- [ ] 网站数据统计
- [ ] 热帖排行
- [ ] 文件上传
- [ ] 优化网站性能

## 🎨 界面展示

## 📜 数据库设计

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
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_type` int(11) DEFAULT NULL COMMENT '评论目标的类别：1 帖子；2 评论 ',
  `entity_id` int(11) DEFAULT NULL COMMENT '评论目标的 id',
  `target_id` int(11) DEFAULT NULL COMMENT '指明对谁进行评论',
  `content` text,
  `status` int(11) DEFAULT NULL COMMENT '状态：0 正常；1 禁用',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8;
```

登录凭证 `login_ticket`：

```sql
DROP TABLE IF EXISTS `login_ticket`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL COMMENT '凭证',
  `status` int(11) DEFAULT '0' COMMENT '凭证状态：0-有效; 1-无效;',
  `expired` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '凭证到期时间',
  PRIMARY KEY (`id`),
  KEY `index_ticket` (`ticket`(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

私信 `message`：

```sql
DROP TABLE IF EXISTS `message`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `conversation_id` varchar(45) NOT NULL,
  `content` text,
  `status` int(11) DEFAULT NULL COMMENT '0-未读;1-已读;2-删除;',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_from_id` (`from_id`),
  KEY `index_to_id` (`to_id`),
  KEY `index_conversation_id` (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## 📖 常见面试题

