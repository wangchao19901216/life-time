package com.lifetime.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author:wangchao
 * @date: 2023/5/7-22:32
 * @description: com.sh3h.common.config
 * @Version:1.0
 */
@Configuration
public class SpringSecurityBean {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}
