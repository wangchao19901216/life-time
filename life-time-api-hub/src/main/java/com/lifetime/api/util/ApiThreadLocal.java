package com.lifetime.api.util;

import com.lifetime.api.model.ApiCacheModel;

/**
 * @author:wangchao
 * @date: 2025/2/26-17:10
 * @description: com.lifetime.api.util
 * @Version:1.0
 */
public class ApiThreadLocal {

    private static ThreadLocal<ApiCacheModel> apiCacheModelThreadLocal =new ThreadLocal<>();

    /**
     * API存入上下文中
     * @param api
     */
    public static void set(ApiCacheModel api){
        apiCacheModelThreadLocal.set(api);
    }

    /**
     * 用户API数据
     */
    public static ApiCacheModel get(){
        return apiCacheModelThreadLocal.get();
    }

    /**
     * 获取apiId
     */
    public static String getApiId(){
        return apiCacheModelThreadLocal.get().getApiInfo().getApiBaseInfo().getApiCode();
    }

    /**
     * 清除
     */
    public static void remove(){
        apiCacheModelThreadLocal.remove();
    }
}
