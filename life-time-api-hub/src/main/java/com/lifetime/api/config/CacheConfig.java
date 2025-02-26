package com.lifetime.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * @author:wangchao
 * @date: 2025/2/25-15:15
 * @description: com.lifetime.api.config
 * @Version:1.0
 */
@Configuration
public class CacheConfig {
    /**
     * 缓存API对象 10小时
     * @return
     */
    @Bean("apiCache")
    public Cache<String, Object> apiCaffeine() {
        return Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.HOURS)
                .build();
    }

}
