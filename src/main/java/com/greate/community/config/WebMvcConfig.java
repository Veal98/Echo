package com.greate.community.config;

import com.greate.community.controller.interceptor.DataInterceptor;
import com.greate.community.controller.interceptor.LoginTicketInterceptor;
import com.greate.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对除静态资源外所有路径进行拦截
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");
    }

}
