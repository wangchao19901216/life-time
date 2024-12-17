package com.lifetime.common.exception;

/**
 * @author:wangchao
 * @date: 2023/5/9-10:59
 * @description:  无效的实体对象字段的自定义异常
 * @Version:1.0
 */
public class InvalidDataFieldException extends RuntimeException {
    private final String modelName;
    private final String fieldName;

    /**
     * 构造函数。
     *
     * @param modelName 实体对象名。
     * @param fieldName 字段名。
     */
    public InvalidDataFieldException(String modelName, String fieldName) {
        this.modelName = modelName;
        this.fieldName = fieldName;
    }
}
