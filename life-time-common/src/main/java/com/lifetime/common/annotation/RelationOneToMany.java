package com.lifetime.common.annotation;
import com.lifetime.common.object.DummyClass;

import java.lang.annotation.*;

/**
 * @author:wangchao
 * @date: 2023/5/9-11:12
 * @description: 标识Model之间的一对多关联关系。
 * @Version:1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationOneToMany {

    /**
     * 当前对象的关联Id字段名称。
     *
     * @return 当前对象的关联Id字段名称。
     */
    String masterIdField();

    /**
     * 被关联Model对象的Class对象。
     *
     * @return 被关联Model对象的Class对象。
     */
    Class<?> slaveModelClass();

    /**
     * 被关联Model对象的关联Id字段名称。
     *
     * @return 被关联Model对象的关联Id字段名称。
     */
    String slaveIdField();

    /**
     * 被关联的本地Service对象名称。
     * 该参数的优先级高于 slaveService()，如果定义了该值，会优先使用加载service的bean对象。
     *
     * @return 被关联的本地Service对象名称。
     */
    String slaveServiceName() default "";

    /**
     * 被关联的本地Service对象CLass类型。
     *
     * @return 被关联的本地Service对象CLass类型。
     */
    Class<?> slaveServiceClass() default DummyClass.class;
}

