package com.lifetime.common.exception;

import com.lifetime.common.response.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * @author:wangchao
 * @date: 2025/2/27-17:32
 * @description: com.lifetime.common.exception
 * @Version:1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseResult<Object> handleException(Exception e) {
        return ResponseResult.error(500,e.getMessage());
    }
}
