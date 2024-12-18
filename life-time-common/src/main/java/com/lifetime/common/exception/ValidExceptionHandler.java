package com.lifetime.common.exception;

import com.lifetime.common.response.ResponseResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedList;
import java.util.List;

/**
 * @Auther:wangchao
 * @Date: 2022/8/2-0:35
 * @Description: com.lifetiem.base.exception
 * @Version:1.0
 */
@RestControllerAdvice
public class ValidExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseResult handlerMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        System.out.println("==================================================");
        System.out.println(ex);
        BindingResult bindingResult=ex.getBindingResult();
        List<String> list = new LinkedList<>();
        bindingResult.getFieldErrors().forEach(error -> {
//            String field = error.getField();
//            Object value = error.getRejectedValue();
            String msg = error.getDefaultMessage();
            list.add(msg);
        });
        return ResponseResult.error(500,"数据效验失败！",list);
    }
}
