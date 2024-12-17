package com.lifetime.manager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author:wangchao
 * @date: 2023/5/7-22:30
 * @description: com.sh3h.common.config
 * @Version:1.0
 */
@Data
@Component
public class AuthConfig {
    @Value("${lifetime.auth.clientId}")
    private  String clientId;

    @Value("${lifetime.auth.clientSecret}")
    private  String clientSecret;

    @Value("${lifetime.auth.tokenUrl}")
    private  String tokenUrl;

    @Value("${lifetime.auth.checkUrl}")
    private  String checkUrl;

    @Value("${lifetime.auth.resourceId}")
    private  String resourceId;
}
