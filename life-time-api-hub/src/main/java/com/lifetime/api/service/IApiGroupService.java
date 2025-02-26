package com.lifetime.api.service;

import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

public interface IApiGroupService extends BaseService<ApiGroupEntity> {
    boolean isExist(String groupCode);

    List<ApiGroupEntity> childEntity(String groupCode);
}
