package com.lifetime.common.manager.service;


import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.service.BaseService;

import javax.management.relation.Role;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:39
 * @description: com.lifetime.manager.service
 * @Version:1.0
 */
public interface IRoleService extends BaseService<RoleEntity> {
    boolean isExist(String roleCode);
    RoleEntity findByRoleCode(String roleCode);
    List<RoleEntity> childEntity(String roleCode);

    List<RoleEntity> getRoleTreeByDept(String deptCode);

    List<RoleEntity> getRolesByUserCodeAndDept(String userCode,String deptCode);
}
