package com.lifetime.common.exception;

import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.response.ResponseResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

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
        BindingResult bindingResult=ex.getBindingResult();
        List<Map<String,String>> list=new ArrayList<>();
        bindingResult.getFieldErrors().forEach(error -> {
            Map<String,String> map=new HashMap<>();
            map.put(error.getField(),error.getDefaultMessage());
//            String field = error.getField();
//            Object value = error.getRejectedValue();
            //String msg = error.getDefaultMessage();
            list.add(map);
        });
        return ResponseResult.error(500, CommonExceptionEnum.INVALID_ARGUMENT_FORMAT.getMessage(),list);
    }
}
