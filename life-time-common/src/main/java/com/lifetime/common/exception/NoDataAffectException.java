package com.lifetime.common.exception;

/**
 * @author:wangchao
 * @date: 2023/5/9-21:01
 * @description: 没有数据被修改的自定义异常
 * @Version:1.0
 */
public class NoDataAffectException extends RuntimeException {

    /**
     * 构造函数。
     */
    public NoDataAffectException() {

    }
    /**
     * 构造函数。
     *
     * @param msg 错误信息。
     */
    public NoDataAffectException(String msg) {
        super(msg);
    }
}

