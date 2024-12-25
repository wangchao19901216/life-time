package com.lifetime.common.manager.service;

import com.lifetime.common.manager.entity.UserDepartmentEntity;
import com.lifetime.common.manager.entity.UserRoleEntity;
import com.lifetime.common.service.BaseService;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/25-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IUserRoleService extends BaseService<UserRoleEntity> {

    List<UserRoleEntity> findByUserCode(String userCode,String deptCode);

    List<UserRoleEntity> findByRoleCode(String roleCode);
}
