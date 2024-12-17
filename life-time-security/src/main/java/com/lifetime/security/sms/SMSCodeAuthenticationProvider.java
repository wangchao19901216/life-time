package com.lifetime.security.sms;

import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author:wangchao
 * @date: 2023/7/6-14:52
 * @description: com.sh3h.security.sms
 * @Version:1.0
 */
public class SMSCodeAuthenticationProvider implements AuthenticationProvider {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public SMSCodeAuthenticationProvider(IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SMSCodeAuthenticationToken authenticationToken=(SMSCodeAuthenticationToken) authentication;
        String userName=(String) authenticationToken.getPrincipal();

        UserEntity userEntity= userService.findByUserCode(userName);

        SMSCodeAuthenticationToken authenticationResult=new SMSCodeAuthenticationToken(userEntity,userEntity.getUsername(),userEntity.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  SMSCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
