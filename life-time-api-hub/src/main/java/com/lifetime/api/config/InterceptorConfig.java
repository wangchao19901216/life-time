package com.lifetime.api.config;

import com.lifetime.api.filter.ApiAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author:wangchao
 * @date: 2025/2/26-16:38
 * @description: com.lifetime.api.config
 * @Version:1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public ApiAuthInterceptor apiInterceptor() {
        return new ApiAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // API开放接口拦截器
        registry.addInterceptor(apiInterceptor())
                .addPathPatterns("/api/common/**"); // 需要拦截的请求
    }
}
