package com.lifetime.security.sms;

import com.lifetime.common.manager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * @author:wangchao
 * @date: 2023/7/6-15:04
 * @description: com.sh3h.security.sms
 * @Version:1.0
 */
@Component
public class SmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserService userService;

    @Override

    public void configure(HttpSecurity builder){
        SMSCodeAuthenticationProvider smsCodeAuthenticationProvider=new SMSCodeAuthenticationProvider(userService,passwordEncoder);
        builder.authenticationProvider(smsCodeAuthenticationProvider);
    }

}
