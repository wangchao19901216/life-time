package com.lifetime.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author:wangchao
 * @date: 2023/5/7-22:33
 * @description: com.sh3h.common.config
 * @Version:1.0
 */
@Configuration
public class TokenConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public TokenStore tokenStore(){
        //JDBC 管理令牌
        return  new JdbcTokenStore(dataSource);
    }
}

