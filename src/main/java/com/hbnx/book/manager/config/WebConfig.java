package com.hbnx.book.manager.config;

import com.hbnx.book.manager.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Description web mvc 配置
 * @Date 2020/7/15 20:47
 * @Author by 尘心
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器 使用spring security 无需登录拦截

    }

    

}
