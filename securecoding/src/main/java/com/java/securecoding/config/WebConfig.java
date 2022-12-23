package com.java.securecoding.config;

import com.java.securecoding.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/bootstrap/**", "/css/**", "/images/**", "/*.ico", "/", "/member/login",
                        "/2/16", "/2/16/*", "/member/signup", "/2/5", "/2/5/*", "/mypage/info/*", "/mypage/delete/*",
                        "/member/logout", "/1/1", "/1/2", "/1/3", "/1/4", "/1/5", "/board/new", "/1/6/*", "/1/7/*", "/1/8", "/1/9",
                        "/1/10", "/1/11", "/1/12", "/1/13", "/1/14", "/1/16", "/1/17",
                        "/2/3", "/2/4/*", "/2/8/*", "/2/9/*", "/2/10", "/2/11", "/2/12", "/2/13", "/2/14/*", "/2/15",
                        "/3/*", "/4/*", "/5/*", "/6/*", "/7/*");
    }
}
