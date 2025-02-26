package com.lifetime.api.business;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.model.ApiModel;
import com.lifetime.api.service.IApiBaseInfoService;
import com.lifetime.api.service.IApiGroupService;
import com.lifetime.api.service.IApiParamService;
import com.lifetime.api.service.IApiSqlInfoService;
import com.lifetime.common.constant.Constants;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.model.TreeModel;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED,exception.getMessage());
        }
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
}
