package com.greate.community.config;

import com.greate.community.controller.interceptor.DataInterceptor;
import com.greate.community.controller.interceptor.LoginTicketInterceptor;
import com.greate.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    // 对除静态资源外所有路径进行拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/editor-md/**", "/editor-md-upload/**");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/editor-md/**", "/editor-md-upload/**");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/editor-md/**", "/editor-md-upload/**");
    }

    // 配置虚拟路径映射访问
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // System.getProperty("user.dir") 获取程序的当前路径
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\editor-md-upload\\";
        registry.addResourceHandler("/editor-md-upload/**").addResourceLocations("file:" + path);
    }

}
