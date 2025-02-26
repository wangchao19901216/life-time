package com.lifetime.api.service;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.common.service.BaseService;

public interface IApiSqlInfoService extends BaseService<ApiSqlInfoEntity> {
    boolean deleteByCode(String code);
    ApiSqlInfoEntity getByApiCode(String apiCode);
}
