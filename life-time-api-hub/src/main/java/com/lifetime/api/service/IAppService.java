package com.lifetime.api.service;

import com.lifetime.api.entity.ApiSqlInfoEntity;
import com.lifetime.api.entity.AppEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

public interface IAppService extends BaseService<AppEntity> {
    List<AppEntity> getByApiCode(String apiCode);
}
