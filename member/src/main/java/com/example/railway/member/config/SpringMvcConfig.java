package com.example.railway.member.config;

import com.example.railway.interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    MemberInterceptor memberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/member/member/send-code", "/member/member/login");
    }
}
