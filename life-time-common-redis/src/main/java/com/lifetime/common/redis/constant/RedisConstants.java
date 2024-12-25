package com.lifetime.common.redis.constant;

/**
 * @author:wangchao
 * @date: 2024/12/18-15:08
 * @description: com.lifetime.common.redis.constant
 * @Version:1.0
 */
public class RedisConstants {
    /**
     * 用户手机号登入的标识
     */
    public static final String MOBILE_LOGIN = "MOBILE_LOGIN_KEY:";


    public static final String LOGIN_USER = "LOGIN_USER_KEY:";

    //redis 提前过期时间 10分钟
    public static final Integer LOGIN_EXPIRE = 600;

}
