package com.lifetime.api.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.lifetime.api.business.ApiInfoBusiness;
import com.lifetime.api.entity.AppEntity;
import com.lifetime.api.model.ApiCacheModel;
import com.lifetime.api.model.ApiModel;
import com.lifetime.api.util.ApiThreadLocal;
import com.lifetime.common.enums.ApiAuthEnum;
import com.lifetime.common.enums.ApiStatusEnum;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.exception.CommonException;
import com.lifetime.common.util.HmacSHAUtils;
import com.lifetime.common.util.RequestUtils;
import org.assertj.core.condition.MappedCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author:wangchao
 * @date: 2025/2/26-16:36
 * @description: API 开放接口鉴权 拦截器
 * @Version:1.0
 */
public class ApiAuthInterceptor implements HandlerInterceptor {

    @Value("${api.auth.expiresTime}")
    private Integer expiresTime;
    // API开放接口前缀
    private final static String API_PRE = "/api/common";

    @Autowired
    ApiInfoBusiness apiInfoBusiness;

    @Autowired
    @Qualifier("apiCache")
    Cache<String, Object> apiInfoCache;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String path = request.getRequestURI();
        String method = request.getMethod();
        String url=path.replace(API_PRE, "");
        String key=method + "_" + url;
        Object apiData = apiInfoCache.getIfPresent(key);
        if (apiData == null) {
            try{
                apiInfoBusiness.setCache(apiInfoBusiness.buildApiCacheModelByMethodAndUrl(method,url));
                apiData = apiInfoCache.getIfPresent(key);
                if(apiData == null){
                    throw new CommonException(ApiStatusEnum.API_INVALID.getCode(), ApiStatusEnum.API_INVALID.getMassage());
                }
            }
            catch (Exception exception){
                throw new CommonException(ApiStatusEnum.API_INVALID.getCode(), ApiStatusEnum.API_INVALID.getMassage());
            }

        }
        ApiCacheModel apiCacheModel=(ApiCacheModel) apiData;
        ApiModel apiModel=apiCacheModel.getApiInfo();
        if (!apiModel.getApiBaseInfo().getIsPublish().equals("1")) {
            throw new CommonException(ApiStatusEnum.API_OFFLINE.getCode(), ApiStatusEnum.API_OFFLINE.getMassage());
        }
        // 应用列表
        List<AppEntity> appList = apiCacheModel.getApps();

        boolean auth = true;
        if (ApiAuthEnum.CODE.getValue().equalsIgnoreCase(apiModel.getApiBaseInfo().getAuthType())) {
            auth = checkAppCode(request, appList);
        } else if (ApiAuthEnum.APP_SECRET.getValue().equalsIgnoreCase(apiModel.getApiBaseInfo().getAuthType())) {
            auth = checkHmacSHA256(request, appList);
        }
        if (!auth) {
            throw new CommonException(ApiStatusEnum.API_UN_AUTH.getCode(), ApiStatusEnum.API_UN_AUTH.getMassage());
        }
        //存入当前时间，当作是日志的请求时间
        apiCacheModel.setRequestDate(new Date());
        apiCacheModel.setRequestTime(System.currentTimeMillis());
        // 放入上下文
        ApiThreadLocal.set(apiCacheModel);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //写入请求日志
        // addLog(request, response, ex);
        // 清除上下文
        ApiThreadLocal.remove();
    }


    /**
     * 验证接口访问权限
     *
     * @param request
     * @param appList
     * @return
     */
    private boolean checkAppCode(HttpServletRequest request, List<AppEntity> appList) {
        String appCode = RequestUtils.getAppCode(request);
        if (appCode == null || "".equals(appCode)) {
            return false;
        }
        for (AppEntity app : appList) {
            if (app.getAppCode().equals(appCode)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 参数签名认证
     *
     * @param request
     * @param appList
     * @return
     * @throws Exception
     */
    public boolean checkHmacSHA256(HttpServletRequest request, List<AppEntity> appList) {
        // 校验参数
        String sign = request.getHeader("sign") == null ? request.getParameter("sign") : request.getHeader("sign");
        String timeStamp = request.getHeader("timestamp") == null ? request.getParameter("timestamp") : request.getHeader("timestamp");
        String appKey = request.getHeader("appkey") == null ? request.getParameter("appkey") : request.getHeader("appkey");
        if (appKey == null || sign == null || timeStamp == null) {
            throw new CommonException(ApiStatusEnum.SHA_PARAM_NOT_FOUNT.getCode(), ApiStatusEnum.PARAM_NOT_FOUNT.getMassage());
        }
        // 校验时间戳,超过10分钟失效
        long authTime = Long.parseLong(timeStamp);
        long nowTime = System.currentTimeMillis() - authTime;
        if (nowTime > expiresTime * 60 * 1000) {
            throw new CommonException(ApiStatusEnum.SHA_TIMESTAMP_EXPIRE.getCode(), ApiStatusEnum.SHA_TIMESTAMP_EXPIRE.getMassage());
        }
        String method = request.getMethod();
        StringBuilder bodyStr = new StringBuilder();
        // POST和PUT才进行Body参数解析
        Map<String, Object> paramsMap = new HashMap<>();
        if ("POST".equals(method) || "PUT".equals(method)) {
            Map<String, Object> bodyMap = RequestUtils.getBodyMap(request);
            paramsMap.putAll(bodyMap);
//            if (request instanceof BaseRequestWrapper) {
//                Map<String, Object> bodyMap = RequestUtils.getBodyMap(request);
//                paramsMap.putAll(bodyMap);
//            }
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            if (request.getHeader("sign") == null && ("sign".equals(key) || "appkey".equals(key) || "timestamp".equals(key))) {
                continue;
            }
            String[] values = parameterMap.get(key);
            if (values.length == 1) {
                paramsMap.put(key, values[0]);
            } else {
                paramsMap.put(key, Arrays.asList(values));
            }
        }

        ArrayList<String> keys = new ArrayList<>(paramsMap.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            Object value = paramsMap.get(key);
            bodyStr.append(key + "=" + value.toString() + "&");
        }
        bodyStr.append("appkey=" + appKey + "&");
        bodyStr.append("timestamp=" + timeStamp);
        String appSecret = "";
        for (AppEntity app : appList) {
            if (app.getAppKey().equals(appKey)) {
                appSecret = app.getAppSecret();
            }
        }
        String signature = null;
        try {
            signature = HmacSHAUtils.HmacSHA256(bodyStr.toString(), appSecret);
        } catch (Exception e) {
            throw new CommonException(ApiStatusEnum.API_SIGN_ERROR.getCode(), ApiStatusEnum.API_SIGN_ERROR.getMassage());
        }
        if (sign.equalsIgnoreCase(signature)) {
            return true;
        }
        return false;
    }

}
