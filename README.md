# Echo — 开源社区系统

---

## 🎁 从本项目你能学到什么

- 学会主流的 Java Web 开发技术和框架
- 积累一个真实的 Web 项目开发经验
- 掌握本项目中涉及的常见面试题的答题策略

## 💻 核心技术栈

 后端：

- Spring
- Spring Boot 2.1.5 RELEASE
- Spring MVC
- ORM：MyBatis
- 数据库：MySQL 5.7
- 缓存：Redis
- 消息队列：Kafka 2.13-2.7.0
- 搜索引擎：Elasticsearch 6.4.3
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

- [x] 注册（MySQL）
  - 用户注册成功，将用户信息存入 MySQL，但此时该用户状态为未激活
  - 向用户发送激活邮件，用户点击链接则激活账号
  
- [x] 登录 | 登出（MySQL、Redis）
  - 进入登录界面，动态生成验证码，并将验证码短暂存入 Redis（60 秒）
  
  - 用户登录成功（验证用户名、密码、验证码），生成登录凭证且设置状态为有效，并将登录凭证存入 Redis
  
    注意：登录凭证存在有效期，在所有的请求执行之前，都会检查凭证是否有效和是否过期，只要该用户的凭证有效并在有效期时间内，本次请求就会一直持有该用户信息（使用 ThreadLocal 持有用户信息）
  
  - 勾选记住我，则延长登录凭证有效时间
  
  - 用户登录成功，将用户信息短暂存入 Redis（1 小时）
  
  - 用户登出，将凭证状态设为无效，并更新 Redis 中该用户的登录凭证信息
  
- [x] 账号设置（MySQL）
  - 修改头像
  - 修改密码
  
- [x] 检查登录状态（禁止未登录用户访问需要登录权限的界面，后续会使用 Spring Security 接管）

- [x] 帖子模块（MySQL）
  - 发布帖子（过滤敏感词），将其存入 MySQL
  - 分页显示所有的帖子
  - 查看帖子详情
  
- [x] 评论模块（MySQL）
  - 发布对帖子的评论（过滤敏感词），将其存入 MySQL
  - 分页显示评论
  - 发布对评论的回复（过滤敏感词）
  
- [x] 私信模块（MySQL）
  - 发送私信（过滤敏感词）
  - 私信列表
    - 查询当前用户的会话列表
    - 每个会话只显示一条最新的私信
    - 支持分页显示
  - 私信详情
    - 查询某个会话所包含的所有私信
    - 访问私信详情时，将显示的私信设为已读状态
    - 支持分页显示
  
- [x] 统一处理异常（404、500）
  - 普通请求异常
  - 异步请求异常
  
- [x] 统一记录日志

- [x] 点赞模块（Redis）
  - 点赞
  - 获赞
  
- [x] 关注模块（Redis）
  
  - 关注功能
  - 取消关注功能
  - 统计用户的关注数和粉丝数
  - 关注列表（查询某个用户关注的人），支持分页
  - 粉丝列表（查询某个用户的粉丝），支持分页
  
- [x] 系统通知模块（Kafka）
  
  - 通知列表
    - 显示评论、点赞、关注三种类型的通知
  - 通知详情
    - 分页显示某一类主题所包含的通知
    - 进入某种类型的系统通知详情，则将该页的所有未读的系统通知状态设置为已读
  - 未读数量
    - 分别显示每种类型的系统通知的未读数量
    - 显示所有系统通知的未读数量
  
    - 导航栏显示所有消息的未读数量（未读私信 + 未读系统通知）
  
- [x] 搜索模块（Elasticsearch + Kafka）

  - 发布事件
    - 发布帖子时，通过消息队列将帖子异步地提交到 Elasticsearch 服务器
    - 为帖子增加评论时，通过消息队列将帖子异步地提交到 Elasticsearch 服务器
  - 搜索服务
    - 从 Elasticsearch 服务器搜索帖子
    - 从 Elasticsearch 服务器删除帖子（当帖子从数据库中被删除时）
  - 显示搜索结果

- [ ] 权限控制

- [ ] 管理员模块
  - 置顶帖子
  - 加精帖子
  - 删除帖子
  
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

