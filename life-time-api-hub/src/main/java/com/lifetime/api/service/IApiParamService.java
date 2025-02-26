package com.lifetime.api.service;

import com.lifetime.api.entity.ApiParamEntity;
import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

public interface IApiParamService extends BaseService<ApiParamEntity> {
    boolean deleteByCode(String code);

    List<ApiParamEntity> getByApiCode(String apiCode);
}
