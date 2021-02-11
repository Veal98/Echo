package com.greate.community.service;

import com.greate.community.dao.UserMapper;
import com.greate.community.entity.LoginTicket;
import com.greate.community.entity.User;
import com.greate.community.util.CommunityConstant;
import com.greate.community.util.CommunityUtil;
import com.greate.community.util.MailClient;
import com.greate.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 用户相关
 */
@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 根据 Id 查询用户
     * @param id
     * @return
     */
    public User findUserById (int id) {
        // return userMapper.selectById(id);
        User user = getCache(id); // 优先从缓存中查询数据
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    /**
     * 根据 username 查询用户
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    /**
     * 用户注册
     * @param user
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明注册成功
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 验证账号是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }

        // 验证邮箱是否已存在
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5)); // salt
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt())); // 加盐加密
        user.setType(0); // 默认普通用户
        user.setStatus(0); // 默认未激活
        user.setActivationCode(CommunityUtil.generateUUID()); // 激活码
        // 随机头像（用户登录后可以自行修改）
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date()); // 注册时间
        userMapper.insertUser(user);

        // 给注册用户发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/echo/activation/用户id/激活码
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活 Echo 账号", content);

        return map;
    }

    /**
     * 激活用户
     * @param userId 用户 id
     * @param code 激活码
     * @return
     */
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            // 用户已激活
            return ACTIVATION_REPEAT;
        }
        else if (user.getActivationCode().equals(code)) {
            // 修改用户状态为已激活
            userMapper.updateStatus(userId, 1);
            clearCache(userId); // 用户信息变更，清除缓存中的旧数据
            return ACTIVATION_SUCCESS;
        }
        else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 用户登录（为用户创建凭证）
     * @param username
     * @param password
     * @param expiredSeconds 多少秒后凭证过期
     * @return Map<String, Object> 返回错误提示消息以及 ticket(凭证)
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            // 账号未激活
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        // 用户名和密码均正确，为该用户生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID()); // 随机凭证
        loginTicket.setStatus(0); // 设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000)); // 设置凭证到期时间

        // loginTicketMapper.insertLoginTicket(loginTicket);
        // 将登录凭证存入 redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    /**
     * 用户退出（将凭证状态设为无效）
     * @param ticket
     */
    public void logout(String ticket) {
        // loginTicketMapper.updateStatus(ticket, 1);
        // 修改（先删除再插入）对应用户在 redis 中的凭证状态
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    /**
     * 根据 ticket 查询 LoginTicket 信息
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket) {
        // return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 修改用户头像
     * @param userId
     * @param headUrl
     * @return
     */
    public int updateHeader(int userId, String headUrl) {
        // return userMapper.updateHeader(userId, headUrl);
        int rows = userMapper.updateHeader(userId, headUrl);
        clearCache(userId);
        return rows;
    }

    /**
     * 修改用户密码（对新密码加盐加密存入数据库）
     * @param userId
     * @param newPassword 新密码
     * @return
     */
    public int updatePassword(int userId, String newPassword) {
        User user = userMapper.selectById(userId);
        // 重新加盐加密
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        return userMapper.updatePassword(userId, newPassword);
    }

    /**
     * 优先从缓存中取值
     * @param userId
     * @return
     */
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 缓存中没有该用户信息时，则将其存入缓存
     * @param userId
     * @return
     */
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * 用户信息变更时清除对应缓存数据
     * @param userId
     */
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 获取某个用户的权限
     * @param userId
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }

}
