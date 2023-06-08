package com.poho.stuup.api.config;

import com.poho.stuup.api.interceptor.CorsInterceptor;
import com.poho.stuup.api.interceptor.MicrovanInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 20:24 2019-06-09
 * @Modified By:
 */
@Configuration
public class MicrovanWebConfig implements WebMvcConfigurer {
    @Resource
    private MicrovanInterceptor microvanInterceptor;

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
        registry.addInterceptor(microvanInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/**")
                .excludePathPatterns("/sync/**")
                .excludePathPatterns("/csrf")
                .excludePathPatterns("/manualTask/**");
    }
}
