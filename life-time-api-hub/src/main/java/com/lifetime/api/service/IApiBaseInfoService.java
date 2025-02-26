package com.lifetime.api.service;

import com.lifetime.api.entity.ApiBaseInfoEntity;
import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

public interface IApiBaseInfoService extends BaseService<ApiBaseInfoEntity> {
    boolean isExist(String ApiCode);

    boolean deleteByCode(String code);
}
