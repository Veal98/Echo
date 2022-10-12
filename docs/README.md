## 📚 项目简介

Echo 是一套前后端不分离的开源社区系统，基于目前主流 Java Web 技术栈（SpringBoot + MyBatis + MySQL + Redis + Kafka + Elasticsearch + Spring Security + ...），并提供详细的开发文档和配套教程。包含帖子、评论、私信、系统通知、点赞、关注、搜索、用户设置、数据统计等模块。

**源码链接**：已托管在 Github 和 Gitee：

- Gitee：[https://gitee.com/veal98/Echo](https://gitee.com/veal98/Echo)（Gitee 官方推荐项目）
- Github：[https://github.com/Veal98/Echo](https://github.com/Veal98/Echo)

**文档地址**：文档通过 <u>Docsify + Github/Gitee Pages</u> 生成

- Gitee Pages：[https://veal98.gitee.io/echo](https://veal98.gitee.io/echo)
- Github Pages：[https://veal98.github.io/Echo](https://veal98.github.io/Echo)

## 📖 配套教程

想要自己从零开始实现这个项目或者深入理解的小伙伴，可以扫描下方二维码关注公众号『**飞天小牛肉**』回复 `Echo` 免费获取配套教程。本套教程不仅会详细解释本项目涉及的各大技术点，还会汇总相关的常见面试题，目前尚在更新中。

<img width="220px" src="https://gitee.com/veal98/images/raw/master/img/20210204145531.png"  />

配套教程部分截图：

![](https://gitee.com/veal98/images/raw/master/img/20210930110659.png)



## 💻 核心技术栈

后端：

- Spring
- Spring Boot 2.1.5 RELEASE
- Spring MVC
- ORM：MyBatis
- 数据库：MySQL 5.7
- 分布式缓存：Redis
- 本地缓存：Caffeine
- 消息队列：Kafka 2.13-2.7.0
- 搜索引擎：Elasticsearch 6.4.3
- 安全：Spring Security
- 邮件任务：Spring Mail
- 分布式定时任务：Spring Quartz
- 日志：SLF4J（日志接口） + Logback（日志实现）

前端：

- Thymeleaf
- Bootstrap 4.x
- Jquery
- Ajax

## 🔨 开发环境

- 操作系统：Windows 10
- 构建工具：Apache Maven
- 集成开发工具：Intellij IDEA
- 应用服务器：Apache Tomcat
- 接口测试工具：Postman
- 压力测试工具：Apache JMeter
- 版本控制工具：Git
- Java 版本：8

## 🎀 界面展示

首页：

![](https://gitee.com/veal98/images/raw/master/img/20210211205641.png)

登录页：

![](https://gitee.com/veal98/images/raw/master/img/20210211205558.png)

密码重置页：

![](https://gitee.com/veal98/images/raw/master/img/20210709003428.png)

帖子发布页（支持 MarkDown）：

![](https://gitee.com/veal98/images/raw/master/img/20210512101041.png)

帖子详情页（MarkDown 渲染）：

![](https://gitee.com/veal98/images/raw/master/img/20210512101202.png)

帖子详情页：

![](https://gitee.com/veal98/images/raw/master/img/20210211205741.png)



个人主页：

![](https://gitee.com/veal98/images/raw/master/img/20210211205820.png)

朋友私信页：

![](https://gitee.com/veal98/images/raw/master/img/20210211205857.png)

私信详情页：

![](https://gitee.com/veal98/images/raw/master/img/20210211205948.png)

系统通知页：

![](https://gitee.com/veal98/images/raw/master/img/20210211210122.png)

通知详情页：

![](https://gitee.com/veal98/images/raw/master/img/20210211210152.png)

账号设置页：

![](https://gitee.com/veal98/images/raw/master/img/20210211210238.png)

数据统计页：

![](https://gitee.com/veal98/images/raw/master/img/20210211210323.png)

搜索详情页：

![](https://gitee.com/veal98/images/raw/master/img/20210211210531.png)

## 🎨 功能列表

![](https://gitee.com/veal98/images/raw/master/img/20210208222403.png)

- [x] **注册**
- [x] **登录 | 登出**
  - 动态生成验证码
  - 记住我
- [x] **账号设置**
  - 修改头像
  - 修改密码
- [x] **过滤敏感词**
  - 前缀树
- [x] **帖子模块**
  - 发布帖子（过滤敏感词）
  - 分页显示所有的帖子
    - 支持按照 “发帖时间” 显示
    - 支持按照 “热度排行” 显示（Spring Quartz）
  - 查看帖子详情
  - 权限管理（Spring Security + Thymeleaf Security）
    - 未登录用户无法发帖
    - “版主” 可以看到帖子的置顶和加精按钮并执行相应操作
    - “管理员” 可以看到帖子的删除按钮并执行相应操作
    - “普通用户” 无法看到帖子的置顶、加精、删除按钮，也无法执行相应操作
- [x] **评论模块**
  - 发布对帖子的评论（过滤敏感词）
  - 分页显示评论
  - 发布对评论的回复（过滤敏感词）
  - 权限管理（Spring Security）
    - 未登录用户无法使用评论功能
- [x] **私信模块**
  - 发送私信（过滤敏感词）
  - 私信列表
    - 查询当前用户的会话列表
    - 每个会话只显示一条最新的私信
    - 支持分页显示
  - 私信详情
    - 查询某个会话所包含的所有私信
    - 访问私信详情时，将显示的私信设为已读状态
    - 支持分页显示
  - 权限管理（Spring Security）
    - 未登录用户无法使用私信功能
- [x] **统一处理 404 / 500 异常**
  - 普通请求异常
  - 异步请求异常
- [x] **统一记录日志**
- [x] **点赞模块**
  - 支持对帖子、评论/回复点赞
  - 第 1 次点赞，第 2 次取消点赞
  - 首页统计帖子的点赞数量
  - 详情页统计帖子和评论/回复的点赞数量
  - 详情页显示当前登录用户的点赞状态（赞过了则显示已赞）
  - 统计我的获赞数量
  - 权限管理（Spring Security）
    - 未登录用户无法使用点赞相关功能
- [x] **关注模块**
  - 关注功能
  - 取消关注功能
  - 统计用户的关注数和粉丝数
  - 我的关注列表（查询某个用户关注的人），支持分页
  - 我的粉丝列表（查询某个用户的粉丝），支持分页
  - 权限管理（Spring Security）
    - 未登录用户无法使用关注相关功能
- [x] **系统通知模块**
  - 通知列表
    - 显示评论、点赞、关注三种类型的通知
  - 通知详情
    - 分页显示某一类主题所包含的通知
    - 进入某种类型的系统通知详情，则将该页的所有未读的系统通知状态设置为已读
  - 未读数量
    - 分别显示每种类型的系统通知的未读数量
    - 显示所有系统通知的未读数量
  - 导航栏显示所有消息的未读数量（未读私信 + 未读系统通知）
  - 权限管理（Spring Security）
    - 未登录用户无法使用系统通知功能
- [x] **搜索模块**
- [x] **网站数据统计**（管理员专属）
  - 独立访客 UV
    - 支持单日查询和区间日期查询
  - 日活跃用户 DAU
    - 支持单日查询和区间日期查询
  - 权限管理（Spring Security）
    - 只有管理员可以查看网站数据统计
- [x] **优化网站性能**
  - 处理每次请求时，都要通过拦截器根据登录凭证查询用户信息，访问的频率非常高。因此将已成功登录的用户信息在缓存 Redis 中保存一段时间，查询用户信息的时候优先从缓存中取值；若缓存中没有该用户信息，则将其存入缓存；用户信息变更时清除对应的缓存数据；
  - 引入本地缓存 Caffeine，缓存热帖列表和帖子的总数，避免缓存雪崩（这里面还能再加一层二级缓存 Redis）。

## 🔐 待实现及优化

以下是我觉得本项目还可以添加的功能，同样欢迎各位小伙伴提 issue 指出还可以增加哪些功能，或者直接提 PR 实现该功能：

- [x] 发帖支持 Markdown 格式
- [x] 忘记密码（发送邮件找回密码）（Implemented by [逆光影者 (cpfree) ](https://gitee.com/cpfree)）
- [ ] 查询我的点赞
- [x] 管理员对帖子的二次点击取消置顶功能（Partially Implement by [Max (Lyingfromyou)](https://gitee.com/Lyingfromyou)）
- [ ] 管理员对已删除帖子的恢复功能（本项目中的删除帖子并未将其从数据库中删除，只是将其状态设置为了拉黑）
- [ ] 定期自动发帖功能
- [ ] 添加 Gitee/Github 账号登陆功能

## 🌱 本地运行

各位如果需要将项目部署在本地进行测试，以下环境请提前备好：

- Java 8
- MySQL 5.7
- Redis
- Kafka 2.13-2.7.0
- Elasticsearch 6.4.3

然后**修改配置文件中的信息为你自己的本地环境，直接运行是运行不了的**，而且相关私密信息我全部用 xxxxxxx 代替了。

本地运行需要修改的配置文件信息如下：

1）`application-develop.properties`：

- MySQL
- Spring Mail（邮箱需要开启 SMTP 服务）
- Kafka：consumer.group-id（该字段见 Kafka 安装包中的 consumer.proerties，可自行修改, 修改完毕后需要重启 Kafka）
- Elasticsearch：cluster-name（该字段见 Elasticsearch 安装包中的 elasticsearch.yml，可自行修改）
- 七牛云（需要新建一个七牛云的对象存储空间，用来存放上传的头像图片）

2）`logback-spring-develop.xml`：

- LOG_PATH：日志存放的位置

每次运行需要打开：

- MySQL
- Redis
- Elasticsearch
- Kafka

另外，还需要事件建好数据库 greatecommunity，然后依次运行项目 sql 文件夹下的这几个 sql 文件建立数据库表：

<img src="https://gitee.com/veal98/images/raw/master/img/20210217134928.png" style="width:386px" />

## 🌌 部署架构

我每个都只部署了一台，以下是理想的部署架构：

<img src="https://gitee.com/veal98/images/raw/master/img/20210211204207.png"  />

## 🎯 功能逻辑图

画了一些不是那么严谨的图帮助各位小伙伴理清思绪。

> 单向绿色箭头：
>
> - 前端模板 -> Controller：表示这个前端模板中有一个超链接是由这个 Controller 处理的
> - Controller -> 前端模板：表示这个 Controller 会像该前端模板传递数据或者跳转
>
> 双向绿色箭头：表示 Controller 和前端模板之间进行参数的相互传递或使用
>
> 单向蓝色箭头： A -> B，表示 A 方法调用了 B 方法
>
> 单向红色箭头：数据库或缓存操作

### 注册

- 用户注册成功，将用户信息存入 MySQL，但此时该用户状态为未激活
- 向用户发送激活邮件，用户点击链接则激活账号（Spring Mail）

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210204222249.png" />

### 登录 | 登出

登录认证模块跳过了 Spring Secuity 自带的认证机制。主要逻辑如下：

- 进入登录界面，随机生成一个字符串来标识这个将要登录的用户，将这个字符串短暂的存入 Cookie（60 秒）；
- 动态生成验证码，并将验证码及标识该用户的字符串短暂存入 Redis（60 秒）；
- 为登录成功（验证用户名、密码、验证码）的用户随机生成登录凭证且设置状态为有效，并将登录凭证及其状态等信息永久存入 Redis，再在 Cookie 中存一份登录凭证；
- 使用拦截器在所有的请求执行之前，从 Cookie 中获取登录凭证，只要 Redis 中该凭证有效并在有效期内，本次请求就会一直持有该用户信息（使用 ThreadLocal 持有用户信息，保证多台服务器上用户的登录状态同步）；
- 勾选记住我，则延长 Cookie 中登录凭证的有效时间；
- 用户登出，将凭证状态设为无效，并更新 Redis 中该登录凭证的相关信息。

下图是登录模块的功能逻辑图，并没有使用 Spring Security 提供的认证逻辑（我觉得这个模块是最复杂的，这张图其实很多细节还没有画全）

![](https://gitee.com/veal98/images/raw/master/img/20210204233233.png)

### 分页显示所有的帖子

- 支持按照 “发帖时间” 显示
- 支持按照 “热度排行” 显示（Spring Quartz）
- 将热帖列表和所有帖子的总数存入本地缓存 Caffeine（利用分布式定时任务 Spring Quartz 每隔一段时间就刷新计算帖子的热度/分数 — 见下文，而 Caffeine 里的数据更新不用我们操心，它天生就会自动的更新它拥有的数据，给它一个初始化方法就完事儿）

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210204222822.png" />



### 账号设置

- 修改头像（异步请求）
  - 将用户选择的头像图片文件上传至七牛云服务器
- 修改密码

此处只画出修改头像：

<img width="700px" src="https://gitee.com/veal98/images/raw/master/img/20210206121201.png" />

### 发布帖子（异步请求）

发布帖子（过滤敏感词），将其存入 MySQL

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210206122521.png" />

### 显示评论及相关信息

> 评论部分前端的名称显示有些缺陷，有兴趣的小伙伴欢迎提 PR 解决 ~

关于评论模块需要注意的就是评论表的设计，把握其中字段的含义，才能透彻了解这个功能的逻辑。

评论 Comment 的目标类型（帖子，评论） entityType 和 entityId 以及对哪个用户进行评论/回复 targetId 是由前端传递给 DiscussPostController 的

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207150925.png" />

一个帖子的详情页需要封装的信息大概如下：

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207151328.png" />

### 添加评论（事务管理）

发布对帖子的评论（过滤敏感词），将其存入 MySQL

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207122908.png" />

### 私信列表和详情页

<img width="700px" src="https://gitee.com/veal98/images/raw/master/img/20210207161130.png" />

### 发送私信（异步请求）

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207161500.png" />

### 点赞（异步请求）

将点赞相关信息存入 Redis 的数据结构 set 中。其中，key 命名为  `like:entity:entityType:entityId`，value 即点赞用户的 id。比如 key =  `like:entity:2:246`  value =  `11` 表示用户 11 对实体类型 2 即评论进行了点赞，该评论的 id 是 246

某个用户的获赞数量对应的存储在 Redis 中的 key 是 `like:user:userId`，value 就是这个用户的获赞数量

<img width="700px" src="https://gitee.com/veal98/images/raw/master/img/20210207165837.png"  />

### 我的获赞数量

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207170003.png" />

### 关注（异步请求）

- 若 A 关注了 B，则 A 是 B 的粉丝 Follower，B 是 A 的目标 Followee
- 关注的目标可以是用户、帖子、题目等，在实现时将这些目标抽象为实体（目前只做了关注用户）

将某个用户关注的实体相关信息存储在 Redis 的数据结构 zset 中：key 是 `followee:userId:entityType` ，对应的 value 是 `zset(entityId, now)` ，以关注的时间进行排序。比如说 `followee:111:3` 对应的value `(20, 2020-02-03-xxxx)`，表明用户 111 关注了一个类型为 3 的实体即人(用户)，关注的这个实体 id 是 20，关注该实体的时间是 2020-02-03-xxxx

同样的，将某个实体拥有的粉丝相关信息也存储在 Redis 的数据结构 zset 中：key 是 `follower:entityType:entityId`，对应的 value 是 `zset(userId, now)`，以关注的时间进行排序

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207174046.png" />

### 关注列表

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210207175621.png" />

### 发送系统通知

![](https://gitee.com/veal98/images/raw/master/img/20210207182917.png)

### 显示系统通知

![](https://gitee.com/veal98/images/raw/master/img/20210208153059.png)

### 搜索

- 发布事件
  - 发布帖子时，通过消息队列将帖子异步地提交到 Elasticsearch 服务器
  - 为帖子增加评论时，通过消息队列将帖子异步地提交到 Elasticsearch 服务器
- 搜索服务
  - 从 Elasticsearch 服务器搜索帖子
  - 从 Elasticsearch 服务器删除帖子（当帖子从数据库中被删除时）
- 显示搜索结果

![](https://gitee.com/veal98/images/raw/master/img/20210208161936.png)

类似的，置顶、加精也会触发发帖事件，就不再图里面画出来了。

### 置顶加精删除（异步请求）

<img width="660px" src="https://gitee.com/veal98/images/raw/master/img/20210208171729.png" />

### 网站数据统计

- 独立访客 UV
  - 存入 Redis 的 HyperLogLog
  - 支持单日查询和区间日期查询
- 日活跃用户 DAU
  - 存入 Redis 的 Bitmap
  - 支持单日查询和区间日期查询
- 权限管理（Spring Security）
  - 只有管理员可以查看网站数据统计

![](https://gitee.com/veal98/images/raw/master/img/20210208170801.png)

### 帖子热度计算

每次发生点赞（给帖子点赞）、评论（给帖子评论）、加精的时候，就将这些帖子信息存入缓存 Redis 中，然后通过分布式的定时任务 Spring Quartz，每隔一段时间就从缓存中取出这些帖子进行计算分数。

帖子分数/热度计算公式：分数（热度） = 权重 + 发帖距离天数

```java
// 计算权重
double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
// 分数 = 权重 + 发帖距离天数
double score = Math.log10(Math.max(w, 1))
        + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
```

![](https://gitee.com/veal98/images/raw/master/img/20210208173636.png)


## 🙋 微信交流群

下方扫码关注公众号回复 `Echo`，里面有我的联系方式，备注 "Echo" 加我微信，我拉你进 Echo 微信交流群，实时跟进项目进度，第一时间获取教程更新，分享您的想法，还能帮您解决遇到的问题：

<img width="220px" src="https://gitee.com/veal98/images/raw/master/img/20210204145531.png"  />

## 👏 鸣谢

若发现 Bug 或好的想法可以积极与我联系或提 PR / issue，采纳后您将出现在下方列表中。感谢以下小伙伴对本项目做出的贡献，排名按照时间先后：

- [freedom_dh](https://gitee.com/dh_free)
- [paulisusu](https://gitee.com/paulisusu)
- [Max (Lyingfromyou)](https://gitee.com/Lyingfromyou)
- [逆光影者 (cpfree) ](https://gitee.com/cpfree)

> 🔗 友情链接（若您想要出现在这里，可以上方扫描微信二维码联系我）：
>
> - [CS-Wiki](https://gitee.com/veal98/CS-Wiki)：致力打造完善的 Java 后端知识体系，不仅仅帮助各位小伙伴快速且系统的准备面试，更指引学习的方向
> - [Furion](https://gitee.com/dotnetchina/Furion)：让 .NET 开发更简单，更通用，更流行 
> - [Free-Fs](https://gitee.com/dh_free/free-fs)：Spring Boot 开源云文件管理系统，方便快捷的管理云存储的文件
