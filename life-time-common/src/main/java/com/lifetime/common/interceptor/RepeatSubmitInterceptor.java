package com.lifetime.common.interceptor;

import com.lifetime.common.annotation.RepeatSubmit;
import com.lifetime.common.util.json.JsonUtil;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.util.ServletUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author:wangchao
 * @date: 2024/12/20-10:53
 * @description: com.lifetime.common.interceptor 防止多次提交
 * @Version:1.0
 */
@Component
public abstract class  RepeatSubmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null)
            {
                if (this.isRepeatSubmit(request, annotation))
                {
                    ResponseResult responseResult =ResponseResult.error(500,annotation.message());
                    ServletUtils.renderString(response, JsonUtil.marshal(responseResult));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request 请求对象
     * @param annotation 防复注解
     * @return 结果
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) throws Exception;
}
