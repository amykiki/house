package com.april.house.web.config;

import com.april.house.web.interceptors.AuthActionInterceptor;
import com.april.house.web.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description :
 * @Creation Date:  2018-10-24 13:33
 * --------  ---------  --------------------------
 */
@Configuration
public class HouseWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private AuthActionInterceptor authActionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static");

        registry
                .addInterceptor(authActionInterceptor)
                .addPathPatterns("/accounts/profile*")
                .addPathPatterns("/accounts/getHouseMsgs")
                .addPathPatterns("/house/toAdd")
                .addPathPatterns("/house/add")
                .addPathPatterns("/house/ownlist")
                .addPathPatterns("/house/bookmarked")
                .addPathPatterns("/house/bookmark")
                .addPathPatterns("/house/unbookmark")
                .addPathPatterns("/house/del")
                .addPathPatterns(("/agency/create"))
                .addPathPatterns(("/agency/submit"))
                .addPathPatterns("/comment/leaveComment")
                .addPathPatterns("/comment/leaveBlogComment");

    }
}
