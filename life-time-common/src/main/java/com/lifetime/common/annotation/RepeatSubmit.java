package com.lifetime.common.annotation;

import java.lang.annotation.*;

/**
 * @author:wangchao
 * @date: 2024/12/20-10:39
 * @description: com.lifetime.common.annotation
 * @Version:1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    public int interval() default 5000;

    /**
     * 提示消息
     */
    public String message() default "不允许重复提交，请稍后再试";
}
