package com.lifetime.api.business;

import com.github.benmanes.caffeine.cache.Cache;
import com.lifetime.api.entity.*;
import com.lifetime.api.model.ApiCacheModel;
import com.lifetime.api.model.ApiModel;
import com.lifetime.api.service.*;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.dataSource.service.IDataSourceService;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:wangchao
 * @date: 2024/12/23-12:47
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */
@Component
public class ApiInfoBusiness {

    @Autowired
    IApiBaseInfoService iApiBaseInfoService;

    @Autowired
    IApiSqlInfoService iApiSqlInfoService;

    @Autowired
    IApiParamService iApiParamService;
    @Autowired
    IAppService iAppService;
    @Autowired
    IDataSourceService iLtDataSourceService;
    @Autowired
    @Qualifier("apiCache")
    Cache<String, Object> apiInfoCache;

    @Transactional
    public ResponseResult save(ApiModel model) {
        if (iApiBaseInfoService.isExist(model.getApiBaseInfo().getApiCode())) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iApiBaseInfoService.save(model.getApiBaseInfo());
            iApiSqlInfoService.save(model.getApiSqlInfo());
            for (ApiParamEntity apiParamEntity : model.getResponseParams()) {
                apiParamEntity.setParamModel("response");
            }
            for (ApiParamEntity apiParamEntity : model.getRequestParams()) {
                apiParamEntity.setParamModel("request");
            }
            iApiParamService.saveBatch(model.getRequestParams());
            iApiParamService.saveBatch(model.getResponseParams());
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }


    @Transactional
    public ResponseResult remove(String apiCode) {
        try {
            iApiBaseInfoService.deleteByCode(apiCode);
            iApiSqlInfoService.deleteByCode(apiCode);
            iApiParamService.deleteByCode(apiCode);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, exception.getMessage());
        }

    }

    @Transactional
    public ResponseResult update(ApiModel mode) {
        try {
            iApiBaseInfoService.saveOrUpdate(mode.getApiBaseInfo());
            iApiSqlInfoService.saveOrUpdate(mode.getApiSqlInfo());

            List<ApiParamEntity> list = new ArrayList<>();
            for (ApiParamEntity entity : mode.getRequestParams()) {
                entity.setParamModel("request");
                list.add(entity);
            }
            for (ApiParamEntity entity : mode.getResponseParams()) {
                entity.setParamModel("response");
                list.add(entity);
            }
            iApiParamService.saveOrUpdateBatch(list);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, exception.getMessage());
        }
    }

    @Transactional
    public ResponseResult publish(String apiCode) {
        ApiBaseInfoEntity apiBaseInfo = iApiBaseInfoService.getByApiCode(apiCode);
        apiBaseInfo.setIsPublish("1");
        apiBaseInfo.setReleaseTime(new Date());
        iApiBaseInfoService.updateById(apiBaseInfo);

        // 更新缓存
        Thread t = new Thread(() -> updateCache(apiCode));
        t.start();
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }

    public ApiModel buildApiModel(String apiCode) {
        ApiModel apiModel = new ApiModel();
        ApiBaseInfoEntity apiBaseInfo = iApiBaseInfoService.getByApiCode(apiCode);
        apiModel.setApiBaseInfo(apiBaseInfo);
        ApiSqlInfoEntity apiSqlInfo = iApiSqlInfoService.getByApiCode(apiCode);
        apiModel.setApiSqlInfo(apiSqlInfo);
        List<ApiParamEntity> apiParamEntities = iApiParamService.getByApiCode(apiCode);
        apiModel.setRequestParams(apiParamEntities.stream().filter(e -> e.getParamModel().equals("request")).collect(Collectors.toList()));
        apiModel.setResponseParams(apiParamEntities.stream().filter(e -> e.getParamModel().equals("response")).collect(Collectors.toList()));
        return apiModel;
    }

    public ApiCacheModel buildApiCacheModel(String apiCode) {
        ApiCacheModel apiCacheModel = new ApiCacheModel();
        apiCacheModel.setApiInfo(buildApiModel(apiCode));
        apiCacheModel.setApps(iAppService.getByApiCode(apiCode));
        return apiCacheModel;
    }


    public ApiCacheModel buildApiCacheModelByMethodAndUrl(String method, String url) {
        ApiCacheModel apiCacheModel = new ApiCacheModel();
        ApiBaseInfoEntity apiBaseInfo = iApiBaseInfoService.getByTypeAndMethodUrl(method, url);
        if (LtCommonUtil.isNotBlankOrNull(apiBaseInfo)) {
            apiCacheModel = buildApiCacheModel(apiBaseInfo.getApiCode());
        }
        return apiCacheModel;
    }


    public void updateCache(String apiCode) {
        ApiCacheModel apiCacheModel = this.buildApiCacheModel(apiCode);
        setCache(apiCacheModel);
    }

    public void setCache(ApiCacheModel apiCacheModel) {
        String key = apiCacheModel.getApiInfo().getApiBaseInfo().getApiMethod() + "_" + apiCacheModel.getApiInfo().getApiBaseInfo().getApiUrl();
        //key method+'_'+url
        apiInfoCache.put(key, apiCacheModel);
    }

    public ResponseResult search(SearchRequest searchRequest) {
        SearchResponse<ApiBaseInfoEntity> resultResponse = iApiBaseInfoService.searchList(searchRequest);
        SearchResponse<ApiModel> response = new SearchResponse<>();
        response.pageInfo = resultResponse.pageInfo;
        List<ApiBaseInfoEntity> resultList = resultResponse.results;
        List<ApiModel> list = new ArrayList<>();
        for (ApiBaseInfoEntity entity : resultList) {
            ApiModel model = new ApiModel();
            model.setApiBaseInfo(entity);
            ApiSqlInfoEntity apiSqlInfo = iApiSqlInfoService.getByApiCode(entity.getApiCode());
            model.setApiSqlInfo(apiSqlInfo);
            List<ApiParamEntity> apiParamEntities = iApiParamService.getByApiCode(entity.getApiCode());
            model.setRequestParams(apiParamEntities.stream().filter(e -> e.getParamModel().equals("request")).collect(Collectors.toList()));
            model.setResponseParams(apiParamEntities.stream().filter(e -> e.getParamModel().equals("response")).collect(Collectors.toList()));
            list.add(model);

        }
        response.results = list;
        return ResponseResult.success(response);
    }

    public ResponseResult execute(String datasourceId, String datasourceType, String schema, String operateType, String sql, Map<String, Object> params) {
        return ResponseResult.success(iLtDataSourceService.execute(datasourceId, datasourceType, schema, operateType, sql, params));
    }

}
