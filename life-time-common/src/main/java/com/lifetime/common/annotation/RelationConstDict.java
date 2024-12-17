package com.lifetime.common.annotation;

import java.lang.annotation.*;

/**
 * @author:wangchao
 * @date: 2023/5/9-11:06
 * @description: 标识Model和常量字典之间的关联关系
 * @Version:1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  RelationConstDict {
    /**
     * 当前对象的关联Id字段名称。
     *
     * @return 当前对象的关联Id字段名称。
     */
    String masterIdField();

    /**
     * 被关联的常量字典的Class对象。
     *
     * @return 关联的常量字典的Class对象。
     */
    Class<?> constantDictClass();
}

