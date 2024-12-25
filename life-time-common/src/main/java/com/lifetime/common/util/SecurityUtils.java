package com.lifetime.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author:wangchao
 * @date: 2024/12/24-18:51
 * @description: com.lifetime.common.util
 * @Version:1.0
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static String getUserCode() {
        return  SecurityContextHolder.getContext().getAuthentication().getName();

        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        return getUserCode().equals("admin")?true:false;
    }
}

