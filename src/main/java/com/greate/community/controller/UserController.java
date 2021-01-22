package com.greate.community.controller;

import com.greate.community.annotation.LoginRequired;
import com.greate.community.entity.User;
import com.greate.community.service.UserService;
import com.greate.community.util.CommunityUtil;
import com.greate.community.util.HostHolder;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;


/**
 * 处理用户信息的修改
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // 网站域名
    @Value("${community.path.upload}")
    private String uploadPath;

    /**
     * 跳转至账号设置界面
     * @return
     */
    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 修改用户头像
     * @param headerImage
     * @param model
     * @return
     */
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }

        // 获取文件后缀名
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")); // 文件后缀名
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "图片文件格式不正确");
            return "/site/setting";
        }

        // 对用户上传的图片文件进行重新随机命名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储图片文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常", e);
        }

        // 更新当前用户的头像的路径（web 访问路径）
        // http://localhost:8080/echo/user/header/xxx.png
        User user = hostHolder.getUser();
        String headUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headUrl);

        return "redirect:/index";

    }

    /**
     * 响应/显示用户上传的图片文件
     * 即解析当前用户头像的路径（web 访问路径）http://localhost:8080/echo/user/header/fileName
     * @param fileName
     * @param response
     */
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 图片存放在服务器上的路径
        fileName = uploadPath + "/" + fileName;
        // 图片文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
            FileInputStream fis = new FileInputStream(fileName);
            OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取文件失败" + e.getMessage());
        }
    }

    /**
     * 修改用户密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @param model
     * @return
     */
    @LoginRequired
    @PostMapping("/password")
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        // 验证原密码是否正确
        User user = hostHolder.getUser();
        String md5OldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(md5OldPassword)) {
            model.addAttribute("oldPasswordError", "原密码错误");
            return "/site/setting";
        }

        // 判断新密码是否合法
        String md5NewPassword = CommunityUtil.md5(newPassword + user.getSalt());
        if (user.getPassword().equals(md5NewPassword)) {
            model.addAttribute("newPasswordError", "新密码和原密码相同");
            return "/site/setting";
        }

        // 修改用户密码
        userService.updatePassword(user.getId(), newPassword);

        return "redirect:/index";
    }
}
