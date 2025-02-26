package com.lifetime.api.business;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.entity.AppEntity;
import com.lifetime.api.model.ApiModel;
import com.lifetime.api.service.IApiBaseInfoService;
import com.lifetime.api.service.IApiParamService;
import com.lifetime.api.service.IApiSqlInfoService;
import com.lifetime.api.service.IAppService;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.rmi.server.ExportException;
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
public class AppBusiness {

    @Autowired
    IAppService iAppService;

    @Transactional
    public ResponseResult save(AppEntity model) {
        try {
             iAppService.save(model);
             return  ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
        catch (Exception exception){
         return  ResponseResult.error(CommonExceptionEnum.DATA_SAVE_FAILED,exception.getMessage());
        }
    }


    @Transactional
    public ResponseResult remove(BigInteger id) {
        try {
            iAppService.removeById(id);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, exception.getMessage());
        }

    }


    public ResponseResult update(AppEntity mode) {
        try {
            iAppService.saveOrUpdate(mode);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        } catch (Exception exception) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, exception.getMessage());
        }
    }

    public ResponseResult search(SearchRequest searchRequest) {
        SearchResponse<AppEntity> resultResponse = iAppService.searchList(searchRequest);
        return ResponseResult.success(resultResponse);
    }
}
