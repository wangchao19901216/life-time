package com.lifetime.security.security;

import com.lifetime.common.manager.entity.UserEntity;
import com.lifetime.common.manager.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author:wangchao
 * @date: 2023/5/7-20:05
 * @description: com.sh3h.security.security
 * @Version:1.0
 */
@Component()
public class CustomerUserDetailService extends AbstractIUserDetailService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IUserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserEntity findUser(String userCode) {
        UserEntity userEntity=userService.findByUserCode(userCode);
        if (!Optional.ofNullable(userEntity).isPresent()) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return userEntity;
    }
}
