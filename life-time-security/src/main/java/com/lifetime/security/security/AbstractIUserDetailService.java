package com.lifetime.security.security;
import com.lifetime.common.manager.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author:wangchao
 * @date: 2023/5/7-20:05
 * @description: com.sh3h.security.security
 * @Version:1.0
 */
public abstract class AbstractIUserDetailService implements UserDetailsService {
    public  abstract UserEntity findUser(String userCode);
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        UserEntity userEntity=findUser(userCode);
        return userEntity;
    }
}
