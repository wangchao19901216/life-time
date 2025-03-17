package com.lifetime.api.controller;

import com.lifetime.api.business.ApiInfoBusiness;
import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.model.ApiCacheModel;
import com.lifetime.api.model.ApiModel;
import com.lifetime.api.util.ApiThreadLocal;
import com.lifetime.common.constant.DataSourceConstants;
import com.lifetime.common.enums.ApiStatusEnum;
import com.lifetime.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author:wangchao
 * @date: 2025/2/25-09:46
 * @description: com.lifetime.api.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("api/common")
@Api(tags = "通用接口服务")
public class ApiServiceController {
    @Autowired
    ApiInfoBusiness apiInfoBusiness;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseResult getService(@RequestParam(required = false) Map<String, Object> paramMap) {
        ApiCacheModel api = ApiThreadLocal.get();
        if (api == null) {
            return ResponseResult.error(ApiStatusEnum.API_INVALID.getCode(), ApiStatusEnum.API_INVALID.getMassage());
        }
        if (paramMap != null) {
            paramMap.put(DataSourceConstants.PAGE_SETUP, api.getApiInfo().getApiBaseInfo().getIsPage());
            if (api.getApiInfo().getApiBaseInfo().getIsPage().equals("1")) {
                if (!(paramMap.containsKey(DataSourceConstants.PAGE_INDEX) && paramMap.containsKey(DataSourceConstants.PAGE_SIZE)))
                    return ResponseResult.error(ApiStatusEnum.PARAM_NOT_FOUNT.getCode(), ApiStatusEnum.PARAM_NOT_FOUNT.getMassage());
            }
            if (!checkParams(api.getApiInfo().getRequestParams(), paramMap)) {
                return ResponseResult.error(ApiStatusEnum.PAGE_NOT_FOUNT.getCode(), ApiStatusEnum.PAGE_NOT_FOUNT.getMassage());
            }
        }
        ApiModel apiModel = api.getApiInfo();

        return apiInfoBusiness.execute(
                apiModel.getApiSqlInfo().getDataSourceId(),
                apiModel.getApiSqlInfo().getDataSourceType(),
                apiModel.getApiSqlInfo().getSchemaName(),
                apiModel.getApiSqlInfo().getTableName(),
                apiModel.getApiSqlInfo().getSqlScript(),
                paramMap
        );
    }


    /**
     * API post请求
     *
     * @param paramMap
     * @param body
     * @return
     */
    @RequestMapping(value = "/**", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseResult postService(@RequestParam(required = false) Map<String, Object> paramMap, @RequestBody(required = false) Object body) {
        ApiCacheModel api = ApiThreadLocal.get();
        if (api == null) {
            return ResponseResult.error(ApiStatusEnum.API_INVALID.getCode(), ApiStatusEnum.API_INVALID.getMassage());
        }
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        if (body instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) body;
            paramMap.putAll(map);
        }
        // 校验参数
        if (paramMap != null) {
            paramMap.put(DataSourceConstants.PAGE_SETUP, api.getApiInfo().getApiBaseInfo().getIsPage());
            if (api.getApiInfo().getApiBaseInfo().getIsPage().equals("1")) {
                if (!(paramMap.containsKey(DataSourceConstants.PAGE_INDEX) && paramMap.containsKey(DataSourceConstants.PAGE_SIZE)))
                    return ResponseResult.error(ApiStatusEnum.PARAM_NOT_FOUNT.getCode(), ApiStatusEnum.PARAM_NOT_FOUNT.getMassage());
            }
            if (!checkParams(api.getApiInfo().getRequestParams(), paramMap)) {
                return ResponseResult.error(ApiStatusEnum.PAGE_NOT_FOUNT.getCode(), ApiStatusEnum.PAGE_NOT_FOUNT.getMassage());
            }
        }
        ApiModel apiModel = api.getApiInfo();
        return apiInfoBusiness.execute(
                apiModel.getApiSqlInfo().getDataSourceId(),
                apiModel.getApiSqlInfo().getDataSourceType(),
                apiModel.getApiSqlInfo().getSchemaName(),
                apiModel.getApiSqlInfo().getTableName(),
                apiModel.getApiSqlInfo().getSqlScript(),
                paramMap
        );
    }


    /**
     * 校验参数
     *
     * @param apiParams
     * @param paramMap
     * @return
     */
    public boolean checkParams(List<ApiParamEntity> apiParams, Map<String, Object> paramMap) {
        if (apiParams == null || paramMap == null) {
            return true;
        }
        for (ApiParamEntity param : apiParams) {
            String paramName = param.getParamName();
            String paramType = param.getParamType();
            if (!paramMap.containsKey(paramName) && param.getRequired().equals("1")) {
                return false;
            }
            Object value = paramMap.get(paramName);
            // Array类型的拼接参数 进行拆分
            if ("Array".equalsIgnoreCase(paramType) && value != null) {
                String[] values = value.toString().split(",");
                paramMap.put(paramName, Arrays.asList(values));
            } else if ("Array".equalsIgnoreCase(paramType) && "".equals(value)) {
                return false;
            }
        }
        return true;
    }
}
