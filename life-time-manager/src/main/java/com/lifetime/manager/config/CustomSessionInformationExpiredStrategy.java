package com.lifetime.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author:wangchao
 * @date: 2023/6/23-11:39
 * @description: com.sh3h.manager.config
 * @Version:1.0
 */
@Component("customSessionInformationExpiredStrategy")
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        //获取用户名
//        UserDetails userDetails = (UserDetails) event.getSessionInformation().getPrincipal();
        AuthenticationException exception = new AuthenticationServiceException(String.format("[%s]用户在另外一台电脑登录，您已被下线", event.getSessionInformation().getPrincipal()));
        //当用户在另外一台电脑登录后，交给失败处理器响应给前端json数据
        customAuthenticationFailureHandler.onAuthenticationFailure(event.getRequest(), event.getResponse(), exception);
    }
}