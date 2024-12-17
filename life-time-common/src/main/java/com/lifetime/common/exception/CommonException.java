package com.lifetime.common.exception;


import com.lifetime.common.enums.CommonExceptionEnum;

/**
 * @ClassName CommonException
 * @Author Liwx
 * 抛出异常
 */

public class CommonException extends IllegalArgumentException {
    private Integer code = 500;
    private String message;

    public CommonException() {
    }

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;

    }

    public CommonException(String message) {
        super(message);

    }

    public CommonException(CommonExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
