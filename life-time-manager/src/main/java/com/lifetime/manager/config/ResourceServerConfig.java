package com.lifetime.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author:wangchao
 * @date: 2023/5/7-22:30
 * @description: com.sh3h.common.config
 * @Version:1.0
 */
@Configuration
@EnableResourceServer // 标识为资源服务器，请求服务中的资源，就要带着token过来，找不到token或token是无效访问不了资源
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别权限控制
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    CustomSessionInformationExpiredStrategy customSessionInformationExpiredStrategy;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源id的权限，有则可以访问当前服务
        resources.resourceId(authConfig.getResourceId().trim()).tokenStore(tokenStore).tokenServices(tokenService());
    }

    public ResourceServerTokenServices tokenService() {
        // 远程认证服务器进行校验 token 是否有效
        RemoteTokenServices service = new RemoteTokenServices();
        // 请求认证服务器校验的地址，默认情况 这个地址在认证服务器它是拒绝访问，要设置为认证通过可访问
        service.setCheckTokenEndpointUrl(authConfig.getCheckUrl());
        service.setClientId(authConfig.getClientId());
        service.setClientSecret(authConfig.getClientSecret());
        return service;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                // SpringSecurity不会使用也不会创建HttpSession实例
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/doc.html")
                .permitAll()

//        http.sessionManagement()
//                .maximumSessions(1) //新的登录踢掉旧的登录  .maxSessionsPreventsLogin(true);此时一个浏览器登录成功后，另外一个浏览器就登录不了了
//                .expiredSessionStrategy(customSessionInformationExpiredStrategy)
//                .and()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // SpringSecurity不会使用也不会创建HttpSession实例
//                .and()
//                .authorizeRequests().
//                antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/doc.html").permitAll();
        //.permitAll()
        // 授权规则配置
//                .antMatchers("/product/*").hasAuthority("product")
        // 所有请求，都需要有all范围（scope）
//                .antMatchers("/**").access("#oauth2.hasScope('all')")
        // 等价于上面
//                .anyRequest().access("#oauth2.hasScope('all')")
        ;
    }



    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {

        return new HttpSessionEventPublisher();
    }


}

