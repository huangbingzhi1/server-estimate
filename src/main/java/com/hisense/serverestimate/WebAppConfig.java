package com.hisense.serverestimate;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/5/6 15:35
 * @Version 1.0
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPattern后跟拦截地址，excludePathPatterns后跟排除拦截地址
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/userController/cisLogin",
                        "/files/notLogin.html",
                        "/cislogin.html",
                        "/login.html",
                        "/images/**",
                        "/js/**",
                        "/css/**",
                        "/examController/receiveExamResult",
                        "/examController/sendExamResult",
                        "/examController/examInfo",
                        "/userController/login",
                        "/userController/ssoLogin");
    }

}
