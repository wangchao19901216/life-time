package com.lifetime.common.exception;

/**
 * @author:wangchao
 * @date: 2023/5/9-21:03
 * @description: com.sh3h.common.exception
 * @Version:1.0
 */
public class RedisCacheAccessException extends RuntimeException {

    /**
     * 构造函数。
     *
     * @param msg   错误信息。
     * @param cause 原始异常。
     */
    public RedisCacheAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

