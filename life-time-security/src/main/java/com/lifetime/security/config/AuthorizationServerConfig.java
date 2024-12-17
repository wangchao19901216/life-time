package com.lifetime.security.config;

import com.lifetime.common.manager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/5/7-20:10
 * @description: com.sh3h.security.config
 * @Version:1.0
 */
@Configuration
@EnableAuthorizationServer  //开启认证服务
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
   // @Autowired
   //  RedisTemplate redisTemplate;
    @Autowired
   IUserService userService;
    @Qualifier("customerUserDetailService")
    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    DataSource dataSource;

    @Bean
    // 授权码管理策略 44
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        // JDBC方式保存授权码到 oauth_code 表中,
        // 意义不大，因为获取一次令牌后，授权码就失效了
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    @Autowired
    TokenStore tokenStore;

    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 服务端点配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //开启password 模式
        endpoints.authenticationManager(authenticationManager);
        //刷新令牌时候使用
        endpoints.userDetailsService(customUserDetailsService);
        //令牌管理方式
        endpoints.tokenStore(tokenStore);

        // 授权码管理策略，针对授权码模式有效，会将授权码放到 auth_code 表，授权后就会删除它
        endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices());

        //开启短信验证模式
        endpoints.tokenGranter(tokenGranter(endpoints));


        // DefaultTokenServices tokenServices = new DefaultTokenServices();


//        // 配置TokenServices参数 自定义SystemTokenServices 的实现
//        SystemTokenServices tokenServices = new SystemTokenServices();
//        tokenServices.setTokenStore(endpoints.getTokenStore());
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
//        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
//        tokenServices.setReuseRefreshToken(false);  // 每次刷新token 都会重新生成新的refresh token
//        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7)); // 刷新token有效时(天)
//        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2)); //token有效时间(小时)
////        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.SECONDS.toSeconds(600)); // 刷新token有效时 测试
////        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.SECONDS.toSeconds(60)); //token有效时间 测试
//        endpoints.tokenServices(tokenServices);
    }

    /**
     * 令牌端点 安全配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //获取公钥
        security.tokenKeyAccess("permitAll()");
        //检验token
        security.checkTokenAccess("isAuthenticated()");
    }

    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {

        // endpoints.getTokenGranter() 获取SpringSecurity OAuth2.0 现有的授权类型
        List<TokenGranter> granters =
                new ArrayList<TokenGranter>(Collections.singletonList(endpoints.getTokenGranter()));

        // 构建短信验证授权类型
//        SmsCodeTokenGranter smsCodeTokenGranter =
//                new SmsCodeTokenGranter( authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(),
//                        endpoints.getOAuth2RequestFactory(),redisTemplate,userService);
//        // 向集合中添加短信授权类型
//        granters.add(smsCodeTokenGranter);
        // 返回所有类型
        return new CompositeTokenGranter(granters);
    }


}

